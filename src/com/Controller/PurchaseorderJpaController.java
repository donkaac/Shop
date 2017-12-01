/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.IllegalOrphanException;
import com.Controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.User;
import com.Entity.PurchaseOrderHasItem;
import com.Entity.Purchaseorder;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class PurchaseorderJpaController implements Serializable {

    public PurchaseorderJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Purchaseorder purchaseorder) {
        if (purchaseorder.getPurchaseOrderHasItemList() == null) {
            purchaseorder.setPurchaseOrderHasItemList(new ArrayList<PurchaseOrderHasItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = purchaseorder.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                purchaseorder.setUserid(userid);
            }
            List<PurchaseOrderHasItem> attachedPurchaseOrderHasItemList = new ArrayList<PurchaseOrderHasItem>();
            for (PurchaseOrderHasItem purchaseOrderHasItemListPurchaseOrderHasItemToAttach : purchaseorder.getPurchaseOrderHasItemList()) {
                purchaseOrderHasItemListPurchaseOrderHasItemToAttach = em.getReference(purchaseOrderHasItemListPurchaseOrderHasItemToAttach.getClass(), purchaseOrderHasItemListPurchaseOrderHasItemToAttach.getId());
                attachedPurchaseOrderHasItemList.add(purchaseOrderHasItemListPurchaseOrderHasItemToAttach);
            }
            purchaseorder.setPurchaseOrderHasItemList(attachedPurchaseOrderHasItemList);
            em.persist(purchaseorder);
            if (userid != null) {
                userid.getPurchaseorderList().add(purchaseorder);
                userid = em.merge(userid);
            }
            for (PurchaseOrderHasItem purchaseOrderHasItemListPurchaseOrderHasItem : purchaseorder.getPurchaseOrderHasItemList()) {
                Purchaseorder oldPurchaseOrderidOfPurchaseOrderHasItemListPurchaseOrderHasItem = purchaseOrderHasItemListPurchaseOrderHasItem.getPurchaseOrderid();
                purchaseOrderHasItemListPurchaseOrderHasItem.setPurchaseOrderid(purchaseorder);
                purchaseOrderHasItemListPurchaseOrderHasItem = em.merge(purchaseOrderHasItemListPurchaseOrderHasItem);
                if (oldPurchaseOrderidOfPurchaseOrderHasItemListPurchaseOrderHasItem != null) {
                    oldPurchaseOrderidOfPurchaseOrderHasItemListPurchaseOrderHasItem.getPurchaseOrderHasItemList().remove(purchaseOrderHasItemListPurchaseOrderHasItem);
                    oldPurchaseOrderidOfPurchaseOrderHasItemListPurchaseOrderHasItem = em.merge(oldPurchaseOrderidOfPurchaseOrderHasItemListPurchaseOrderHasItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Purchaseorder purchaseorder) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Purchaseorder persistentPurchaseorder = em.find(Purchaseorder.class, purchaseorder.getId());
            User useridOld = persistentPurchaseorder.getUserid();
            User useridNew = purchaseorder.getUserid();
            List<PurchaseOrderHasItem> purchaseOrderHasItemListOld = persistentPurchaseorder.getPurchaseOrderHasItemList();
            List<PurchaseOrderHasItem> purchaseOrderHasItemListNew = purchaseorder.getPurchaseOrderHasItemList();
            List<String> illegalOrphanMessages = null;
            for (PurchaseOrderHasItem purchaseOrderHasItemListOldPurchaseOrderHasItem : purchaseOrderHasItemListOld) {
                if (!purchaseOrderHasItemListNew.contains(purchaseOrderHasItemListOldPurchaseOrderHasItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PurchaseOrderHasItem " + purchaseOrderHasItemListOldPurchaseOrderHasItem + " since its purchaseOrderid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                purchaseorder.setUserid(useridNew);
            }
            List<PurchaseOrderHasItem> attachedPurchaseOrderHasItemListNew = new ArrayList<PurchaseOrderHasItem>();
            for (PurchaseOrderHasItem purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach : purchaseOrderHasItemListNew) {
                purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach = em.getReference(purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach.getClass(), purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach.getId());
                attachedPurchaseOrderHasItemListNew.add(purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach);
            }
            purchaseOrderHasItemListNew = attachedPurchaseOrderHasItemListNew;
            purchaseorder.setPurchaseOrderHasItemList(purchaseOrderHasItemListNew);
            purchaseorder = em.merge(purchaseorder);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getPurchaseorderList().remove(purchaseorder);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getPurchaseorderList().add(purchaseorder);
                useridNew = em.merge(useridNew);
            }
            for (PurchaseOrderHasItem purchaseOrderHasItemListNewPurchaseOrderHasItem : purchaseOrderHasItemListNew) {
                if (!purchaseOrderHasItemListOld.contains(purchaseOrderHasItemListNewPurchaseOrderHasItem)) {
                    Purchaseorder oldPurchaseOrderidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem = purchaseOrderHasItemListNewPurchaseOrderHasItem.getPurchaseOrderid();
                    purchaseOrderHasItemListNewPurchaseOrderHasItem.setPurchaseOrderid(purchaseorder);
                    purchaseOrderHasItemListNewPurchaseOrderHasItem = em.merge(purchaseOrderHasItemListNewPurchaseOrderHasItem);
                    if (oldPurchaseOrderidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem != null && !oldPurchaseOrderidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem.equals(purchaseorder)) {
                        oldPurchaseOrderidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem.getPurchaseOrderHasItemList().remove(purchaseOrderHasItemListNewPurchaseOrderHasItem);
                        oldPurchaseOrderidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem = em.merge(oldPurchaseOrderidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = purchaseorder.getId();
                if (findPurchaseorder(id) == null) {
                    throw new NonexistentEntityException("The purchaseorder with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Purchaseorder purchaseorder;
            try {
                purchaseorder = em.getReference(Purchaseorder.class, id);
                purchaseorder.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The purchaseorder with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PurchaseOrderHasItem> purchaseOrderHasItemListOrphanCheck = purchaseorder.getPurchaseOrderHasItemList();
            for (PurchaseOrderHasItem purchaseOrderHasItemListOrphanCheckPurchaseOrderHasItem : purchaseOrderHasItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Purchaseorder (" + purchaseorder + ") cannot be destroyed since the PurchaseOrderHasItem " + purchaseOrderHasItemListOrphanCheckPurchaseOrderHasItem + " in its purchaseOrderHasItemList field has a non-nullable purchaseOrderid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = purchaseorder.getUserid();
            if (userid != null) {
                userid.getPurchaseorderList().remove(purchaseorder);
                userid = em.merge(userid);
            }
            em.remove(purchaseorder);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Purchaseorder> findPurchaseorderEntities() {
        return findPurchaseorderEntities(true, -1, -1);
    }

    public List<Purchaseorder> findPurchaseorderEntities(int maxResults, int firstResult) {
        return findPurchaseorderEntities(false, maxResults, firstResult);
    }

    private List<Purchaseorder> findPurchaseorderEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Purchaseorder.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Purchaseorder findPurchaseorder(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Purchaseorder.class, id);
        } finally {
            em.close();
        }
    }

    public int getPurchaseorderCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Purchaseorder> rt = cq.from(Purchaseorder.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
