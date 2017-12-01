/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.IllegalOrphanException;
import com.Controller.exceptions.NonexistentEntityException;
import com.Entity.Login;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.User;
import com.Entity.Loginhistory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class LoginJpaController implements Serializable {

    public LoginJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Login login) {
        if (login.getLoginhistoryList() == null) {
            login.setLoginhistoryList(new ArrayList<Loginhistory>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = login.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                login.setUserid(userid);
            }
            List<Loginhistory> attachedLoginhistoryList = new ArrayList<Loginhistory>();
            for (Loginhistory loginhistoryListLoginhistoryToAttach : login.getLoginhistoryList()) {
                loginhistoryListLoginhistoryToAttach = em.getReference(loginhistoryListLoginhistoryToAttach.getClass(), loginhistoryListLoginhistoryToAttach.getId());
                attachedLoginhistoryList.add(loginhistoryListLoginhistoryToAttach);
            }
            login.setLoginhistoryList(attachedLoginhistoryList);
            em.persist(login);
            if (userid != null) {
                userid.getLoginList().add(login);
                userid = em.merge(userid);
            }
            for (Loginhistory loginhistoryListLoginhistory : login.getLoginhistoryList()) {
                Login oldLoginidOfLoginhistoryListLoginhistory = loginhistoryListLoginhistory.getLoginid();
                loginhistoryListLoginhistory.setLoginid(login);
                loginhistoryListLoginhistory = em.merge(loginhistoryListLoginhistory);
                if (oldLoginidOfLoginhistoryListLoginhistory != null) {
                    oldLoginidOfLoginhistoryListLoginhistory.getLoginhistoryList().remove(loginhistoryListLoginhistory);
                    oldLoginidOfLoginhistoryListLoginhistory = em.merge(oldLoginidOfLoginhistoryListLoginhistory);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Login login) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Login persistentLogin = em.find(Login.class, login.getId());
            User useridOld = persistentLogin.getUserid();
            User useridNew = login.getUserid();
            List<Loginhistory> loginhistoryListOld = persistentLogin.getLoginhistoryList();
            List<Loginhistory> loginhistoryListNew = login.getLoginhistoryList();
            List<String> illegalOrphanMessages = null;
            for (Loginhistory loginhistoryListOldLoginhistory : loginhistoryListOld) {
                if (!loginhistoryListNew.contains(loginhistoryListOldLoginhistory)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Loginhistory " + loginhistoryListOldLoginhistory + " since its loginid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                login.setUserid(useridNew);
            }
            List<Loginhistory> attachedLoginhistoryListNew = new ArrayList<Loginhistory>();
            for (Loginhistory loginhistoryListNewLoginhistoryToAttach : loginhistoryListNew) {
                loginhistoryListNewLoginhistoryToAttach = em.getReference(loginhistoryListNewLoginhistoryToAttach.getClass(), loginhistoryListNewLoginhistoryToAttach.getId());
                attachedLoginhistoryListNew.add(loginhistoryListNewLoginhistoryToAttach);
            }
            loginhistoryListNew = attachedLoginhistoryListNew;
            login.setLoginhistoryList(loginhistoryListNew);
            login = em.merge(login);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getLoginList().remove(login);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getLoginList().add(login);
                useridNew = em.merge(useridNew);
            }
            for (Loginhistory loginhistoryListNewLoginhistory : loginhistoryListNew) {
                if (!loginhistoryListOld.contains(loginhistoryListNewLoginhistory)) {
                    Login oldLoginidOfLoginhistoryListNewLoginhistory = loginhistoryListNewLoginhistory.getLoginid();
                    loginhistoryListNewLoginhistory.setLoginid(login);
                    loginhistoryListNewLoginhistory = em.merge(loginhistoryListNewLoginhistory);
                    if (oldLoginidOfLoginhistoryListNewLoginhistory != null && !oldLoginidOfLoginhistoryListNewLoginhistory.equals(login)) {
                        oldLoginidOfLoginhistoryListNewLoginhistory.getLoginhistoryList().remove(loginhistoryListNewLoginhistory);
                        oldLoginidOfLoginhistoryListNewLoginhistory = em.merge(oldLoginidOfLoginhistoryListNewLoginhistory);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = login.getId();
                if (findLogin(id) == null) {
                    throw new NonexistentEntityException("The login with id " + id + " no longer exists.");
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
            Login login;
            try {
                login = em.getReference(Login.class, id);
                login.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The login with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Loginhistory> loginhistoryListOrphanCheck = login.getLoginhistoryList();
            for (Loginhistory loginhistoryListOrphanCheckLoginhistory : loginhistoryListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Login (" + login + ") cannot be destroyed since the Loginhistory " + loginhistoryListOrphanCheckLoginhistory + " in its loginhistoryList field has a non-nullable loginid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = login.getUserid();
            if (userid != null) {
                userid.getLoginList().remove(login);
                userid = em.merge(userid);
            }
            em.remove(login);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Login> findLoginEntities() {
        return findLoginEntities(true, -1, -1);
    }

    public List<Login> findLoginEntities(int maxResults, int firstResult) {
        return findLoginEntities(false, maxResults, firstResult);
    }

    private List<Login> findLoginEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Login.class));
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

    public Login findLogin(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Login.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoginCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Login> rt = cq.from(Login.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
