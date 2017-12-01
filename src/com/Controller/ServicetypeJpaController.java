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
import com.Entity.Serviceinvoice;
import com.Entity.Servicetype;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class ServicetypeJpaController implements Serializable {

    public ServicetypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Servicetype servicetype) {
        if (servicetype.getServiceinvoiceList() == null) {
            servicetype.setServiceinvoiceList(new ArrayList<Serviceinvoice>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Serviceinvoice> attachedServiceinvoiceList = new ArrayList<Serviceinvoice>();
            for (Serviceinvoice serviceinvoiceListServiceinvoiceToAttach : servicetype.getServiceinvoiceList()) {
                serviceinvoiceListServiceinvoiceToAttach = em.getReference(serviceinvoiceListServiceinvoiceToAttach.getClass(), serviceinvoiceListServiceinvoiceToAttach.getId());
                attachedServiceinvoiceList.add(serviceinvoiceListServiceinvoiceToAttach);
            }
            servicetype.setServiceinvoiceList(attachedServiceinvoiceList);
            em.persist(servicetype);
            for (Serviceinvoice serviceinvoiceListServiceinvoice : servicetype.getServiceinvoiceList()) {
                Servicetype oldServiceTypeidOfServiceinvoiceListServiceinvoice = serviceinvoiceListServiceinvoice.getServiceTypeid();
                serviceinvoiceListServiceinvoice.setServiceTypeid(servicetype);
                serviceinvoiceListServiceinvoice = em.merge(serviceinvoiceListServiceinvoice);
                if (oldServiceTypeidOfServiceinvoiceListServiceinvoice != null) {
                    oldServiceTypeidOfServiceinvoiceListServiceinvoice.getServiceinvoiceList().remove(serviceinvoiceListServiceinvoice);
                    oldServiceTypeidOfServiceinvoiceListServiceinvoice = em.merge(oldServiceTypeidOfServiceinvoiceListServiceinvoice);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Servicetype servicetype) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servicetype persistentServicetype = em.find(Servicetype.class, servicetype.getId());
            List<Serviceinvoice> serviceinvoiceListOld = persistentServicetype.getServiceinvoiceList();
            List<Serviceinvoice> serviceinvoiceListNew = servicetype.getServiceinvoiceList();
            List<String> illegalOrphanMessages = null;
            for (Serviceinvoice serviceinvoiceListOldServiceinvoice : serviceinvoiceListOld) {
                if (!serviceinvoiceListNew.contains(serviceinvoiceListOldServiceinvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Serviceinvoice " + serviceinvoiceListOldServiceinvoice + " since its serviceTypeid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Serviceinvoice> attachedServiceinvoiceListNew = new ArrayList<Serviceinvoice>();
            for (Serviceinvoice serviceinvoiceListNewServiceinvoiceToAttach : serviceinvoiceListNew) {
                serviceinvoiceListNewServiceinvoiceToAttach = em.getReference(serviceinvoiceListNewServiceinvoiceToAttach.getClass(), serviceinvoiceListNewServiceinvoiceToAttach.getId());
                attachedServiceinvoiceListNew.add(serviceinvoiceListNewServiceinvoiceToAttach);
            }
            serviceinvoiceListNew = attachedServiceinvoiceListNew;
            servicetype.setServiceinvoiceList(serviceinvoiceListNew);
            servicetype = em.merge(servicetype);
            for (Serviceinvoice serviceinvoiceListNewServiceinvoice : serviceinvoiceListNew) {
                if (!serviceinvoiceListOld.contains(serviceinvoiceListNewServiceinvoice)) {
                    Servicetype oldServiceTypeidOfServiceinvoiceListNewServiceinvoice = serviceinvoiceListNewServiceinvoice.getServiceTypeid();
                    serviceinvoiceListNewServiceinvoice.setServiceTypeid(servicetype);
                    serviceinvoiceListNewServiceinvoice = em.merge(serviceinvoiceListNewServiceinvoice);
                    if (oldServiceTypeidOfServiceinvoiceListNewServiceinvoice != null && !oldServiceTypeidOfServiceinvoiceListNewServiceinvoice.equals(servicetype)) {
                        oldServiceTypeidOfServiceinvoiceListNewServiceinvoice.getServiceinvoiceList().remove(serviceinvoiceListNewServiceinvoice);
                        oldServiceTypeidOfServiceinvoiceListNewServiceinvoice = em.merge(oldServiceTypeidOfServiceinvoiceListNewServiceinvoice);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servicetype.getId();
                if (findServicetype(id) == null) {
                    throw new NonexistentEntityException("The servicetype with id " + id + " no longer exists.");
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
            Servicetype servicetype;
            try {
                servicetype = em.getReference(Servicetype.class, id);
                servicetype.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicetype with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Serviceinvoice> serviceinvoiceListOrphanCheck = servicetype.getServiceinvoiceList();
            for (Serviceinvoice serviceinvoiceListOrphanCheckServiceinvoice : serviceinvoiceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Servicetype (" + servicetype + ") cannot be destroyed since the Serviceinvoice " + serviceinvoiceListOrphanCheckServiceinvoice + " in its serviceinvoiceList field has a non-nullable serviceTypeid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(servicetype);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Servicetype> findServicetypeEntities() {
        return findServicetypeEntities(true, -1, -1);
    }

    public List<Servicetype> findServicetypeEntities(int maxResults, int firstResult) {
        return findServicetypeEntities(false, maxResults, firstResult);
    }

    private List<Servicetype> findServicetypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicetype.class));
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

    public Servicetype findServicetype(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servicetype.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicetypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servicetype> rt = cq.from(Servicetype.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
