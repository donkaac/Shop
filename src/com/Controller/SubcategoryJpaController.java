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
import com.Entity.Item;
import com.Entity.Subcategory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class SubcategoryJpaController implements Serializable {

    public SubcategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subcategory subcategory) {
        if (subcategory.getItemList() == null) {
            subcategory.setItemList(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = subcategory.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                subcategory.setUserid(userid);
            }
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : subcategory.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getId());
                attachedItemList.add(itemListItemToAttach);
            }
            subcategory.setItemList(attachedItemList);
            em.persist(subcategory);
            if (userid != null) {
                userid.getSubcategoryList().add(subcategory);
                userid = em.merge(userid);
            }
            for (Item itemListItem : subcategory.getItemList()) {
                Subcategory oldSubCategoryidOfItemListItem = itemListItem.getSubCategoryid();
                itemListItem.setSubCategoryid(subcategory);
                itemListItem = em.merge(itemListItem);
                if (oldSubCategoryidOfItemListItem != null) {
                    oldSubCategoryidOfItemListItem.getItemList().remove(itemListItem);
                    oldSubCategoryidOfItemListItem = em.merge(oldSubCategoryidOfItemListItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subcategory subcategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subcategory persistentSubcategory = em.find(Subcategory.class, subcategory.getId());
            User useridOld = persistentSubcategory.getUserid();
            User useridNew = subcategory.getUserid();
            List<Item> itemListOld = persistentSubcategory.getItemList();
            List<Item> itemListNew = subcategory.getItemList();
            List<String> illegalOrphanMessages = null;
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Item " + itemListOldItem + " since its subCategoryid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                subcategory.setUserid(useridNew);
            }
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getId());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            subcategory.setItemList(itemListNew);
            subcategory = em.merge(subcategory);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getSubcategoryList().remove(subcategory);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getSubcategoryList().add(subcategory);
                useridNew = em.merge(useridNew);
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    Subcategory oldSubCategoryidOfItemListNewItem = itemListNewItem.getSubCategoryid();
                    itemListNewItem.setSubCategoryid(subcategory);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldSubCategoryidOfItemListNewItem != null && !oldSubCategoryidOfItemListNewItem.equals(subcategory)) {
                        oldSubCategoryidOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldSubCategoryidOfItemListNewItem = em.merge(oldSubCategoryidOfItemListNewItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subcategory.getId();
                if (findSubcategory(id) == null) {
                    throw new NonexistentEntityException("The subcategory with id " + id + " no longer exists.");
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
            Subcategory subcategory;
            try {
                subcategory = em.getReference(Subcategory.class, id);
                subcategory.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subcategory with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Item> itemListOrphanCheck = subcategory.getItemList();
            for (Item itemListOrphanCheckItem : itemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subcategory (" + subcategory + ") cannot be destroyed since the Item " + itemListOrphanCheckItem + " in its itemList field has a non-nullable subCategoryid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = subcategory.getUserid();
            if (userid != null) {
                userid.getSubcategoryList().remove(subcategory);
                userid = em.merge(userid);
            }
            em.remove(subcategory);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subcategory> findSubcategoryEntities() {
        return findSubcategoryEntities(true, -1, -1);
    }

    public List<Subcategory> findSubcategoryEntities(int maxResults, int firstResult) {
        return findSubcategoryEntities(false, maxResults, firstResult);
    }

    private List<Subcategory> findSubcategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subcategory.class));
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

    public Subcategory findSubcategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subcategory.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubcategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subcategory> rt = cq.from(Subcategory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
