/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.IllegalOrphanException;
import com.Controller.exceptions.NonexistentEntityException;
import com.Controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.User;
import com.Entity.Customertype;
import com.Entity.Discount;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class DiscountJpaController implements Serializable {

    public DiscountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Discount discount) throws PreexistingEntityException, Exception {
        if (discount.getCustomertypeList() == null) {
            discount.setCustomertypeList(new ArrayList<Customertype>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = discount.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                discount.setUserid(userid);
            }
            List<Customertype> attachedCustomertypeList = new ArrayList<Customertype>();
            for (Customertype customertypeListCustomertypeToAttach : discount.getCustomertypeList()) {
                customertypeListCustomertypeToAttach = em.getReference(customertypeListCustomertypeToAttach.getClass(), customertypeListCustomertypeToAttach.getId());
                attachedCustomertypeList.add(customertypeListCustomertypeToAttach);
            }
            discount.setCustomertypeList(attachedCustomertypeList);
            em.persist(discount);
            if (userid != null) {
                userid.getDiscountList().add(discount);
                userid = em.merge(userid);
            }
            for (Customertype customertypeListCustomertype : discount.getCustomertypeList()) {
                Discount oldDiscountidOfCustomertypeListCustomertype = customertypeListCustomertype.getDiscountid();
                customertypeListCustomertype.setDiscountid(discount);
                customertypeListCustomertype = em.merge(customertypeListCustomertype);
                if (oldDiscountidOfCustomertypeListCustomertype != null) {
                    oldDiscountidOfCustomertypeListCustomertype.getCustomertypeList().remove(customertypeListCustomertype);
                    oldDiscountidOfCustomertypeListCustomertype = em.merge(oldDiscountidOfCustomertypeListCustomertype);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiscount(discount.getId()) != null) {
                throw new PreexistingEntityException("Discount " + discount + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Discount discount) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Discount persistentDiscount = em.find(Discount.class, discount.getId());
            User useridOld = persistentDiscount.getUserid();
            User useridNew = discount.getUserid();
            List<Customertype> customertypeListOld = persistentDiscount.getCustomertypeList();
            List<Customertype> customertypeListNew = discount.getCustomertypeList();
            List<String> illegalOrphanMessages = null;
            for (Customertype customertypeListOldCustomertype : customertypeListOld) {
                if (!customertypeListNew.contains(customertypeListOldCustomertype)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Customertype " + customertypeListOldCustomertype + " since its discountid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                discount.setUserid(useridNew);
            }
            List<Customertype> attachedCustomertypeListNew = new ArrayList<Customertype>();
            for (Customertype customertypeListNewCustomertypeToAttach : customertypeListNew) {
                customertypeListNewCustomertypeToAttach = em.getReference(customertypeListNewCustomertypeToAttach.getClass(), customertypeListNewCustomertypeToAttach.getId());
                attachedCustomertypeListNew.add(customertypeListNewCustomertypeToAttach);
            }
            customertypeListNew = attachedCustomertypeListNew;
            discount.setCustomertypeList(customertypeListNew);
            discount = em.merge(discount);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getDiscountList().remove(discount);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getDiscountList().add(discount);
                useridNew = em.merge(useridNew);
            }
            for (Customertype customertypeListNewCustomertype : customertypeListNew) {
                if (!customertypeListOld.contains(customertypeListNewCustomertype)) {
                    Discount oldDiscountidOfCustomertypeListNewCustomertype = customertypeListNewCustomertype.getDiscountid();
                    customertypeListNewCustomertype.setDiscountid(discount);
                    customertypeListNewCustomertype = em.merge(customertypeListNewCustomertype);
                    if (oldDiscountidOfCustomertypeListNewCustomertype != null && !oldDiscountidOfCustomertypeListNewCustomertype.equals(discount)) {
                        oldDiscountidOfCustomertypeListNewCustomertype.getCustomertypeList().remove(customertypeListNewCustomertype);
                        oldDiscountidOfCustomertypeListNewCustomertype = em.merge(oldDiscountidOfCustomertypeListNewCustomertype);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = discount.getId();
                if (findDiscount(id) == null) {
                    throw new NonexistentEntityException("The discount with id " + id + " no longer exists.");
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
            Discount discount;
            try {
                discount = em.getReference(Discount.class, id);
                discount.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discount with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Customertype> customertypeListOrphanCheck = discount.getCustomertypeList();
            for (Customertype customertypeListOrphanCheckCustomertype : customertypeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Discount (" + discount + ") cannot be destroyed since the Customertype " + customertypeListOrphanCheckCustomertype + " in its customertypeList field has a non-nullable discountid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = discount.getUserid();
            if (userid != null) {
                userid.getDiscountList().remove(discount);
                userid = em.merge(userid);
            }
            em.remove(discount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Discount> findDiscountEntities() {
        return findDiscountEntities(true, -1, -1);
    }

    public List<Discount> findDiscountEntities(int maxResults, int firstResult) {
        return findDiscountEntities(false, maxResults, firstResult);
    }

    private List<Discount> findDiscountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Discount.class));
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

    public Discount findDiscount(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Discount.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Discount> rt = cq.from(Discount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
