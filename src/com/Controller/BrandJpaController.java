/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.exceptions.IllegalOrphanException;
import com.Controller.exceptions.NonexistentEntityException;
import com.Entity.Brand;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Entity.User;
import com.Entity.Item;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class BrandJpaController implements Serializable {

    public BrandJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Brand brand) {
        if (brand.getItemList() == null) {
            brand.setItemList(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = brand.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                brand.setUserid(userid);
            }
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : brand.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getId());
                attachedItemList.add(itemListItemToAttach);
            }
            brand.setItemList(attachedItemList);
            em.persist(brand);
            if (userid != null) {
                userid.getBrandList().add(brand);
                userid = em.merge(userid);
            }
            for (Item itemListItem : brand.getItemList()) {
                Brand oldBrandidOfItemListItem = itemListItem.getBrandid();
                itemListItem.setBrandid(brand);
                itemListItem = em.merge(itemListItem);
                if (oldBrandidOfItemListItem != null) {
                    oldBrandidOfItemListItem.getItemList().remove(itemListItem);
                    oldBrandidOfItemListItem = em.merge(oldBrandidOfItemListItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Brand brand) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Brand persistentBrand = em.find(Brand.class, brand.getId());
            User useridOld = persistentBrand.getUserid();
            User useridNew = brand.getUserid();
            List<Item> itemListOld = persistentBrand.getItemList();
            List<Item> itemListNew = brand.getItemList();
            List<String> illegalOrphanMessages = null;
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Item " + itemListOldItem + " since its brandid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                brand.setUserid(useridNew);
            }
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getId());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            brand.setItemList(itemListNew);
            brand = em.merge(brand);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getBrandList().remove(brand);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getBrandList().add(brand);
                useridNew = em.merge(useridNew);
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    Brand oldBrandidOfItemListNewItem = itemListNewItem.getBrandid();
                    itemListNewItem.setBrandid(brand);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldBrandidOfItemListNewItem != null && !oldBrandidOfItemListNewItem.equals(brand)) {
                        oldBrandidOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldBrandidOfItemListNewItem = em.merge(oldBrandidOfItemListNewItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = brand.getId();
                if (findBrand(id) == null) {
                    throw new NonexistentEntityException("The brand with id " + id + " no longer exists.");
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
            Brand brand;
            try {
                brand = em.getReference(Brand.class, id);
                brand.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The brand with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Item> itemListOrphanCheck = brand.getItemList();
            for (Item itemListOrphanCheckItem : itemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Brand (" + brand + ") cannot be destroyed since the Item " + itemListOrphanCheckItem + " in its itemList field has a non-nullable brandid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = brand.getUserid();
            if (userid != null) {
                userid.getBrandList().remove(brand);
                userid = em.merge(userid);
            }
            em.remove(brand);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Brand> findBrandEntities() {
        return findBrandEntities(true, -1, -1);
    }

    public List<Brand> findBrandEntities(int maxResults, int firstResult) {
        return findBrandEntities(false, maxResults, firstResult);
    }

    private List<Brand> findBrandEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Brand.class));
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

    public Brand findBrand(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Brand.class, id);
        } finally {
            em.close();
        }
    }

    public int getBrandCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Brand> rt = cq.from(Brand.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
