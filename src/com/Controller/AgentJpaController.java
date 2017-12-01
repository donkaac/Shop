/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.NonexistentEntityException;
import com.Entity.Agent;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.User;
import com.Entity.Supplier;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class AgentJpaController implements Serializable {

    public AgentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Agent agent) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = agent.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                agent.setUserid(userid);
            }
            Supplier supplierid = agent.getSupplierid();
            if (supplierid != null) {
                supplierid = em.getReference(supplierid.getClass(), supplierid.getId());
                agent.setSupplierid(supplierid);
            }
            em.persist(agent);
            if (userid != null) {
                userid.getAgentList().add(agent);
                userid = em.merge(userid);
            }
            if (supplierid != null) {
                supplierid.getAgentList().add(agent);
                supplierid = em.merge(supplierid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Agent agent) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agent persistentAgent = em.find(Agent.class, agent.getId());
            User useridOld = persistentAgent.getUserid();
            User useridNew = agent.getUserid();
            Supplier supplieridOld = persistentAgent.getSupplierid();
            Supplier supplieridNew = agent.getSupplierid();
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                agent.setUserid(useridNew);
            }
            if (supplieridNew != null) {
                supplieridNew = em.getReference(supplieridNew.getClass(), supplieridNew.getId());
                agent.setSupplierid(supplieridNew);
            }
            agent = em.merge(agent);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getAgentList().remove(agent);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getAgentList().add(agent);
                useridNew = em.merge(useridNew);
            }
            if (supplieridOld != null && !supplieridOld.equals(supplieridNew)) {
                supplieridOld.getAgentList().remove(agent);
                supplieridOld = em.merge(supplieridOld);
            }
            if (supplieridNew != null && !supplieridNew.equals(supplieridOld)) {
                supplieridNew.getAgentList().add(agent);
                supplieridNew = em.merge(supplieridNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = agent.getId();
                if (findAgent(id) == null) {
                    throw new NonexistentEntityException("The agent with id " + id + " no longer exists.");
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
            Agent agent;
            try {
                agent = em.getReference(Agent.class, id);
                agent.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agent with id " + id + " no longer exists.", enfe);
            }
            User userid = agent.getUserid();
            if (userid != null) {
                userid.getAgentList().remove(agent);
                userid = em.merge(userid);
            }
            Supplier supplierid = agent.getSupplierid();
            if (supplierid != null) {
                supplierid.getAgentList().remove(agent);
                supplierid = em.merge(supplierid);
            }
            em.remove(agent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Agent> findAgentEntities() {
        return findAgentEntities(true, -1, -1);
    }

    public List<Agent> findAgentEntities(int maxResults, int firstResult) {
        return findAgentEntities(false, maxResults, firstResult);
    }

    private List<Agent> findAgentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agent.class));
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

    public Agent findAgent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agent.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Agent> rt = cq.from(Agent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
