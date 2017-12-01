/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.IllegalOrphanException;
import com.Controller.exceptions.NonexistentEntityException;
import com.Entity.Grn;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.Supplier;
import com.Entity.User;
import com.Entity.GrnHasItem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class GrnJpaController implements Serializable {

    public GrnJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grn grn) {
        if (grn.getGrnHasItemList() == null) {
            grn.setGrnHasItemList(new ArrayList<GrnHasItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier supplierid = grn.getSupplierid();
            if (supplierid != null) {
                supplierid = em.getReference(supplierid.getClass(), supplierid.getId());
                grn.setSupplierid(supplierid);
            }
            User userid = grn.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                grn.setUserid(userid);
            }
            List<GrnHasItem> attachedGrnHasItemList = new ArrayList<GrnHasItem>();
            for (GrnHasItem grnHasItemListGrnHasItemToAttach : grn.getGrnHasItemList()) {
                grnHasItemListGrnHasItemToAttach = em.getReference(grnHasItemListGrnHasItemToAttach.getClass(), grnHasItemListGrnHasItemToAttach.getId());
                attachedGrnHasItemList.add(grnHasItemListGrnHasItemToAttach);
            }
            grn.setGrnHasItemList(attachedGrnHasItemList);
            em.persist(grn);
            if (supplierid != null) {
                supplierid.getGrnList().add(grn);
                supplierid = em.merge(supplierid);
            }
            if (userid != null) {
                userid.getGrnList().add(grn);
                userid = em.merge(userid);
            }
            for (GrnHasItem grnHasItemListGrnHasItem : grn.getGrnHasItemList()) {
                Grn oldGrnidOfGrnHasItemListGrnHasItem = grnHasItemListGrnHasItem.getGrnid();
                grnHasItemListGrnHasItem.setGrnid(grn);
                grnHasItemListGrnHasItem = em.merge(grnHasItemListGrnHasItem);
                if (oldGrnidOfGrnHasItemListGrnHasItem != null) {
                    oldGrnidOfGrnHasItemListGrnHasItem.getGrnHasItemList().remove(grnHasItemListGrnHasItem);
                    oldGrnidOfGrnHasItemListGrnHasItem = em.merge(oldGrnidOfGrnHasItemListGrnHasItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grn grn) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grn persistentGrn = em.find(Grn.class, grn.getId());
            Supplier supplieridOld = persistentGrn.getSupplierid();
            Supplier supplieridNew = grn.getSupplierid();
            User useridOld = persistentGrn.getUserid();
            User useridNew = grn.getUserid();
            List<GrnHasItem> grnHasItemListOld = persistentGrn.getGrnHasItemList();
            List<GrnHasItem> grnHasItemListNew = grn.getGrnHasItemList();
            List<String> illegalOrphanMessages = null;
            for (GrnHasItem grnHasItemListOldGrnHasItem : grnHasItemListOld) {
                if (!grnHasItemListNew.contains(grnHasItemListOldGrnHasItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GrnHasItem " + grnHasItemListOldGrnHasItem + " since its grnid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (supplieridNew != null) {
                supplieridNew = em.getReference(supplieridNew.getClass(), supplieridNew.getId());
                grn.setSupplierid(supplieridNew);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                grn.setUserid(useridNew);
            }
            List<GrnHasItem> attachedGrnHasItemListNew = new ArrayList<GrnHasItem>();
            for (GrnHasItem grnHasItemListNewGrnHasItemToAttach : grnHasItemListNew) {
                grnHasItemListNewGrnHasItemToAttach = em.getReference(grnHasItemListNewGrnHasItemToAttach.getClass(), grnHasItemListNewGrnHasItemToAttach.getId());
                attachedGrnHasItemListNew.add(grnHasItemListNewGrnHasItemToAttach);
            }
            grnHasItemListNew = attachedGrnHasItemListNew;
            grn.setGrnHasItemList(grnHasItemListNew);
            grn = em.merge(grn);
            if (supplieridOld != null && !supplieridOld.equals(supplieridNew)) {
                supplieridOld.getGrnList().remove(grn);
                supplieridOld = em.merge(supplieridOld);
            }
            if (supplieridNew != null && !supplieridNew.equals(supplieridOld)) {
                supplieridNew.getGrnList().add(grn);
                supplieridNew = em.merge(supplieridNew);
            }
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getGrnList().remove(grn);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getGrnList().add(grn);
                useridNew = em.merge(useridNew);
            }
            for (GrnHasItem grnHasItemListNewGrnHasItem : grnHasItemListNew) {
                if (!grnHasItemListOld.contains(grnHasItemListNewGrnHasItem)) {
                    Grn oldGrnidOfGrnHasItemListNewGrnHasItem = grnHasItemListNewGrnHasItem.getGrnid();
                    grnHasItemListNewGrnHasItem.setGrnid(grn);
                    grnHasItemListNewGrnHasItem = em.merge(grnHasItemListNewGrnHasItem);
                    if (oldGrnidOfGrnHasItemListNewGrnHasItem != null && !oldGrnidOfGrnHasItemListNewGrnHasItem.equals(grn)) {
                        oldGrnidOfGrnHasItemListNewGrnHasItem.getGrnHasItemList().remove(grnHasItemListNewGrnHasItem);
                        oldGrnidOfGrnHasItemListNewGrnHasItem = em.merge(oldGrnidOfGrnHasItemListNewGrnHasItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grn.getId();
                if (findGrn(id) == null) {
                    throw new NonexistentEntityException("The grn with id " + id + " no longer exists.");
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
            Grn grn;
            try {
                grn = em.getReference(Grn.class, id);
                grn.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grn with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GrnHasItem> grnHasItemListOrphanCheck = grn.getGrnHasItemList();
            for (GrnHasItem grnHasItemListOrphanCheckGrnHasItem : grnHasItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grn (" + grn + ") cannot be destroyed since the GrnHasItem " + grnHasItemListOrphanCheckGrnHasItem + " in its grnHasItemList field has a non-nullable grnid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Supplier supplierid = grn.getSupplierid();
            if (supplierid != null) {
                supplierid.getGrnList().remove(grn);
                supplierid = em.merge(supplierid);
            }
            User userid = grn.getUserid();
            if (userid != null) {
                userid.getGrnList().remove(grn);
                userid = em.merge(userid);
            }
            em.remove(grn);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grn> findGrnEntities() {
        return findGrnEntities(true, -1, -1);
    }

    public List<Grn> findGrnEntities(int maxResults, int firstResult) {
        return findGrnEntities(false, maxResults, firstResult);
    }

    private List<Grn> findGrnEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grn.class));
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

    public Grn findGrn(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grn.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrnCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grn> rt = cq.from(Grn.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
