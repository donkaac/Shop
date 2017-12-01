/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.IllegalOrphanException;
import com.Controller.exceptions.NonexistentEntityException;
import com.Entity.Batch;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.User;
import com.Entity.GrnHasItem;
import com.Entity.InvoiceHasItem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class BatchJpaController implements Serializable {

    public BatchJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Batch batch) {
        if (batch.getInvoiceHasItemList() == null) {
            batch.setInvoiceHasItemList(new ArrayList<InvoiceHasItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = batch.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                batch.setUserid(userid);
            }
            GrnHasItem grnHasItemid = batch.getGrnHasItemid();
            if (grnHasItemid != null) {
                grnHasItemid = em.getReference(grnHasItemid.getClass(), grnHasItemid.getId());
                batch.setGrnHasItemid(grnHasItemid);
            }
            List<InvoiceHasItem> attachedInvoiceHasItemList = new ArrayList<InvoiceHasItem>();
            for (InvoiceHasItem invoiceHasItemListInvoiceHasItemToAttach : batch.getInvoiceHasItemList()) {
                invoiceHasItemListInvoiceHasItemToAttach = em.getReference(invoiceHasItemListInvoiceHasItemToAttach.getClass(), invoiceHasItemListInvoiceHasItemToAttach.getId());
                attachedInvoiceHasItemList.add(invoiceHasItemListInvoiceHasItemToAttach);
            }
            batch.setInvoiceHasItemList(attachedInvoiceHasItemList);
            em.persist(batch);
            if (userid != null) {
                userid.getBatchList().add(batch);
                userid = em.merge(userid);
            }
            if (grnHasItemid != null) {
                grnHasItemid.getBatchList().add(batch);
                grnHasItemid = em.merge(grnHasItemid);
            }
            for (InvoiceHasItem invoiceHasItemListInvoiceHasItem : batch.getInvoiceHasItemList()) {
                Batch oldBatchidOfInvoiceHasItemListInvoiceHasItem = invoiceHasItemListInvoiceHasItem.getBatchid();
                invoiceHasItemListInvoiceHasItem.setBatchid(batch);
                invoiceHasItemListInvoiceHasItem = em.merge(invoiceHasItemListInvoiceHasItem);
                if (oldBatchidOfInvoiceHasItemListInvoiceHasItem != null) {
                    oldBatchidOfInvoiceHasItemListInvoiceHasItem.getInvoiceHasItemList().remove(invoiceHasItemListInvoiceHasItem);
                    oldBatchidOfInvoiceHasItemListInvoiceHasItem = em.merge(oldBatchidOfInvoiceHasItemListInvoiceHasItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Batch batch) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Batch persistentBatch = em.find(Batch.class, batch.getId());
            User useridOld = persistentBatch.getUserid();
            User useridNew = batch.getUserid();
            GrnHasItem grnHasItemidOld = persistentBatch.getGrnHasItemid();
            GrnHasItem grnHasItemidNew = batch.getGrnHasItemid();
            List<InvoiceHasItem> invoiceHasItemListOld = persistentBatch.getInvoiceHasItemList();
            List<InvoiceHasItem> invoiceHasItemListNew = batch.getInvoiceHasItemList();
            List<String> illegalOrphanMessages = null;
            for (InvoiceHasItem invoiceHasItemListOldInvoiceHasItem : invoiceHasItemListOld) {
                if (!invoiceHasItemListNew.contains(invoiceHasItemListOldInvoiceHasItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InvoiceHasItem " + invoiceHasItemListOldInvoiceHasItem + " since its batchid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                batch.setUserid(useridNew);
            }
            if (grnHasItemidNew != null) {
                grnHasItemidNew = em.getReference(grnHasItemidNew.getClass(), grnHasItemidNew.getId());
                batch.setGrnHasItemid(grnHasItemidNew);
            }
            List<InvoiceHasItem> attachedInvoiceHasItemListNew = new ArrayList<InvoiceHasItem>();
            for (InvoiceHasItem invoiceHasItemListNewInvoiceHasItemToAttach : invoiceHasItemListNew) {
                invoiceHasItemListNewInvoiceHasItemToAttach = em.getReference(invoiceHasItemListNewInvoiceHasItemToAttach.getClass(), invoiceHasItemListNewInvoiceHasItemToAttach.getId());
                attachedInvoiceHasItemListNew.add(invoiceHasItemListNewInvoiceHasItemToAttach);
            }
            invoiceHasItemListNew = attachedInvoiceHasItemListNew;
            batch.setInvoiceHasItemList(invoiceHasItemListNew);
            batch = em.merge(batch);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getBatchList().remove(batch);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getBatchList().add(batch);
                useridNew = em.merge(useridNew);
            }
            if (grnHasItemidOld != null && !grnHasItemidOld.equals(grnHasItemidNew)) {
                grnHasItemidOld.getBatchList().remove(batch);
                grnHasItemidOld = em.merge(grnHasItemidOld);
            }
            if (grnHasItemidNew != null && !grnHasItemidNew.equals(grnHasItemidOld)) {
                grnHasItemidNew.getBatchList().add(batch);
                grnHasItemidNew = em.merge(grnHasItemidNew);
            }
            for (InvoiceHasItem invoiceHasItemListNewInvoiceHasItem : invoiceHasItemListNew) {
                if (!invoiceHasItemListOld.contains(invoiceHasItemListNewInvoiceHasItem)) {
                    Batch oldBatchidOfInvoiceHasItemListNewInvoiceHasItem = invoiceHasItemListNewInvoiceHasItem.getBatchid();
                    invoiceHasItemListNewInvoiceHasItem.setBatchid(batch);
                    invoiceHasItemListNewInvoiceHasItem = em.merge(invoiceHasItemListNewInvoiceHasItem);
                    if (oldBatchidOfInvoiceHasItemListNewInvoiceHasItem != null && !oldBatchidOfInvoiceHasItemListNewInvoiceHasItem.equals(batch)) {
                        oldBatchidOfInvoiceHasItemListNewInvoiceHasItem.getInvoiceHasItemList().remove(invoiceHasItemListNewInvoiceHasItem);
                        oldBatchidOfInvoiceHasItemListNewInvoiceHasItem = em.merge(oldBatchidOfInvoiceHasItemListNewInvoiceHasItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = batch.getId();
                if (findBatch(id) == null) {
                    throw new NonexistentEntityException("The batch with id " + id + " no longer exists.");
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
            Batch batch;
            try {
                batch = em.getReference(Batch.class, id);
                batch.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The batch with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<InvoiceHasItem> invoiceHasItemListOrphanCheck = batch.getInvoiceHasItemList();
            for (InvoiceHasItem invoiceHasItemListOrphanCheckInvoiceHasItem : invoiceHasItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Batch (" + batch + ") cannot be destroyed since the InvoiceHasItem " + invoiceHasItemListOrphanCheckInvoiceHasItem + " in its invoiceHasItemList field has a non-nullable batchid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = batch.getUserid();
            if (userid != null) {
                userid.getBatchList().remove(batch);
                userid = em.merge(userid);
            }
            GrnHasItem grnHasItemid = batch.getGrnHasItemid();
            if (grnHasItemid != null) {
                grnHasItemid.getBatchList().remove(batch);
                grnHasItemid = em.merge(grnHasItemid);
            }
            em.remove(batch);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Batch> findBatchEntities() {
        return findBatchEntities(true, -1, -1);
    }

    public List<Batch> findBatchEntities(int maxResults, int firstResult) {
        return findBatchEntities(false, maxResults, firstResult);
    }

    private List<Batch> findBatchEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Batch.class));
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

    public Batch findBatch(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Batch.class, id);
        } finally {
            em.close();
        }
    }

    public int getBatchCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Batch> rt = cq.from(Batch.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
