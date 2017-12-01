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
import com.Entity.Invoice;
import com.Entity.Tax;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class TaxJpaController implements Serializable {

    public TaxJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tax tax) {
        if (tax.getInvoiceList() == null) {
            tax.setInvoiceList(new ArrayList<Invoice>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = tax.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                tax.setUserid(userid);
            }
            List<Invoice> attachedInvoiceList = new ArrayList<Invoice>();
            for (Invoice invoiceListInvoiceToAttach : tax.getInvoiceList()) {
                invoiceListInvoiceToAttach = em.getReference(invoiceListInvoiceToAttach.getClass(), invoiceListInvoiceToAttach.getId());
                attachedInvoiceList.add(invoiceListInvoiceToAttach);
            }
            tax.setInvoiceList(attachedInvoiceList);
            em.persist(tax);
            if (userid != null) {
                userid.getTaxList().add(tax);
                userid = em.merge(userid);
            }
            for (Invoice invoiceListInvoice : tax.getInvoiceList()) {
                Tax oldTaxidOfInvoiceListInvoice = invoiceListInvoice.getTaxid();
                invoiceListInvoice.setTaxid(tax);
                invoiceListInvoice = em.merge(invoiceListInvoice);
                if (oldTaxidOfInvoiceListInvoice != null) {
                    oldTaxidOfInvoiceListInvoice.getInvoiceList().remove(invoiceListInvoice);
                    oldTaxidOfInvoiceListInvoice = em.merge(oldTaxidOfInvoiceListInvoice);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tax tax) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tax persistentTax = em.find(Tax.class, tax.getId());
            User useridOld = persistentTax.getUserid();
            User useridNew = tax.getUserid();
            List<Invoice> invoiceListOld = persistentTax.getInvoiceList();
            List<Invoice> invoiceListNew = tax.getInvoiceList();
            List<String> illegalOrphanMessages = null;
            for (Invoice invoiceListOldInvoice : invoiceListOld) {
                if (!invoiceListNew.contains(invoiceListOldInvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Invoice " + invoiceListOldInvoice + " since its taxid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                tax.setUserid(useridNew);
            }
            List<Invoice> attachedInvoiceListNew = new ArrayList<Invoice>();
            for (Invoice invoiceListNewInvoiceToAttach : invoiceListNew) {
                invoiceListNewInvoiceToAttach = em.getReference(invoiceListNewInvoiceToAttach.getClass(), invoiceListNewInvoiceToAttach.getId());
                attachedInvoiceListNew.add(invoiceListNewInvoiceToAttach);
            }
            invoiceListNew = attachedInvoiceListNew;
            tax.setInvoiceList(invoiceListNew);
            tax = em.merge(tax);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getTaxList().remove(tax);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getTaxList().add(tax);
                useridNew = em.merge(useridNew);
            }
            for (Invoice invoiceListNewInvoice : invoiceListNew) {
                if (!invoiceListOld.contains(invoiceListNewInvoice)) {
                    Tax oldTaxidOfInvoiceListNewInvoice = invoiceListNewInvoice.getTaxid();
                    invoiceListNewInvoice.setTaxid(tax);
                    invoiceListNewInvoice = em.merge(invoiceListNewInvoice);
                    if (oldTaxidOfInvoiceListNewInvoice != null && !oldTaxidOfInvoiceListNewInvoice.equals(tax)) {
                        oldTaxidOfInvoiceListNewInvoice.getInvoiceList().remove(invoiceListNewInvoice);
                        oldTaxidOfInvoiceListNewInvoice = em.merge(oldTaxidOfInvoiceListNewInvoice);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tax.getId();
                if (findTax(id) == null) {
                    throw new NonexistentEntityException("The tax with id " + id + " no longer exists.");
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
            Tax tax;
            try {
                tax = em.getReference(Tax.class, id);
                tax.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tax with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Invoice> invoiceListOrphanCheck = tax.getInvoiceList();
            for (Invoice invoiceListOrphanCheckInvoice : invoiceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tax (" + tax + ") cannot be destroyed since the Invoice " + invoiceListOrphanCheckInvoice + " in its invoiceList field has a non-nullable taxid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = tax.getUserid();
            if (userid != null) {
                userid.getTaxList().remove(tax);
                userid = em.merge(userid);
            }
            em.remove(tax);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tax> findTaxEntities() {
        return findTaxEntities(true, -1, -1);
    }

    public List<Tax> findTaxEntities(int maxResults, int firstResult) {
        return findTaxEntities(false, maxResults, firstResult);
    }

    private List<Tax> findTaxEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tax.class));
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

    public Tax findTax(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tax.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaxCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tax> rt = cq.from(Tax.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
