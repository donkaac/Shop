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
import com.Entity.Customer;
import com.Entity.Service;
import com.Entity.Serviceinvoice;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class ServiceJpaController implements Serializable {

    public ServiceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Service service) {
        if (service.getServiceinvoiceList() == null) {
            service.setServiceinvoiceList(new ArrayList<Serviceinvoice>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer customerid = service.getCustomerid();
            if (customerid != null) {
                customerid = em.getReference(customerid.getClass(), customerid.getId());
                service.setCustomerid(customerid);
            }
            List<Serviceinvoice> attachedServiceinvoiceList = new ArrayList<Serviceinvoice>();
            for (Serviceinvoice serviceinvoiceListServiceinvoiceToAttach : service.getServiceinvoiceList()) {
                serviceinvoiceListServiceinvoiceToAttach = em.getReference(serviceinvoiceListServiceinvoiceToAttach.getClass(), serviceinvoiceListServiceinvoiceToAttach.getId());
                attachedServiceinvoiceList.add(serviceinvoiceListServiceinvoiceToAttach);
            }
            service.setServiceinvoiceList(attachedServiceinvoiceList);
            em.persist(service);
            if (customerid != null) {
                customerid.getServiceList().add(service);
                customerid = em.merge(customerid);
            }
            for (Serviceinvoice serviceinvoiceListServiceinvoice : service.getServiceinvoiceList()) {
                Service oldServiceidOfServiceinvoiceListServiceinvoice = serviceinvoiceListServiceinvoice.getServiceid();
                serviceinvoiceListServiceinvoice.setServiceid(service);
                serviceinvoiceListServiceinvoice = em.merge(serviceinvoiceListServiceinvoice);
                if (oldServiceidOfServiceinvoiceListServiceinvoice != null) {
                    oldServiceidOfServiceinvoiceListServiceinvoice.getServiceinvoiceList().remove(serviceinvoiceListServiceinvoice);
                    oldServiceidOfServiceinvoiceListServiceinvoice = em.merge(oldServiceidOfServiceinvoiceListServiceinvoice);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Service service) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Service persistentService = em.find(Service.class, service.getId());
            Customer customeridOld = persistentService.getCustomerid();
            Customer customeridNew = service.getCustomerid();
            List<Serviceinvoice> serviceinvoiceListOld = persistentService.getServiceinvoiceList();
            List<Serviceinvoice> serviceinvoiceListNew = service.getServiceinvoiceList();
            List<String> illegalOrphanMessages = null;
            for (Serviceinvoice serviceinvoiceListOldServiceinvoice : serviceinvoiceListOld) {
                if (!serviceinvoiceListNew.contains(serviceinvoiceListOldServiceinvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Serviceinvoice " + serviceinvoiceListOldServiceinvoice + " since its serviceid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (customeridNew != null) {
                customeridNew = em.getReference(customeridNew.getClass(), customeridNew.getId());
                service.setCustomerid(customeridNew);
            }
            List<Serviceinvoice> attachedServiceinvoiceListNew = new ArrayList<Serviceinvoice>();
            for (Serviceinvoice serviceinvoiceListNewServiceinvoiceToAttach : serviceinvoiceListNew) {
                serviceinvoiceListNewServiceinvoiceToAttach = em.getReference(serviceinvoiceListNewServiceinvoiceToAttach.getClass(), serviceinvoiceListNewServiceinvoiceToAttach.getId());
                attachedServiceinvoiceListNew.add(serviceinvoiceListNewServiceinvoiceToAttach);
            }
            serviceinvoiceListNew = attachedServiceinvoiceListNew;
            service.setServiceinvoiceList(serviceinvoiceListNew);
            service = em.merge(service);
            if (customeridOld != null && !customeridOld.equals(customeridNew)) {
                customeridOld.getServiceList().remove(service);
                customeridOld = em.merge(customeridOld);
            }
            if (customeridNew != null && !customeridNew.equals(customeridOld)) {
                customeridNew.getServiceList().add(service);
                customeridNew = em.merge(customeridNew);
            }
            for (Serviceinvoice serviceinvoiceListNewServiceinvoice : serviceinvoiceListNew) {
                if (!serviceinvoiceListOld.contains(serviceinvoiceListNewServiceinvoice)) {
                    Service oldServiceidOfServiceinvoiceListNewServiceinvoice = serviceinvoiceListNewServiceinvoice.getServiceid();
                    serviceinvoiceListNewServiceinvoice.setServiceid(service);
                    serviceinvoiceListNewServiceinvoice = em.merge(serviceinvoiceListNewServiceinvoice);
                    if (oldServiceidOfServiceinvoiceListNewServiceinvoice != null && !oldServiceidOfServiceinvoiceListNewServiceinvoice.equals(service)) {
                        oldServiceidOfServiceinvoiceListNewServiceinvoice.getServiceinvoiceList().remove(serviceinvoiceListNewServiceinvoice);
                        oldServiceidOfServiceinvoiceListNewServiceinvoice = em.merge(oldServiceidOfServiceinvoiceListNewServiceinvoice);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = service.getId();
                if (findService(id) == null) {
                    throw new NonexistentEntityException("The service with id " + id + " no longer exists.");
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
            Service service;
            try {
                service = em.getReference(Service.class, id);
                service.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The service with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Serviceinvoice> serviceinvoiceListOrphanCheck = service.getServiceinvoiceList();
            for (Serviceinvoice serviceinvoiceListOrphanCheckServiceinvoice : serviceinvoiceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Service (" + service + ") cannot be destroyed since the Serviceinvoice " + serviceinvoiceListOrphanCheckServiceinvoice + " in its serviceinvoiceList field has a non-nullable serviceid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Customer customerid = service.getCustomerid();
            if (customerid != null) {
                customerid.getServiceList().remove(service);
                customerid = em.merge(customerid);
            }
            em.remove(service);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Service> findServiceEntities() {
        return findServiceEntities(true, -1, -1);
    }

    public List<Service> findServiceEntities(int maxResults, int firstResult) {
        return findServiceEntities(false, maxResults, firstResult);
    }

    private List<Service> findServiceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Service.class));
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

    public Service findService(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Service.class, id);
        } finally {
            em.close();
        }
    }

    public int getServiceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Service> rt = cq.from(Service.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
