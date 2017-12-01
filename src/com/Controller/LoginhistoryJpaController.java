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
import com.Entity.Login;
import com.Entity.Loginhistory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class LoginhistoryJpaController implements Serializable {

    public LoginhistoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Loginhistory loginhistory) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Login loginid = loginhistory.getLoginid();
            if (loginid != null) {
                loginid = em.getReference(loginid.getClass(), loginid.getId());
                loginhistory.setLoginid(loginid);
            }
            em.persist(loginhistory);
            if (loginid != null) {
                loginid.getLoginhistoryList().add(loginhistory);
                loginid = em.merge(loginid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Loginhistory loginhistory) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Loginhistory persistentLoginhistory = em.find(Loginhistory.class, loginhistory.getId());
            Login loginidOld = persistentLoginhistory.getLoginid();
            Login loginidNew = loginhistory.getLoginid();
            if (loginidNew != null) {
                loginidNew = em.getReference(loginidNew.getClass(), loginidNew.getId());
                loginhistory.setLoginid(loginidNew);
            }
            loginhistory = em.merge(loginhistory);
            if (loginidOld != null && !loginidOld.equals(loginidNew)) {
                loginidOld.getLoginhistoryList().remove(loginhistory);
                loginidOld = em.merge(loginidOld);
            }
            if (loginidNew != null && !loginidNew.equals(loginidOld)) {
                loginidNew.getLoginhistoryList().add(loginhistory);
                loginidNew = em.merge(loginidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = loginhistory.getId();
                if (findLoginhistory(id) == null) {
                    throw new NonexistentEntityException("The loginhistory with id " + id + " no longer exists.");
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
            Loginhistory loginhistory;
            try {
                loginhistory = em.getReference(Loginhistory.class, id);
                loginhistory.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loginhistory with id " + id + " no longer exists.", enfe);
            }
            Login loginid = loginhistory.getLoginid();
            if (loginid != null) {
                loginid.getLoginhistoryList().remove(loginhistory);
                loginid = em.merge(loginid);
            }
            em.remove(loginhistory);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Loginhistory> findLoginhistoryEntities() {
        return findLoginhistoryEntities(true, -1, -1);
    }

    public List<Loginhistory> findLoginhistoryEntities(int maxResults, int firstResult) {
        return findLoginhistoryEntities(false, maxResults, firstResult);
    }

    private List<Loginhistory> findLoginhistoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Loginhistory.class));
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

    public Loginhistory findLoginhistory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Loginhistory.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoginhistoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Loginhistory> rt = cq.from(Loginhistory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
