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
import com.Entity.User;
import com.Entity.UserHasAction;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class UserHasActionJpaController implements Serializable {

    public UserHasActionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserHasAction userHasAction) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = userHasAction.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                userHasAction.setUserid(userid);
            }
            em.persist(userHasAction);
            if (userid != null) {
                userid.getUserHasActionList().add(userHasAction);
                userid = em.merge(userid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserHasAction userHasAction) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserHasAction persistentUserHasAction = em.find(UserHasAction.class, userHasAction.getId());
            User useridOld = persistentUserHasAction.getUserid();
            User useridNew = userHasAction.getUserid();
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                userHasAction.setUserid(useridNew);
            }
            userHasAction = em.merge(userHasAction);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getUserHasActionList().remove(userHasAction);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getUserHasActionList().add(userHasAction);
                useridNew = em.merge(useridNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userHasAction.getId();
                if (findUserHasAction(id) == null) {
                    throw new NonexistentEntityException("The userHasAction with id " + id + " no longer exists.");
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
            UserHasAction userHasAction;
            try {
                userHasAction = em.getReference(UserHasAction.class, id);
                userHasAction.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userHasAction with id " + id + " no longer exists.", enfe);
            }
            User userid = userHasAction.getUserid();
            if (userid != null) {
                userid.getUserHasActionList().remove(userHasAction);
                userid = em.merge(userid);
            }
            em.remove(userHasAction);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserHasAction> findUserHasActionEntities() {
        return findUserHasActionEntities(true, -1, -1);
    }

    public List<UserHasAction> findUserHasActionEntities(int maxResults, int firstResult) {
        return findUserHasActionEntities(false, maxResults, firstResult);
    }

    private List<UserHasAction> findUserHasActionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserHasAction.class));
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

    public UserHasAction findUserHasAction(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserHasAction.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserHasActionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserHasAction> rt = cq.from(UserHasAction.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
