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
import com.Entity.Servicetype;
import com.Entity.Service;
import com.Entity.Serviceinvoice;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class ServiceinvoiceJpaController implements Serializable {

    public ServiceinvoiceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Serviceinvoice serviceinvoice) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servicetype serviceTypeid = serviceinvoice.getServiceTypeid();
            if (serviceTypeid != null) {
                serviceTypeid = em.getReference(serviceTypeid.getClass(), serviceTypeid.getId());
                serviceinvoice.setServiceTypeid(serviceTypeid);
            }
            Service serviceid = serviceinvoice.getServiceid();
            if (serviceid != null) {
                serviceid = em.getReference(serviceid.getClass(), serviceid.getId());
                serviceinvoice.setServiceid(serviceid);
            }
            em.persist(serviceinvoice);
            if (serviceTypeid != null) {
                serviceTypeid.getServiceinvoiceList().add(serviceinvoice);
                serviceTypeid = em.merge(serviceTypeid);
            }
            if (serviceid != null) {
                serviceid.getServiceinvoiceList().add(serviceinvoice);
                serviceid = em.merge(serviceid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Serviceinvoice serviceinvoice) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Serviceinvoice persistentServiceinvoice = em.find(Serviceinvoice.class, serviceinvoice.getId());
            Servicetype serviceTypeidOld = persistentServiceinvoice.getServiceTypeid();
            Servicetype serviceTypeidNew = serviceinvoice.getServiceTypeid();
            Service serviceidOld = persistentServiceinvoice.getServiceid();
            Service serviceidNew = serviceinvoice.getServiceid();
            if (serviceTypeidNew != null) {
                serviceTypeidNew = em.getReference(serviceTypeidNew.getClass(), serviceTypeidNew.getId());
                serviceinvoice.setServiceTypeid(serviceTypeidNew);
            }
            if (serviceidNew != null) {
                serviceidNew = em.getReference(serviceidNew.getClass(), serviceidNew.getId());
                serviceinvoice.setServiceid(serviceidNew);
            }
            serviceinvoice = em.merge(serviceinvoice);
            if (serviceTypeidOld != null && !serviceTypeidOld.equals(serviceTypeidNew)) {
                serviceTypeidOld.getServiceinvoiceList().remove(serviceinvoice);
                serviceTypeidOld = em.merge(serviceTypeidOld);
            }
            if (serviceTypeidNew != null && !serviceTypeidNew.equals(serviceTypeidOld)) {
                serviceTypeidNew.getServiceinvoiceList().add(serviceinvoice);
                serviceTypeidNew = em.merge(serviceTypeidNew);
            }
            if (serviceidOld != null && !serviceidOld.equals(serviceidNew)) {
                serviceidOld.getServiceinvoiceList().remove(serviceinvoice);
                serviceidOld = em.merge(serviceidOld);
            }
            if (serviceidNew != null && !serviceidNew.equals(serviceidOld)) {
                serviceidNew.getServiceinvoiceList().add(serviceinvoice);
                serviceidNew = em.merge(serviceidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = serviceinvoice.getId();
                if (findServiceinvoice(id) == null) {
                    throw new NonexistentEntityException("The serviceinvoice with id " + id + " no longer exists.");
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
            Serviceinvoice serviceinvoice;
            try {
                serviceinvoice = em.getReference(Serviceinvoice.class, id);
                serviceinvoice.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The serviceinvoice with id " + id + " no longer exists.", enfe);
            }
            Servicetype serviceTypeid = serviceinvoice.getServiceTypeid();
            if (serviceTypeid != null) {
                serviceTypeid.getServiceinvoiceList().remove(serviceinvoice);
                serviceTypeid = em.merge(serviceTypeid);
            }
            Service serviceid = serviceinvoice.getServiceid();
            if (serviceid != null) {
                serviceid.getServiceinvoiceList().remove(serviceinvoice);
                serviceid = em.merge(serviceid);
            }
            em.remove(serviceinvoice);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Serviceinvoice> findServiceinvoiceEntities() {
        return findServiceinvoiceEntities(true, -1, -1);
    }

    public List<Serviceinvoice> findServiceinvoiceEntities(int maxResults, int firstResult) {
        return findServiceinvoiceEntities(false, maxResults, firstResult);
    }

    private List<Serviceinvoice> findServiceinvoiceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Serviceinvoice.class));
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

    public Serviceinvoice findServiceinvoice(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Serviceinvoice.class, id);
        } finally {
            em.close();
        }
    }

    public int getServiceinvoiceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Serviceinvoice> rt = cq.from(Serviceinvoice.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
