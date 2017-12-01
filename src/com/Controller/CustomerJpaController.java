/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.IllegalOrphanException;
import com.Controller.exceptions.NonexistentEntityException;
import com.Entity.Customer;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.User;
import com.Entity.Customertype;
import com.Entity.Invoice;
import java.util.ArrayList;
import java.util.List;
import com.Entity.Service;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class CustomerJpaController implements Serializable {

    public CustomerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customer customer) {
        if (customer.getInvoiceList() == null) {
            customer.setInvoiceList(new ArrayList<Invoice>());
        }
        if (customer.getServiceList() == null) {
            customer.setServiceList(new ArrayList<Service>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = customer.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                customer.setUserid(userid);
            }
            Customertype customerTypeid = customer.getCustomerTypeid();
            if (customerTypeid != null) {
                customerTypeid = em.getReference(customerTypeid.getClass(), customerTypeid.getId());
                customer.setCustomerTypeid(customerTypeid);
            }
            List<Invoice> attachedInvoiceList = new ArrayList<Invoice>();
            for (Invoice invoiceListInvoiceToAttach : customer.getInvoiceList()) {
                invoiceListInvoiceToAttach = em.getReference(invoiceListInvoiceToAttach.getClass(), invoiceListInvoiceToAttach.getId());
                attachedInvoiceList.add(invoiceListInvoiceToAttach);
            }
            customer.setInvoiceList(attachedInvoiceList);
            List<Service> attachedServiceList = new ArrayList<Service>();
            for (Service serviceListServiceToAttach : customer.getServiceList()) {
                serviceListServiceToAttach = em.getReference(serviceListServiceToAttach.getClass(), serviceListServiceToAttach.getId());
                attachedServiceList.add(serviceListServiceToAttach);
            }
            customer.setServiceList(attachedServiceList);
            em.persist(customer);
            if (userid != null) {
                userid.getCustomerList().add(customer);
                userid = em.merge(userid);
            }
            if (customerTypeid != null) {
                customerTypeid.getCustomerList().add(customer);
                customerTypeid = em.merge(customerTypeid);
            }
            for (Invoice invoiceListInvoice : customer.getInvoiceList()) {
                Customer oldCustomeridOfInvoiceListInvoice = invoiceListInvoice.getCustomerid();
                invoiceListInvoice.setCustomerid(customer);
                invoiceListInvoice = em.merge(invoiceListInvoice);
                if (oldCustomeridOfInvoiceListInvoice != null) {
                    oldCustomeridOfInvoiceListInvoice.getInvoiceList().remove(invoiceListInvoice);
                    oldCustomeridOfInvoiceListInvoice = em.merge(oldCustomeridOfInvoiceListInvoice);
                }
            }
            for (Service serviceListService : customer.getServiceList()) {
                Customer oldCustomeridOfServiceListService = serviceListService.getCustomerid();
                serviceListService.setCustomerid(customer);
                serviceListService = em.merge(serviceListService);
                if (oldCustomeridOfServiceListService != null) {
                    oldCustomeridOfServiceListService.getServiceList().remove(serviceListService);
                    oldCustomeridOfServiceListService = em.merge(oldCustomeridOfServiceListService);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customer customer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer persistentCustomer = em.find(Customer.class, customer.getId());
            User useridOld = persistentCustomer.getUserid();
            User useridNew = customer.getUserid();
            Customertype customerTypeidOld = persistentCustomer.getCustomerTypeid();
            Customertype customerTypeidNew = customer.getCustomerTypeid();
            List<Invoice> invoiceListOld = persistentCustomer.getInvoiceList();
            List<Invoice> invoiceListNew = customer.getInvoiceList();
            List<Service> serviceListOld = persistentCustomer.getServiceList();
            List<Service> serviceListNew = customer.getServiceList();
            List<String> illegalOrphanMessages = null;
            for (Invoice invoiceListOldInvoice : invoiceListOld) {
                if (!invoiceListNew.contains(invoiceListOldInvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Invoice " + invoiceListOldInvoice + " since its customerid field is not nullable.");
                }
            }
            for (Service serviceListOldService : serviceListOld) {
                if (!serviceListNew.contains(serviceListOldService)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Service " + serviceListOldService + " since its customerid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                customer.setUserid(useridNew);
            }
            if (customerTypeidNew != null) {
                customerTypeidNew = em.getReference(customerTypeidNew.getClass(), customerTypeidNew.getId());
                customer.setCustomerTypeid(customerTypeidNew);
            }
            List<Invoice> attachedInvoiceListNew = new ArrayList<Invoice>();
            for (Invoice invoiceListNewInvoiceToAttach : invoiceListNew) {
                invoiceListNewInvoiceToAttach = em.getReference(invoiceListNewInvoiceToAttach.getClass(), invoiceListNewInvoiceToAttach.getId());
                attachedInvoiceListNew.add(invoiceListNewInvoiceToAttach);
            }
            invoiceListNew = attachedInvoiceListNew;
            customer.setInvoiceList(invoiceListNew);
            List<Service> attachedServiceListNew = new ArrayList<Service>();
            for (Service serviceListNewServiceToAttach : serviceListNew) {
                serviceListNewServiceToAttach = em.getReference(serviceListNewServiceToAttach.getClass(), serviceListNewServiceToAttach.getId());
                attachedServiceListNew.add(serviceListNewServiceToAttach);
            }
            serviceListNew = attachedServiceListNew;
            customer.setServiceList(serviceListNew);
            customer = em.merge(customer);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getCustomerList().remove(customer);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getCustomerList().add(customer);
                useridNew = em.merge(useridNew);
            }
            if (customerTypeidOld != null && !customerTypeidOld.equals(customerTypeidNew)) {
                customerTypeidOld.getCustomerList().remove(customer);
                customerTypeidOld = em.merge(customerTypeidOld);
            }
            if (customerTypeidNew != null && !customerTypeidNew.equals(customerTypeidOld)) {
                customerTypeidNew.getCustomerList().add(customer);
                customerTypeidNew = em.merge(customerTypeidNew);
            }
            for (Invoice invoiceListNewInvoice : invoiceListNew) {
                if (!invoiceListOld.contains(invoiceListNewInvoice)) {
                    Customer oldCustomeridOfInvoiceListNewInvoice = invoiceListNewInvoice.getCustomerid();
                    invoiceListNewInvoice.setCustomerid(customer);
                    invoiceListNewInvoice = em.merge(invoiceListNewInvoice);
                    if (oldCustomeridOfInvoiceListNewInvoice != null && !oldCustomeridOfInvoiceListNewInvoice.equals(customer)) {
                        oldCustomeridOfInvoiceListNewInvoice.getInvoiceList().remove(invoiceListNewInvoice);
                        oldCustomeridOfInvoiceListNewInvoice = em.merge(oldCustomeridOfInvoiceListNewInvoice);
                    }
                }
            }
            for (Service serviceListNewService : serviceListNew) {
                if (!serviceListOld.contains(serviceListNewService)) {
                    Customer oldCustomeridOfServiceListNewService = serviceListNewService.getCustomerid();
                    serviceListNewService.setCustomerid(customer);
                    serviceListNewService = em.merge(serviceListNewService);
                    if (oldCustomeridOfServiceListNewService != null && !oldCustomeridOfServiceListNewService.equals(customer)) {
                        oldCustomeridOfServiceListNewService.getServiceList().remove(serviceListNewService);
                        oldCustomeridOfServiceListNewService = em.merge(oldCustomeridOfServiceListNewService);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = customer.getId();
                if (findCustomer(id) == null) {
                    throw new NonexistentEntityException("The customer with id " + id + " no longer exists.");
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
            Customer customer;
            try {
                customer = em.getReference(Customer.class, id);
                customer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Invoice> invoiceListOrphanCheck = customer.getInvoiceList();
            for (Invoice invoiceListOrphanCheckInvoice : invoiceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Customer (" + customer + ") cannot be destroyed since the Invoice " + invoiceListOrphanCheckInvoice + " in its invoiceList field has a non-nullable customerid field.");
            }
            List<Service> serviceListOrphanCheck = customer.getServiceList();
            for (Service serviceListOrphanCheckService : serviceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Customer (" + customer + ") cannot be destroyed since the Service " + serviceListOrphanCheckService + " in its serviceList field has a non-nullable customerid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = customer.getUserid();
            if (userid != null) {
                userid.getCustomerList().remove(customer);
                userid = em.merge(userid);
            }
            Customertype customerTypeid = customer.getCustomerTypeid();
            if (customerTypeid != null) {
                customerTypeid.getCustomerList().remove(customer);
                customerTypeid = em.merge(customerTypeid);
            }
            em.remove(customer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customer> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Customer> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Customer> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customer.class));
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

    public Customer findCustomer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customer> rt = cq.from(Customer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
