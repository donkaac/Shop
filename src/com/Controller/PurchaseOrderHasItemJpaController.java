/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.Purchaseorder;
import com.Entity.Item;
import com.Entity.PurchaseOrderHasItem;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class PurchaseOrderHasItemJpaController implements Serializable {

    public PurchaseOrderHasItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PurchaseOrderHasItem purchaseOrderHasItem) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Purchaseorder purchaseOrderid = purchaseOrderHasItem.getPurchaseOrderid();
            if (purchaseOrderid != null) {
                purchaseOrderid = em.getReference(purchaseOrderid.getClass(), purchaseOrderid.getId());
                purchaseOrderHasItem.setPurchaseOrderid(purchaseOrderid);
            }
            Item itemid = purchaseOrderHasItem.getItemid();
            if (itemid != null) {
                itemid = em.getReference(itemid.getClass(), itemid.getId());
                purchaseOrderHasItem.setItemid(itemid);
            }
            em.persist(purchaseOrderHasItem);
            if (purchaseOrderid != null) {
                purchaseOrderid.getPurchaseOrderHasItemList().add(purchaseOrderHasItem);
                purchaseOrderid = em.merge(purchaseOrderid);
            }
            if (itemid != null) {
                itemid.getPurchaseOrderHasItemList().add(purchaseOrderHasItem);
                itemid = em.merge(itemid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PurchaseOrderHasItem purchaseOrderHasItem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PurchaseOrderHasItem persistentPurchaseOrderHasItem = em.find(PurchaseOrderHasItem.class, purchaseOrderHasItem.getId());
            Purchaseorder purchaseOrderidOld = persistentPurchaseOrderHasItem.getPurchaseOrderid();
            Purchaseorder purchaseOrderidNew = purchaseOrderHasItem.getPurchaseOrderid();
            Item itemidOld = persistentPurchaseOrderHasItem.getItemid();
            Item itemidNew = purchaseOrderHasItem.getItemid();
            if (purchaseOrderidNew != null) {
                purchaseOrderidNew = em.getReference(purchaseOrderidNew.getClass(), purchaseOrderidNew.getId());
                purchaseOrderHasItem.setPurchaseOrderid(purchaseOrderidNew);
            }
            if (itemidNew != null) {
                itemidNew = em.getReference(itemidNew.getClass(), itemidNew.getId());
                purchaseOrderHasItem.setItemid(itemidNew);
            }
            purchaseOrderHasItem = em.merge(purchaseOrderHasItem);
            if (purchaseOrderidOld != null && !purchaseOrderidOld.equals(purchaseOrderidNew)) {
                purchaseOrderidOld.getPurchaseOrderHasItemList().remove(purchaseOrderHasItem);
                purchaseOrderidOld = em.merge(purchaseOrderidOld);
            }
            if (purchaseOrderidNew != null && !purchaseOrderidNew.equals(purchaseOrderidOld)) {
                purchaseOrderidNew.getPurchaseOrderHasItemList().add(purchaseOrderHasItem);
                purchaseOrderidNew = em.merge(purchaseOrderidNew);
            }
            if (itemidOld != null && !itemidOld.equals(itemidNew)) {
                itemidOld.getPurchaseOrderHasItemList().remove(purchaseOrderHasItem);
                itemidOld = em.merge(itemidOld);
            }
            if (itemidNew != null && !itemidNew.equals(itemidOld)) {
                itemidNew.getPurchaseOrderHasItemList().add(purchaseOrderHasItem);
                itemidNew = em.merge(itemidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = purchaseOrderHasItem.getId();
                if (findPurchaseOrderHasItem(id) == null) {
                    throw new NonexistentEntityException("The purchaseOrderHasItem with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PurchaseOrderHasItem purchaseOrderHasItem;
            try {
                purchaseOrderHasItem = em.getReference(PurchaseOrderHasItem.class, id);
                purchaseOrderHasItem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The purchaseOrderHasItem with id " + id + " no longer exists.", enfe);
            }
            Purchaseorder purchaseOrderid = purchaseOrderHasItem.getPurchaseOrderid();
            if (purchaseOrderid != null) {
                purchaseOrderid.getPurchaseOrderHasItemList().remove(purchaseOrderHasItem);
                purchaseOrderid = em.merge(purchaseOrderid);
            }
            Item itemid = purchaseOrderHasItem.getItemid();
            if (itemid != null) {
                itemid.getPurchaseOrderHasItemList().remove(purchaseOrderHasItem);
                itemid = em.merge(itemid);
            }
            em.remove(purchaseOrderHasItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PurchaseOrderHasItem> findPurchaseOrderHasItemEntities() {
        return findPurchaseOrderHasItemEntities(true, -1, -1);
    }

    public List<PurchaseOrderHasItem> findPurchaseOrderHasItemEntities(int maxResults, int firstResult) {
        return findPurchaseOrderHasItemEntities(false, maxResults, firstResult);
    }

    private List<PurchaseOrderHasItem> findPurchaseOrderHasItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PurchaseOrderHasItem.class));
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

    public PurchaseOrderHasItem findPurchaseOrderHasItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PurchaseOrderHasItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getPurchaseOrderHasItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PurchaseOrderHasItem> rt = cq.from(PurchaseOrderHasItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
