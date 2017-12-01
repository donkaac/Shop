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
import com.Entity.Item;
import com.Entity.ItemHasImage;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class ItemHasImageJpaController implements Serializable {

    public ItemHasImageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemHasImage itemHasImage) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item itemid = itemHasImage.getItemid();
            if (itemid != null) {
                itemid = em.getReference(itemid.getClass(), itemid.getId());
                itemHasImage.setItemid(itemid);
            }
            em.persist(itemHasImage);
            if (itemid != null) {
                itemid.getItemHasImageList().add(itemHasImage);
                itemid = em.merge(itemid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItemHasImage itemHasImage) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemHasImage persistentItemHasImage = em.find(ItemHasImage.class, itemHasImage.getId());
            Item itemidOld = persistentItemHasImage.getItemid();
            Item itemidNew = itemHasImage.getItemid();
            if (itemidNew != null) {
                itemidNew = em.getReference(itemidNew.getClass(), itemidNew.getId());
                itemHasImage.setItemid(itemidNew);
            }
            itemHasImage = em.merge(itemHasImage);
            if (itemidOld != null && !itemidOld.equals(itemidNew)) {
                itemidOld.getItemHasImageList().remove(itemHasImage);
                itemidOld = em.merge(itemidOld);
            }
            if (itemidNew != null && !itemidNew.equals(itemidOld)) {
                itemidNew.getItemHasImageList().add(itemHasImage);
                itemidNew = em.merge(itemidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = itemHasImage.getId();
                if (findItemHasImage(id) == null) {
                    throw new NonexistentEntityException("The itemHasImage with id " + id + " no longer exists.");
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
            ItemHasImage itemHasImage;
            try {
                itemHasImage = em.getReference(ItemHasImage.class, id);
                itemHasImage.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemHasImage with id " + id + " no longer exists.", enfe);
            }
            Item itemid = itemHasImage.getItemid();
            if (itemid != null) {
                itemid.getItemHasImageList().remove(itemHasImage);
                itemid = em.merge(itemid);
            }
            em.remove(itemHasImage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ItemHasImage> findItemHasImageEntities() {
        return findItemHasImageEntities(true, -1, -1);
    }

    public List<ItemHasImage> findItemHasImageEntities(int maxResults, int firstResult) {
        return findItemHasImageEntities(false, maxResults, firstResult);
    }

    private List<ItemHasImage> findItemHasImageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemHasImage.class));
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

    public ItemHasImage findItemHasImage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemHasImage.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemHasImageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemHasImage> rt = cq.from(ItemHasImage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
