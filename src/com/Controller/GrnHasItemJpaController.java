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
import com.Entity.Item;
import com.Entity.Grn;
import com.Entity.Batch;
import com.Entity.GrnHasItem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class GrnHasItemJpaController implements Serializable {

    public GrnHasItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GrnHasItem grnHasItem) {
        if (grnHasItem.getBatchList() == null) {
            grnHasItem.setBatchList(new ArrayList<Batch>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item itemid = grnHasItem.getItemid();
            if (itemid != null) {
                itemid = em.getReference(itemid.getClass(), itemid.getId());
                grnHasItem.setItemid(itemid);
            }
            Grn grnid = grnHasItem.getGrnid();
            if (grnid != null) {
                grnid = em.getReference(grnid.getClass(), grnid.getId());
                grnHasItem.setGrnid(grnid);
            }
            List<Batch> attachedBatchList = new ArrayList<Batch>();
            for (Batch batchListBatchToAttach : grnHasItem.getBatchList()) {
                batchListBatchToAttach = em.getReference(batchListBatchToAttach.getClass(), batchListBatchToAttach.getId());
                attachedBatchList.add(batchListBatchToAttach);
            }
            grnHasItem.setBatchList(attachedBatchList);
            em.persist(grnHasItem);
            if (itemid != null) {
                itemid.getGrnHasItemList().add(grnHasItem);
                itemid = em.merge(itemid);
            }
            if (grnid != null) {
                grnid.getGrnHasItemList().add(grnHasItem);
                grnid = em.merge(grnid);
            }
            for (Batch batchListBatch : grnHasItem.getBatchList()) {
                GrnHasItem oldGrnHasItemidOfBatchListBatch = batchListBatch.getGrnHasItemid();
                batchListBatch.setGrnHasItemid(grnHasItem);
                batchListBatch = em.merge(batchListBatch);
                if (oldGrnHasItemidOfBatchListBatch != null) {
                    oldGrnHasItemidOfBatchListBatch.getBatchList().remove(batchListBatch);
                    oldGrnHasItemidOfBatchListBatch = em.merge(oldGrnHasItemidOfBatchListBatch);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GrnHasItem grnHasItem) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GrnHasItem persistentGrnHasItem = em.find(GrnHasItem.class, grnHasItem.getId());
            Item itemidOld = persistentGrnHasItem.getItemid();
            Item itemidNew = grnHasItem.getItemid();
            Grn grnidOld = persistentGrnHasItem.getGrnid();
            Grn grnidNew = grnHasItem.getGrnid();
            List<Batch> batchListOld = persistentGrnHasItem.getBatchList();
            List<Batch> batchListNew = grnHasItem.getBatchList();
            List<String> illegalOrphanMessages = null;
            for (Batch batchListOldBatch : batchListOld) {
                if (!batchListNew.contains(batchListOldBatch)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Batch " + batchListOldBatch + " since its grnHasItemid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (itemidNew != null) {
                itemidNew = em.getReference(itemidNew.getClass(), itemidNew.getId());
                grnHasItem.setItemid(itemidNew);
            }
            if (grnidNew != null) {
                grnidNew = em.getReference(grnidNew.getClass(), grnidNew.getId());
                grnHasItem.setGrnid(grnidNew);
            }
            List<Batch> attachedBatchListNew = new ArrayList<Batch>();
            for (Batch batchListNewBatchToAttach : batchListNew) {
                batchListNewBatchToAttach = em.getReference(batchListNewBatchToAttach.getClass(), batchListNewBatchToAttach.getId());
                attachedBatchListNew.add(batchListNewBatchToAttach);
            }
            batchListNew = attachedBatchListNew;
            grnHasItem.setBatchList(batchListNew);
            grnHasItem = em.merge(grnHasItem);
            if (itemidOld != null && !itemidOld.equals(itemidNew)) {
                itemidOld.getGrnHasItemList().remove(grnHasItem);
                itemidOld = em.merge(itemidOld);
            }
            if (itemidNew != null && !itemidNew.equals(itemidOld)) {
                itemidNew.getGrnHasItemList().add(grnHasItem);
                itemidNew = em.merge(itemidNew);
            }
            if (grnidOld != null && !grnidOld.equals(grnidNew)) {
                grnidOld.getGrnHasItemList().remove(grnHasItem);
                grnidOld = em.merge(grnidOld);
            }
            if (grnidNew != null && !grnidNew.equals(grnidOld)) {
                grnidNew.getGrnHasItemList().add(grnHasItem);
                grnidNew = em.merge(grnidNew);
            }
            for (Batch batchListNewBatch : batchListNew) {
                if (!batchListOld.contains(batchListNewBatch)) {
                    GrnHasItem oldGrnHasItemidOfBatchListNewBatch = batchListNewBatch.getGrnHasItemid();
                    batchListNewBatch.setGrnHasItemid(grnHasItem);
                    batchListNewBatch = em.merge(batchListNewBatch);
                    if (oldGrnHasItemidOfBatchListNewBatch != null && !oldGrnHasItemidOfBatchListNewBatch.equals(grnHasItem)) {
                        oldGrnHasItemidOfBatchListNewBatch.getBatchList().remove(batchListNewBatch);
                        oldGrnHasItemidOfBatchListNewBatch = em.merge(oldGrnHasItemidOfBatchListNewBatch);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grnHasItem.getId();
                if (findGrnHasItem(id) == null) {
                    throw new NonexistentEntityException("The grnHasItem with id " + id + " no longer exists.");
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
            GrnHasItem grnHasItem;
            try {
                grnHasItem = em.getReference(GrnHasItem.class, id);
                grnHasItem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grnHasItem with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Batch> batchListOrphanCheck = grnHasItem.getBatchList();
            for (Batch batchListOrphanCheckBatch : batchListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GrnHasItem (" + grnHasItem + ") cannot be destroyed since the Batch " + batchListOrphanCheckBatch + " in its batchList field has a non-nullable grnHasItemid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Item itemid = grnHasItem.getItemid();
            if (itemid != null) {
                itemid.getGrnHasItemList().remove(grnHasItem);
                itemid = em.merge(itemid);
            }
            Grn grnid = grnHasItem.getGrnid();
            if (grnid != null) {
                grnid.getGrnHasItemList().remove(grnHasItem);
                grnid = em.merge(grnid);
            }
            em.remove(grnHasItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GrnHasItem> findGrnHasItemEntities() {
        return findGrnHasItemEntities(true, -1, -1);
    }

    public List<GrnHasItem> findGrnHasItemEntities(int maxResults, int firstResult) {
        return findGrnHasItemEntities(false, maxResults, firstResult);
    }

    private List<GrnHasItem> findGrnHasItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GrnHasItem.class));
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

    public GrnHasItem findGrnHasItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GrnHasItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrnHasItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GrnHasItem> rt = cq.from(GrnHasItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
