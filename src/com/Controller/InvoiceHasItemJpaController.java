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
import com.Entity.Invoice;
import com.Entity.Batch;
import com.Entity.InvoiceHasItem;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class InvoiceHasItemJpaController implements Serializable {

    public InvoiceHasItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InvoiceHasItem invoiceHasItem) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Invoice invoiceid = invoiceHasItem.getInvoiceid();
            if (invoiceid != null) {
                invoiceid = em.getReference(invoiceid.getClass(), invoiceid.getId());
                invoiceHasItem.setInvoiceid(invoiceid);
            }
            Batch batchid = invoiceHasItem.getBatchid();
            if (batchid != null) {
                batchid = em.getReference(batchid.getClass(), batchid.getId());
                invoiceHasItem.setBatchid(batchid);
            }
            em.persist(invoiceHasItem);
            if (invoiceid != null) {
                invoiceid.getInvoiceHasItemList().add(invoiceHasItem);
                invoiceid = em.merge(invoiceid);
            }
            if (batchid != null) {
                batchid.getInvoiceHasItemList().add(invoiceHasItem);
                batchid = em.merge(batchid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InvoiceHasItem invoiceHasItem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InvoiceHasItem persistentInvoiceHasItem = em.find(InvoiceHasItem.class, invoiceHasItem.getId());
            Invoice invoiceidOld = persistentInvoiceHasItem.getInvoiceid();
            Invoice invoiceidNew = invoiceHasItem.getInvoiceid();
            Batch batchidOld = persistentInvoiceHasItem.getBatchid();
            Batch batchidNew = invoiceHasItem.getBatchid();
            if (invoiceidNew != null) {
                invoiceidNew = em.getReference(invoiceidNew.getClass(), invoiceidNew.getId());
                invoiceHasItem.setInvoiceid(invoiceidNew);
            }
            if (batchidNew != null) {
                batchidNew = em.getReference(batchidNew.getClass(), batchidNew.getId());
                invoiceHasItem.setBatchid(batchidNew);
            }
            invoiceHasItem = em.merge(invoiceHasItem);
            if (invoiceidOld != null && !invoiceidOld.equals(invoiceidNew)) {
                invoiceidOld.getInvoiceHasItemList().remove(invoiceHasItem);
                invoiceidOld = em.merge(invoiceidOld);
            }
            if (invoiceidNew != null && !invoiceidNew.equals(invoiceidOld)) {
                invoiceidNew.getInvoiceHasItemList().add(invoiceHasItem);
                invoiceidNew = em.merge(invoiceidNew);
            }
            if (batchidOld != null && !batchidOld.equals(batchidNew)) {
                batchidOld.getInvoiceHasItemList().remove(invoiceHasItem);
                batchidOld = em.merge(batchidOld);
            }
            if (batchidNew != null && !batchidNew.equals(batchidOld)) {
                batchidNew.getInvoiceHasItemList().add(invoiceHasItem);
                batchidNew = em.merge(batchidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = invoiceHasItem.getId();
                if (findInvoiceHasItem(id) == null) {
                    throw new NonexistentEntityException("The invoiceHasItem with id " + id + " no longer exists.");
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
            InvoiceHasItem invoiceHasItem;
            try {
                invoiceHasItem = em.getReference(InvoiceHasItem.class, id);
                invoiceHasItem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The invoiceHasItem with id " + id + " no longer exists.", enfe);
            }
            Invoice invoiceid = invoiceHasItem.getInvoiceid();
            if (invoiceid != null) {
                invoiceid.getInvoiceHasItemList().remove(invoiceHasItem);
                invoiceid = em.merge(invoiceid);
            }
            Batch batchid = invoiceHasItem.getBatchid();
            if (batchid != null) {
                batchid.getInvoiceHasItemList().remove(invoiceHasItem);
                batchid = em.merge(batchid);
            }
            em.remove(invoiceHasItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InvoiceHasItem> findInvoiceHasItemEntities() {
        return findInvoiceHasItemEntities(true, -1, -1);
    }

    public List<InvoiceHasItem> findInvoiceHasItemEntities(int maxResults, int firstResult) {
        return findInvoiceHasItemEntities(false, maxResults, firstResult);
    }

    private List<InvoiceHasItem> findInvoiceHasItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InvoiceHasItem.class));
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

    public InvoiceHasItem findInvoiceHasItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InvoiceHasItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getInvoiceHasItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InvoiceHasItem> rt = cq.from(InvoiceHasItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
