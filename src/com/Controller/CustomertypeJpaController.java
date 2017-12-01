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
import com.Entity.Discount;
import com.Entity.Customer;
import com.Entity.Customertype;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class CustomertypeJpaController implements Serializable {

    public CustomertypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customertype customertype) {
        if (customertype.getCustomerList() == null) {
            customertype.setCustomerList(new ArrayList<Customer>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = customertype.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                customertype.setUserid(userid);
            }
            Discount discountid = customertype.getDiscountid();
            if (discountid != null) {
                discountid = em.getReference(discountid.getClass(), discountid.getId());
                customertype.setDiscountid(discountid);
            }
            List<Customer> attachedCustomerList = new ArrayList<Customer>();
            for (Customer customerListCustomerToAttach : customertype.getCustomerList()) {
                customerListCustomerToAttach = em.getReference(customerListCustomerToAttach.getClass(), customerListCustomerToAttach.getId());
                attachedCustomerList.add(customerListCustomerToAttach);
            }
            customertype.setCustomerList(attachedCustomerList);
            em.persist(customertype);
            if (userid != null) {
                userid.getCustomertypeList().add(customertype);
                userid = em.merge(userid);
            }
            if (discountid != null) {
                discountid.getCustomertypeList().add(customertype);
                discountid = em.merge(discountid);
            }
            for (Customer customerListCustomer : customertype.getCustomerList()) {
                Customertype oldCustomerTypeidOfCustomerListCustomer = customerListCustomer.getCustomerTypeid();
                customerListCustomer.setCustomerTypeid(customertype);
                customerListCustomer = em.merge(customerListCustomer);
                if (oldCustomerTypeidOfCustomerListCustomer != null) {
                    oldCustomerTypeidOfCustomerListCustomer.getCustomerList().remove(customerListCustomer);
                    oldCustomerTypeidOfCustomerListCustomer = em.merge(oldCustomerTypeidOfCustomerListCustomer);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customertype customertype) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customertype persistentCustomertype = em.find(Customertype.class, customertype.getId());
            User useridOld = persistentCustomertype.getUserid();
            User useridNew = customertype.getUserid();
            Discount discountidOld = persistentCustomertype.getDiscountid();
            Discount discountidNew = customertype.getDiscountid();
            List<Customer> customerListOld = persistentCustomertype.getCustomerList();
            List<Customer> customerListNew = customertype.getCustomerList();
            List<String> illegalOrphanMessages = null;
            for (Customer customerListOldCustomer : customerListOld) {
                if (!customerListNew.contains(customerListOldCustomer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Customer " + customerListOldCustomer + " since its customerTypeid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                customertype.setUserid(useridNew);
            }
            if (discountidNew != null) {
                discountidNew = em.getReference(discountidNew.getClass(), discountidNew.getId());
                customertype.setDiscountid(discountidNew);
            }
            List<Customer> attachedCustomerListNew = new ArrayList<Customer>();
            for (Customer customerListNewCustomerToAttach : customerListNew) {
                customerListNewCustomerToAttach = em.getReference(customerListNewCustomerToAttach.getClass(), customerListNewCustomerToAttach.getId());
                attachedCustomerListNew.add(customerListNewCustomerToAttach);
            }
            customerListNew = attachedCustomerListNew;
            customertype.setCustomerList(customerListNew);
            customertype = em.merge(customertype);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getCustomertypeList().remove(customertype);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getCustomertypeList().add(customertype);
                useridNew = em.merge(useridNew);
            }
            if (discountidOld != null && !discountidOld.equals(discountidNew)) {
                discountidOld.getCustomertypeList().remove(customertype);
                discountidOld = em.merge(discountidOld);
            }
            if (discountidNew != null && !discountidNew.equals(discountidOld)) {
                discountidNew.getCustomertypeList().add(customertype);
                discountidNew = em.merge(discountidNew);
            }
            for (Customer customerListNewCustomer : customerListNew) {
                if (!customerListOld.contains(customerListNewCustomer)) {
                    Customertype oldCustomerTypeidOfCustomerListNewCustomer = customerListNewCustomer.getCustomerTypeid();
                    customerListNewCustomer.setCustomerTypeid(customertype);
                    customerListNewCustomer = em.merge(customerListNewCustomer);
                    if (oldCustomerTypeidOfCustomerListNewCustomer != null && !oldCustomerTypeidOfCustomerListNewCustomer.equals(customertype)) {
                        oldCustomerTypeidOfCustomerListNewCustomer.getCustomerList().remove(customerListNewCustomer);
                        oldCustomerTypeidOfCustomerListNewCustomer = em.merge(oldCustomerTypeidOfCustomerListNewCustomer);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = customertype.getId();
                if (findCustomertype(id) == null) {
                    throw new NonexistentEntityException("The customertype with id " + id + " no longer exists.");
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
            Customertype customertype;
            try {
                customertype = em.getReference(Customertype.class, id);
                customertype.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customertype with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Customer> customerListOrphanCheck = customertype.getCustomerList();
            for (Customer customerListOrphanCheckCustomer : customerListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Customertype (" + customertype + ") cannot be destroyed since the Customer " + customerListOrphanCheckCustomer + " in its customerList field has a non-nullable customerTypeid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = customertype.getUserid();
            if (userid != null) {
                userid.getCustomertypeList().remove(customertype);
                userid = em.merge(userid);
            }
            Discount discountid = customertype.getDiscountid();
            if (discountid != null) {
                discountid.getCustomertypeList().remove(customertype);
                discountid = em.merge(discountid);
            }
            em.remove(customertype);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customertype> findCustomertypeEntities() {
        return findCustomertypeEntities(true, -1, -1);
    }

    public List<Customertype> findCustomertypeEntities(int maxResults, int firstResult) {
        return findCustomertypeEntities(false, maxResults, firstResult);
    }

    private List<Customertype> findCustomertypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customertype.class));
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

    public Customertype findCustomertype(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customertype.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomertypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customertype> rt = cq.from(Customertype.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
