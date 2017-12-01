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
import com.Entity.Usertype;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class UsertypeJpaController implements Serializable {

    public UsertypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usertype usertype) {
        if (usertype.getUserList() == null) {
            usertype.setUserList(new ArrayList<User>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<User> attachedUserList = new ArrayList<User>();
            for (User userListUserToAttach : usertype.getUserList()) {
                userListUserToAttach = em.getReference(userListUserToAttach.getClass(), userListUserToAttach.getId());
                attachedUserList.add(userListUserToAttach);
            }
            usertype.setUserList(attachedUserList);
            em.persist(usertype);
            for (User userListUser : usertype.getUserList()) {
                Usertype oldUserTypeidOfUserListUser = userListUser.getUserTypeid();
                userListUser.setUserTypeid(usertype);
                userListUser = em.merge(userListUser);
                if (oldUserTypeidOfUserListUser != null) {
                    oldUserTypeidOfUserListUser.getUserList().remove(userListUser);
                    oldUserTypeidOfUserListUser = em.merge(oldUserTypeidOfUserListUser);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usertype usertype) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usertype persistentUsertype = em.find(Usertype.class, usertype.getId());
            List<User> userListOld = persistentUsertype.getUserList();
            List<User> userListNew = usertype.getUserList();
            List<String> illegalOrphanMessages = null;
            for (User userListOldUser : userListOld) {
                if (!userListNew.contains(userListOldUser)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain User " + userListOldUser + " since its userTypeid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<User> attachedUserListNew = new ArrayList<User>();
            for (User userListNewUserToAttach : userListNew) {
                userListNewUserToAttach = em.getReference(userListNewUserToAttach.getClass(), userListNewUserToAttach.getId());
                attachedUserListNew.add(userListNewUserToAttach);
            }
            userListNew = attachedUserListNew;
            usertype.setUserList(userListNew);
            usertype = em.merge(usertype);
            for (User userListNewUser : userListNew) {
                if (!userListOld.contains(userListNewUser)) {
                    Usertype oldUserTypeidOfUserListNewUser = userListNewUser.getUserTypeid();
                    userListNewUser.setUserTypeid(usertype);
                    userListNewUser = em.merge(userListNewUser);
                    if (oldUserTypeidOfUserListNewUser != null && !oldUserTypeidOfUserListNewUser.equals(usertype)) {
                        oldUserTypeidOfUserListNewUser.getUserList().remove(userListNewUser);
                        oldUserTypeidOfUserListNewUser = em.merge(oldUserTypeidOfUserListNewUser);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usertype.getId();
                if (findUsertype(id) == null) {
                    throw new NonexistentEntityException("The usertype with id " + id + " no longer exists.");
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
            Usertype usertype;
            try {
                usertype = em.getReference(Usertype.class, id);
                usertype.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usertype with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<User> userListOrphanCheck = usertype.getUserList();
            for (User userListOrphanCheckUser : userListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usertype (" + usertype + ") cannot be destroyed since the User " + userListOrphanCheckUser + " in its userList field has a non-nullable userTypeid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usertype);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usertype> findUsertypeEntities() {
        return findUsertypeEntities(true, -1, -1);
    }

    public List<Usertype> findUsertypeEntities(int maxResults, int firstResult) {
        return findUsertypeEntities(false, maxResults, firstResult);
    }

    private List<Usertype> findUsertypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usertype.class));
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

    public Usertype findUsertype(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usertype.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsertypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usertype> rt = cq.from(Usertype.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
