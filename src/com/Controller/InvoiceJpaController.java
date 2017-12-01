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
import com.Entity.Tax;
import com.Entity.Customer;
import com.Entity.Invoice;
import com.Entity.InvoiceHasItem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class InvoiceJpaController implements Serializable {

    public InvoiceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Invoice invoice) {
        if (invoice.getInvoiceHasItemList() == null) {
            invoice.setInvoiceHasItemList(new ArrayList<InvoiceHasItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = invoice.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                invoice.setUserid(userid);
            }
            Tax taxid = invoice.getTaxid();
            if (taxid != null) {
                taxid = em.getReference(taxid.getClass(), taxid.getId());
                invoice.setTaxid(taxid);
            }
            Customer customerid = invoice.getCustomerid();
            if (customerid != null) {
                customerid = em.getReference(customerid.getClass(), customerid.getId());
                invoice.setCustomerid(customerid);
            }
            List<InvoiceHasItem> attachedInvoiceHasItemList = new ArrayList<InvoiceHasItem>();
            for (InvoiceHasItem invoiceHasItemListInvoiceHasItemToAttach : invoice.getInvoiceHasItemList()) {
                invoiceHasItemListInvoiceHasItemToAttach = em.getReference(invoiceHasItemListInvoiceHasItemToAttach.getClass(), invoiceHasItemListInvoiceHasItemToAttach.getId());
                attachedInvoiceHasItemList.add(invoiceHasItemListInvoiceHasItemToAttach);
            }
            invoice.setInvoiceHasItemList(attachedInvoiceHasItemList);
            em.persist(invoice);
            if (userid != null) {
                userid.getInvoiceList().add(invoice);
                userid = em.merge(userid);
            }
            if (taxid != null) {
                taxid.getInvoiceList().add(invoice);
                taxid = em.merge(taxid);
            }
            if (customerid != null) {
                customerid.getInvoiceList().add(invoice);
                customerid = em.merge(customerid);
            }
            for (InvoiceHasItem invoiceHasItemListInvoiceHasItem : invoice.getInvoiceHasItemList()) {
                Invoice oldInvoiceidOfInvoiceHasItemListInvoiceHasItem = invoiceHasItemListInvoiceHasItem.getInvoiceid();
                invoiceHasItemListInvoiceHasItem.setInvoiceid(invoice);
                invoiceHasItemListInvoiceHasItem = em.merge(invoiceHasItemListInvoiceHasItem);
                if (oldInvoiceidOfInvoiceHasItemListInvoiceHasItem != null) {
                    oldInvoiceidOfInvoiceHasItemListInvoiceHasItem.getInvoiceHasItemList().remove(invoiceHasItemListInvoiceHasItem);
                    oldInvoiceidOfInvoiceHasItemListInvoiceHasItem = em.merge(oldInvoiceidOfInvoiceHasItemListInvoiceHasItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Invoice invoice) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Invoice persistentInvoice = em.find(Invoice.class, invoice.getId());
            User useridOld = persistentInvoice.getUserid();
            User useridNew = invoice.getUserid();
            Tax taxidOld = persistentInvoice.getTaxid();
            Tax taxidNew = invoice.getTaxid();
            Customer customeridOld = persistentInvoice.getCustomerid();
            Customer customeridNew = invoice.getCustomerid();
            List<InvoiceHasItem> invoiceHasItemListOld = persistentInvoice.getInvoiceHasItemList();
            List<InvoiceHasItem> invoiceHasItemListNew = invoice.getInvoiceHasItemList();
            List<String> illegalOrphanMessages = null;
            for (InvoiceHasItem invoiceHasItemListOldInvoiceHasItem : invoiceHasItemListOld) {
                if (!invoiceHasItemListNew.contains(invoiceHasItemListOldInvoiceHasItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InvoiceHasItem " + invoiceHasItemListOldInvoiceHasItem + " since its invoiceid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                invoice.setUserid(useridNew);
            }
            if (taxidNew != null) {
                taxidNew = em.getReference(taxidNew.getClass(), taxidNew.getId());
                invoice.setTaxid(taxidNew);
            }
            if (customeridNew != null) {
                customeridNew = em.getReference(customeridNew.getClass(), customeridNew.getId());
                invoice.setCustomerid(customeridNew);
            }
            List<InvoiceHasItem> attachedInvoiceHasItemListNew = new ArrayList<InvoiceHasItem>();
            for (InvoiceHasItem invoiceHasItemListNewInvoiceHasItemToAttach : invoiceHasItemListNew) {
                invoiceHasItemListNewInvoiceHasItemToAttach = em.getReference(invoiceHasItemListNewInvoiceHasItemToAttach.getClass(), invoiceHasItemListNewInvoiceHasItemToAttach.getId());
                attachedInvoiceHasItemListNew.add(invoiceHasItemListNewInvoiceHasItemToAttach);
            }
            invoiceHasItemListNew = attachedInvoiceHasItemListNew;
            invoice.setInvoiceHasItemList(invoiceHasItemListNew);
            invoice = em.merge(invoice);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getInvoiceList().remove(invoice);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getInvoiceList().add(invoice);
                useridNew = em.merge(useridNew);
            }
            if (taxidOld != null && !taxidOld.equals(taxidNew)) {
                taxidOld.getInvoiceList().remove(invoice);
                taxidOld = em.merge(taxidOld);
            }
            if (taxidNew != null && !taxidNew.equals(taxidOld)) {
                taxidNew.getInvoiceList().add(invoice);
                taxidNew = em.merge(taxidNew);
            }
            if (customeridOld != null && !customeridOld.equals(customeridNew)) {
                customeridOld.getInvoiceList().remove(invoice);
                customeridOld = em.merge(customeridOld);
            }
            if (customeridNew != null && !customeridNew.equals(customeridOld)) {
                customeridNew.getInvoiceList().add(invoice);
                customeridNew = em.merge(customeridNew);
            }
            for (InvoiceHasItem invoiceHasItemListNewInvoiceHasItem : invoiceHasItemListNew) {
                if (!invoiceHasItemListOld.contains(invoiceHasItemListNewInvoiceHasItem)) {
                    Invoice oldInvoiceidOfInvoiceHasItemListNewInvoiceHasItem = invoiceHasItemListNewInvoiceHasItem.getInvoiceid();
                    invoiceHasItemListNewInvoiceHasItem.setInvoiceid(invoice);
                    invoiceHasItemListNewInvoiceHasItem = em.merge(invoiceHasItemListNewInvoiceHasItem);
                    if (oldInvoiceidOfInvoiceHasItemListNewInvoiceHasItem != null && !oldInvoiceidOfInvoiceHasItemListNewInvoiceHasItem.equals(invoice)) {
                        oldInvoiceidOfInvoiceHasItemListNewInvoiceHasItem.getInvoiceHasItemList().remove(invoiceHasItemListNewInvoiceHasItem);
                        oldInvoiceidOfInvoiceHasItemListNewInvoiceHasItem = em.merge(oldInvoiceidOfInvoiceHasItemListNewInvoiceHasItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = invoice.getId();
                if (findInvoice(id) == null) {
                    throw new NonexistentEntityException("The invoice with id " + id + " no longer exists.");
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
            Invoice invoice;
            try {
                invoice = em.getReference(Invoice.class, id);
                invoice.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The invoice with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<InvoiceHasItem> invoiceHasItemListOrphanCheck = invoice.getInvoiceHasItemList();
            for (InvoiceHasItem invoiceHasItemListOrphanCheckInvoiceHasItem : invoiceHasItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Invoice (" + invoice + ") cannot be destroyed since the InvoiceHasItem " + invoiceHasItemListOrphanCheckInvoiceHasItem + " in its invoiceHasItemList field has a non-nullable invoiceid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = invoice.getUserid();
            if (userid != null) {
                userid.getInvoiceList().remove(invoice);
                userid = em.merge(userid);
            }
            Tax taxid = invoice.getTaxid();
            if (taxid != null) {
                taxid.getInvoiceList().remove(invoice);
                taxid = em.merge(taxid);
            }
            Customer customerid = invoice.getCustomerid();
            if (customerid != null) {
                customerid.getInvoiceList().remove(invoice);
                customerid = em.merge(customerid);
            }
            em.remove(invoice);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Invoice> findInvoiceEntities() {
        return findInvoiceEntities(true, -1, -1);
    }

    public List<Invoice> findInvoiceEntities(int maxResults, int firstResult) {
        return findInvoiceEntities(false, maxResults, firstResult);
    }

    private List<Invoice> findInvoiceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Invoice.class));
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

    public Invoice findInvoice(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Invoice.class, id);
        } finally {
            em.close();
        }
    }

    public int getInvoiceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Invoice> rt = cq.from(Invoice.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
