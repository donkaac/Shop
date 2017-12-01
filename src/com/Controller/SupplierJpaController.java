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
import com.Entity.Grn;
import java.util.ArrayList;
import java.util.List;
import com.Entity.Agent;
import com.Entity.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class SupplierJpaController implements Serializable {

    public SupplierJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Supplier supplier) {
        if (supplier.getGrnList() == null) {
            supplier.setGrnList(new ArrayList<Grn>());
        }
        if (supplier.getAgentList() == null) {
            supplier.setAgentList(new ArrayList<Agent>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = supplier.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                supplier.setUserid(userid);
            }
            List<Grn> attachedGrnList = new ArrayList<Grn>();
            for (Grn grnListGrnToAttach : supplier.getGrnList()) {
                grnListGrnToAttach = em.getReference(grnListGrnToAttach.getClass(), grnListGrnToAttach.getId());
                attachedGrnList.add(grnListGrnToAttach);
            }
            supplier.setGrnList(attachedGrnList);
            List<Agent> attachedAgentList = new ArrayList<Agent>();
            for (Agent agentListAgentToAttach : supplier.getAgentList()) {
                agentListAgentToAttach = em.getReference(agentListAgentToAttach.getClass(), agentListAgentToAttach.getId());
                attachedAgentList.add(agentListAgentToAttach);
            }
            supplier.setAgentList(attachedAgentList);
            em.persist(supplier);
            if (userid != null) {
                userid.getSupplierList().add(supplier);
                userid = em.merge(userid);
            }
            for (Grn grnListGrn : supplier.getGrnList()) {
                Supplier oldSupplieridOfGrnListGrn = grnListGrn.getSupplierid();
                grnListGrn.setSupplierid(supplier);
                grnListGrn = em.merge(grnListGrn);
                if (oldSupplieridOfGrnListGrn != null) {
                    oldSupplieridOfGrnListGrn.getGrnList().remove(grnListGrn);
                    oldSupplieridOfGrnListGrn = em.merge(oldSupplieridOfGrnListGrn);
                }
            }
            for (Agent agentListAgent : supplier.getAgentList()) {
                Supplier oldSupplieridOfAgentListAgent = agentListAgent.getSupplierid();
                agentListAgent.setSupplierid(supplier);
                agentListAgent = em.merge(agentListAgent);
                if (oldSupplieridOfAgentListAgent != null) {
                    oldSupplieridOfAgentListAgent.getAgentList().remove(agentListAgent);
                    oldSupplieridOfAgentListAgent = em.merge(oldSupplieridOfAgentListAgent);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Supplier supplier) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier persistentSupplier = em.find(Supplier.class, supplier.getId());
            User useridOld = persistentSupplier.getUserid();
            User useridNew = supplier.getUserid();
            List<Grn> grnListOld = persistentSupplier.getGrnList();
            List<Grn> grnListNew = supplier.getGrnList();
            List<Agent> agentListOld = persistentSupplier.getAgentList();
            List<Agent> agentListNew = supplier.getAgentList();
            List<String> illegalOrphanMessages = null;
            for (Grn grnListOldGrn : grnListOld) {
                if (!grnListNew.contains(grnListOldGrn)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Grn " + grnListOldGrn + " since its supplierid field is not nullable.");
                }
            }
            for (Agent agentListOldAgent : agentListOld) {
                if (!agentListNew.contains(agentListOldAgent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agent " + agentListOldAgent + " since its supplierid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                supplier.setUserid(useridNew);
            }
            List<Grn> attachedGrnListNew = new ArrayList<Grn>();
            for (Grn grnListNewGrnToAttach : grnListNew) {
                grnListNewGrnToAttach = em.getReference(grnListNewGrnToAttach.getClass(), grnListNewGrnToAttach.getId());
                attachedGrnListNew.add(grnListNewGrnToAttach);
            }
            grnListNew = attachedGrnListNew;
            supplier.setGrnList(grnListNew);
            List<Agent> attachedAgentListNew = new ArrayList<Agent>();
            for (Agent agentListNewAgentToAttach : agentListNew) {
                agentListNewAgentToAttach = em.getReference(agentListNewAgentToAttach.getClass(), agentListNewAgentToAttach.getId());
                attachedAgentListNew.add(agentListNewAgentToAttach);
            }
            agentListNew = attachedAgentListNew;
            supplier.setAgentList(agentListNew);
            supplier = em.merge(supplier);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getSupplierList().remove(supplier);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getSupplierList().add(supplier);
                useridNew = em.merge(useridNew);
            }
            for (Grn grnListNewGrn : grnListNew) {
                if (!grnListOld.contains(grnListNewGrn)) {
                    Supplier oldSupplieridOfGrnListNewGrn = grnListNewGrn.getSupplierid();
                    grnListNewGrn.setSupplierid(supplier);
                    grnListNewGrn = em.merge(grnListNewGrn);
                    if (oldSupplieridOfGrnListNewGrn != null && !oldSupplieridOfGrnListNewGrn.equals(supplier)) {
                        oldSupplieridOfGrnListNewGrn.getGrnList().remove(grnListNewGrn);
                        oldSupplieridOfGrnListNewGrn = em.merge(oldSupplieridOfGrnListNewGrn);
                    }
                }
            }
            for (Agent agentListNewAgent : agentListNew) {
                if (!agentListOld.contains(agentListNewAgent)) {
                    Supplier oldSupplieridOfAgentListNewAgent = agentListNewAgent.getSupplierid();
                    agentListNewAgent.setSupplierid(supplier);
                    agentListNewAgent = em.merge(agentListNewAgent);
                    if (oldSupplieridOfAgentListNewAgent != null && !oldSupplieridOfAgentListNewAgent.equals(supplier)) {
                        oldSupplieridOfAgentListNewAgent.getAgentList().remove(agentListNewAgent);
                        oldSupplieridOfAgentListNewAgent = em.merge(oldSupplieridOfAgentListNewAgent);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = supplier.getId();
                if (findSupplier(id) == null) {
                    throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.");
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
            Supplier supplier;
            try {
                supplier = em.getReference(Supplier.class, id);
                supplier.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Grn> grnListOrphanCheck = supplier.getGrnList();
            for (Grn grnListOrphanCheckGrn : grnListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Supplier (" + supplier + ") cannot be destroyed since the Grn " + grnListOrphanCheckGrn + " in its grnList field has a non-nullable supplierid field.");
            }
            List<Agent> agentListOrphanCheck = supplier.getAgentList();
            for (Agent agentListOrphanCheckAgent : agentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Supplier (" + supplier + ") cannot be destroyed since the Agent " + agentListOrphanCheckAgent + " in its agentList field has a non-nullable supplierid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = supplier.getUserid();
            if (userid != null) {
                userid.getSupplierList().remove(supplier);
                userid = em.merge(userid);
            }
            em.remove(supplier);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Supplier> findSupplierEntities() {
        return findSupplierEntities(true, -1, -1);
    }

    public List<Supplier> findSupplierEntities(int maxResults, int firstResult) {
        return findSupplierEntities(false, maxResults, firstResult);
    }

    private List<Supplier> findSupplierEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Supplier.class));
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

    public Supplier findSupplier(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Supplier.class, id);
        } finally {
            em.close();
        }
    }

    public int getSupplierCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Supplier> rt = cq.from(Supplier.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
