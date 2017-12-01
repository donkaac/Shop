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
import com.Entity.Subcategory;
import com.Entity.Category;
import com.Entity.Brand;
import com.Entity.PurchaseOrderHasItem;
import java.util.ArrayList;
import java.util.List;
import com.Entity.ItemHasImage;
import com.Entity.Stock;
import com.Entity.GrnHasItem;
import com.Entity.Item;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) {
        if (item.getPurchaseOrderHasItemList() == null) {
            item.setPurchaseOrderHasItemList(new ArrayList<PurchaseOrderHasItem>());
        }
        if (item.getItemHasImageList() == null) {
            item.setItemHasImageList(new ArrayList<ItemHasImage>());
        }
        if (item.getStockList() == null) {
            item.setStockList(new ArrayList<Stock>());
        }
        if (item.getGrnHasItemList() == null) {
            item.setGrnHasItemList(new ArrayList<GrnHasItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userid = item.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getId());
                item.setUserid(userid);
            }
            Subcategory subCategoryid = item.getSubCategoryid();
            if (subCategoryid != null) {
                subCategoryid = em.getReference(subCategoryid.getClass(), subCategoryid.getId());
                item.setSubCategoryid(subCategoryid);
            }
            Category categoryid = item.getCategoryid();
            if (categoryid != null) {
                categoryid = em.getReference(categoryid.getClass(), categoryid.getId());
                item.setCategoryid(categoryid);
            }
            Brand brandid = item.getBrandid();
            if (brandid != null) {
                brandid = em.getReference(brandid.getClass(), brandid.getId());
                item.setBrandid(brandid);
            }
            List<PurchaseOrderHasItem> attachedPurchaseOrderHasItemList = new ArrayList<PurchaseOrderHasItem>();
            for (PurchaseOrderHasItem purchaseOrderHasItemListPurchaseOrderHasItemToAttach : item.getPurchaseOrderHasItemList()) {
                purchaseOrderHasItemListPurchaseOrderHasItemToAttach = em.getReference(purchaseOrderHasItemListPurchaseOrderHasItemToAttach.getClass(), purchaseOrderHasItemListPurchaseOrderHasItemToAttach.getId());
                attachedPurchaseOrderHasItemList.add(purchaseOrderHasItemListPurchaseOrderHasItemToAttach);
            }
            item.setPurchaseOrderHasItemList(attachedPurchaseOrderHasItemList);
            List<ItemHasImage> attachedItemHasImageList = new ArrayList<ItemHasImage>();
            for (ItemHasImage itemHasImageListItemHasImageToAttach : item.getItemHasImageList()) {
                itemHasImageListItemHasImageToAttach = em.getReference(itemHasImageListItemHasImageToAttach.getClass(), itemHasImageListItemHasImageToAttach.getId());
                attachedItemHasImageList.add(itemHasImageListItemHasImageToAttach);
            }
            item.setItemHasImageList(attachedItemHasImageList);
            List<Stock> attachedStockList = new ArrayList<Stock>();
            for (Stock stockListStockToAttach : item.getStockList()) {
                stockListStockToAttach = em.getReference(stockListStockToAttach.getClass(), stockListStockToAttach.getId());
                attachedStockList.add(stockListStockToAttach);
            }
            item.setStockList(attachedStockList);
            List<GrnHasItem> attachedGrnHasItemList = new ArrayList<GrnHasItem>();
            for (GrnHasItem grnHasItemListGrnHasItemToAttach : item.getGrnHasItemList()) {
                grnHasItemListGrnHasItemToAttach = em.getReference(grnHasItemListGrnHasItemToAttach.getClass(), grnHasItemListGrnHasItemToAttach.getId());
                attachedGrnHasItemList.add(grnHasItemListGrnHasItemToAttach);
            }
            item.setGrnHasItemList(attachedGrnHasItemList);
            em.persist(item);
            if (userid != null) {
                userid.getItemList().add(item);
                userid = em.merge(userid);
            }
            if (subCategoryid != null) {
                subCategoryid.getItemList().add(item);
                subCategoryid = em.merge(subCategoryid);
            }
            if (categoryid != null) {
                categoryid.getItemList().add(item);
                categoryid = em.merge(categoryid);
            }
            if (brandid != null) {
                brandid.getItemList().add(item);
                brandid = em.merge(brandid);
            }
            for (PurchaseOrderHasItem purchaseOrderHasItemListPurchaseOrderHasItem : item.getPurchaseOrderHasItemList()) {
                Item oldItemidOfPurchaseOrderHasItemListPurchaseOrderHasItem = purchaseOrderHasItemListPurchaseOrderHasItem.getItemid();
                purchaseOrderHasItemListPurchaseOrderHasItem.setItemid(item);
                purchaseOrderHasItemListPurchaseOrderHasItem = em.merge(purchaseOrderHasItemListPurchaseOrderHasItem);
                if (oldItemidOfPurchaseOrderHasItemListPurchaseOrderHasItem != null) {
                    oldItemidOfPurchaseOrderHasItemListPurchaseOrderHasItem.getPurchaseOrderHasItemList().remove(purchaseOrderHasItemListPurchaseOrderHasItem);
                    oldItemidOfPurchaseOrderHasItemListPurchaseOrderHasItem = em.merge(oldItemidOfPurchaseOrderHasItemListPurchaseOrderHasItem);
                }
            }
            for (ItemHasImage itemHasImageListItemHasImage : item.getItemHasImageList()) {
                Item oldItemidOfItemHasImageListItemHasImage = itemHasImageListItemHasImage.getItemid();
                itemHasImageListItemHasImage.setItemid(item);
                itemHasImageListItemHasImage = em.merge(itemHasImageListItemHasImage);
                if (oldItemidOfItemHasImageListItemHasImage != null) {
                    oldItemidOfItemHasImageListItemHasImage.getItemHasImageList().remove(itemHasImageListItemHasImage);
                    oldItemidOfItemHasImageListItemHasImage = em.merge(oldItemidOfItemHasImageListItemHasImage);
                }
            }
            for (Stock stockListStock : item.getStockList()) {
                Item oldItemidOfStockListStock = stockListStock.getItemid();
                stockListStock.setItemid(item);
                stockListStock = em.merge(stockListStock);
                if (oldItemidOfStockListStock != null) {
                    oldItemidOfStockListStock.getStockList().remove(stockListStock);
                    oldItemidOfStockListStock = em.merge(oldItemidOfStockListStock);
                }
            }
            for (GrnHasItem grnHasItemListGrnHasItem : item.getGrnHasItemList()) {
                Item oldItemidOfGrnHasItemListGrnHasItem = grnHasItemListGrnHasItem.getItemid();
                grnHasItemListGrnHasItem.setItemid(item);
                grnHasItemListGrnHasItem = em.merge(grnHasItemListGrnHasItem);
                if (oldItemidOfGrnHasItemListGrnHasItem != null) {
                    oldItemidOfGrnHasItemListGrnHasItem.getGrnHasItemList().remove(grnHasItemListGrnHasItem);
                    oldItemidOfGrnHasItemListGrnHasItem = em.merge(oldItemidOfGrnHasItemListGrnHasItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getId());
            User useridOld = persistentItem.getUserid();
            User useridNew = item.getUserid();
            Subcategory subCategoryidOld = persistentItem.getSubCategoryid();
            Subcategory subCategoryidNew = item.getSubCategoryid();
            Category categoryidOld = persistentItem.getCategoryid();
            Category categoryidNew = item.getCategoryid();
            Brand brandidOld = persistentItem.getBrandid();
            Brand brandidNew = item.getBrandid();
            List<PurchaseOrderHasItem> purchaseOrderHasItemListOld = persistentItem.getPurchaseOrderHasItemList();
            List<PurchaseOrderHasItem> purchaseOrderHasItemListNew = item.getPurchaseOrderHasItemList();
            List<ItemHasImage> itemHasImageListOld = persistentItem.getItemHasImageList();
            List<ItemHasImage> itemHasImageListNew = item.getItemHasImageList();
            List<Stock> stockListOld = persistentItem.getStockList();
            List<Stock> stockListNew = item.getStockList();
            List<GrnHasItem> grnHasItemListOld = persistentItem.getGrnHasItemList();
            List<GrnHasItem> grnHasItemListNew = item.getGrnHasItemList();
            List<String> illegalOrphanMessages = null;
            for (PurchaseOrderHasItem purchaseOrderHasItemListOldPurchaseOrderHasItem : purchaseOrderHasItemListOld) {
                if (!purchaseOrderHasItemListNew.contains(purchaseOrderHasItemListOldPurchaseOrderHasItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PurchaseOrderHasItem " + purchaseOrderHasItemListOldPurchaseOrderHasItem + " since its itemid field is not nullable.");
                }
            }
            for (ItemHasImage itemHasImageListOldItemHasImage : itemHasImageListOld) {
                if (!itemHasImageListNew.contains(itemHasImageListOldItemHasImage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemHasImage " + itemHasImageListOldItemHasImage + " since its itemid field is not nullable.");
                }
            }
            for (Stock stockListOldStock : stockListOld) {
                if (!stockListNew.contains(stockListOldStock)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Stock " + stockListOldStock + " since its itemid field is not nullable.");
                }
            }
            for (GrnHasItem grnHasItemListOldGrnHasItem : grnHasItemListOld) {
                if (!grnHasItemListNew.contains(grnHasItemListOldGrnHasItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GrnHasItem " + grnHasItemListOldGrnHasItem + " since its itemid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getId());
                item.setUserid(useridNew);
            }
            if (subCategoryidNew != null) {
                subCategoryidNew = em.getReference(subCategoryidNew.getClass(), subCategoryidNew.getId());
                item.setSubCategoryid(subCategoryidNew);
            }
            if (categoryidNew != null) {
                categoryidNew = em.getReference(categoryidNew.getClass(), categoryidNew.getId());
                item.setCategoryid(categoryidNew);
            }
            if (brandidNew != null) {
                brandidNew = em.getReference(brandidNew.getClass(), brandidNew.getId());
                item.setBrandid(brandidNew);
            }
            List<PurchaseOrderHasItem> attachedPurchaseOrderHasItemListNew = new ArrayList<PurchaseOrderHasItem>();
            for (PurchaseOrderHasItem purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach : purchaseOrderHasItemListNew) {
                purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach = em.getReference(purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach.getClass(), purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach.getId());
                attachedPurchaseOrderHasItemListNew.add(purchaseOrderHasItemListNewPurchaseOrderHasItemToAttach);
            }
            purchaseOrderHasItemListNew = attachedPurchaseOrderHasItemListNew;
            item.setPurchaseOrderHasItemList(purchaseOrderHasItemListNew);
            List<ItemHasImage> attachedItemHasImageListNew = new ArrayList<ItemHasImage>();
            for (ItemHasImage itemHasImageListNewItemHasImageToAttach : itemHasImageListNew) {
                itemHasImageListNewItemHasImageToAttach = em.getReference(itemHasImageListNewItemHasImageToAttach.getClass(), itemHasImageListNewItemHasImageToAttach.getId());
                attachedItemHasImageListNew.add(itemHasImageListNewItemHasImageToAttach);
            }
            itemHasImageListNew = attachedItemHasImageListNew;
            item.setItemHasImageList(itemHasImageListNew);
            List<Stock> attachedStockListNew = new ArrayList<Stock>();
            for (Stock stockListNewStockToAttach : stockListNew) {
                stockListNewStockToAttach = em.getReference(stockListNewStockToAttach.getClass(), stockListNewStockToAttach.getId());
                attachedStockListNew.add(stockListNewStockToAttach);
            }
            stockListNew = attachedStockListNew;
            item.setStockList(stockListNew);
            List<GrnHasItem> attachedGrnHasItemListNew = new ArrayList<GrnHasItem>();
            for (GrnHasItem grnHasItemListNewGrnHasItemToAttach : grnHasItemListNew) {
                grnHasItemListNewGrnHasItemToAttach = em.getReference(grnHasItemListNewGrnHasItemToAttach.getClass(), grnHasItemListNewGrnHasItemToAttach.getId());
                attachedGrnHasItemListNew.add(grnHasItemListNewGrnHasItemToAttach);
            }
            grnHasItemListNew = attachedGrnHasItemListNew;
            item.setGrnHasItemList(grnHasItemListNew);
            item = em.merge(item);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getItemList().remove(item);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getItemList().add(item);
                useridNew = em.merge(useridNew);
            }
            if (subCategoryidOld != null && !subCategoryidOld.equals(subCategoryidNew)) {
                subCategoryidOld.getItemList().remove(item);
                subCategoryidOld = em.merge(subCategoryidOld);
            }
            if (subCategoryidNew != null && !subCategoryidNew.equals(subCategoryidOld)) {
                subCategoryidNew.getItemList().add(item);
                subCategoryidNew = em.merge(subCategoryidNew);
            }
            if (categoryidOld != null && !categoryidOld.equals(categoryidNew)) {
                categoryidOld.getItemList().remove(item);
                categoryidOld = em.merge(categoryidOld);
            }
            if (categoryidNew != null && !categoryidNew.equals(categoryidOld)) {
                categoryidNew.getItemList().add(item);
                categoryidNew = em.merge(categoryidNew);
            }
            if (brandidOld != null && !brandidOld.equals(brandidNew)) {
                brandidOld.getItemList().remove(item);
                brandidOld = em.merge(brandidOld);
            }
            if (brandidNew != null && !brandidNew.equals(brandidOld)) {
                brandidNew.getItemList().add(item);
                brandidNew = em.merge(brandidNew);
            }
            for (PurchaseOrderHasItem purchaseOrderHasItemListNewPurchaseOrderHasItem : purchaseOrderHasItemListNew) {
                if (!purchaseOrderHasItemListOld.contains(purchaseOrderHasItemListNewPurchaseOrderHasItem)) {
                    Item oldItemidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem = purchaseOrderHasItemListNewPurchaseOrderHasItem.getItemid();
                    purchaseOrderHasItemListNewPurchaseOrderHasItem.setItemid(item);
                    purchaseOrderHasItemListNewPurchaseOrderHasItem = em.merge(purchaseOrderHasItemListNewPurchaseOrderHasItem);
                    if (oldItemidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem != null && !oldItemidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem.equals(item)) {
                        oldItemidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem.getPurchaseOrderHasItemList().remove(purchaseOrderHasItemListNewPurchaseOrderHasItem);
                        oldItemidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem = em.merge(oldItemidOfPurchaseOrderHasItemListNewPurchaseOrderHasItem);
                    }
                }
            }
            for (ItemHasImage itemHasImageListNewItemHasImage : itemHasImageListNew) {
                if (!itemHasImageListOld.contains(itemHasImageListNewItemHasImage)) {
                    Item oldItemidOfItemHasImageListNewItemHasImage = itemHasImageListNewItemHasImage.getItemid();
                    itemHasImageListNewItemHasImage.setItemid(item);
                    itemHasImageListNewItemHasImage = em.merge(itemHasImageListNewItemHasImage);
                    if (oldItemidOfItemHasImageListNewItemHasImage != null && !oldItemidOfItemHasImageListNewItemHasImage.equals(item)) {
                        oldItemidOfItemHasImageListNewItemHasImage.getItemHasImageList().remove(itemHasImageListNewItemHasImage);
                        oldItemidOfItemHasImageListNewItemHasImage = em.merge(oldItemidOfItemHasImageListNewItemHasImage);
                    }
                }
            }
            for (Stock stockListNewStock : stockListNew) {
                if (!stockListOld.contains(stockListNewStock)) {
                    Item oldItemidOfStockListNewStock = stockListNewStock.getItemid();
                    stockListNewStock.setItemid(item);
                    stockListNewStock = em.merge(stockListNewStock);
                    if (oldItemidOfStockListNewStock != null && !oldItemidOfStockListNewStock.equals(item)) {
                        oldItemidOfStockListNewStock.getStockList().remove(stockListNewStock);
                        oldItemidOfStockListNewStock = em.merge(oldItemidOfStockListNewStock);
                    }
                }
            }
            for (GrnHasItem grnHasItemListNewGrnHasItem : grnHasItemListNew) {
                if (!grnHasItemListOld.contains(grnHasItemListNewGrnHasItem)) {
                    Item oldItemidOfGrnHasItemListNewGrnHasItem = grnHasItemListNewGrnHasItem.getItemid();
                    grnHasItemListNewGrnHasItem.setItemid(item);
                    grnHasItemListNewGrnHasItem = em.merge(grnHasItemListNewGrnHasItem);
                    if (oldItemidOfGrnHasItemListNewGrnHasItem != null && !oldItemidOfGrnHasItemListNewGrnHasItem.equals(item)) {
                        oldItemidOfGrnHasItemListNewGrnHasItem.getGrnHasItemList().remove(grnHasItemListNewGrnHasItem);
                        oldItemidOfGrnHasItemListNewGrnHasItem = em.merge(oldItemidOfGrnHasItemListNewGrnHasItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = item.getId();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PurchaseOrderHasItem> purchaseOrderHasItemListOrphanCheck = item.getPurchaseOrderHasItemList();
            for (PurchaseOrderHasItem purchaseOrderHasItemListOrphanCheckPurchaseOrderHasItem : purchaseOrderHasItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the PurchaseOrderHasItem " + purchaseOrderHasItemListOrphanCheckPurchaseOrderHasItem + " in its purchaseOrderHasItemList field has a non-nullable itemid field.");
            }
            List<ItemHasImage> itemHasImageListOrphanCheck = item.getItemHasImageList();
            for (ItemHasImage itemHasImageListOrphanCheckItemHasImage : itemHasImageListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the ItemHasImage " + itemHasImageListOrphanCheckItemHasImage + " in its itemHasImageList field has a non-nullable itemid field.");
            }
            List<Stock> stockListOrphanCheck = item.getStockList();
            for (Stock stockListOrphanCheckStock : stockListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the Stock " + stockListOrphanCheckStock + " in its stockList field has a non-nullable itemid field.");
            }
            List<GrnHasItem> grnHasItemListOrphanCheck = item.getGrnHasItemList();
            for (GrnHasItem grnHasItemListOrphanCheckGrnHasItem : grnHasItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the GrnHasItem " + grnHasItemListOrphanCheckGrnHasItem + " in its grnHasItemList field has a non-nullable itemid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userid = item.getUserid();
            if (userid != null) {
                userid.getItemList().remove(item);
                userid = em.merge(userid);
            }
            Subcategory subCategoryid = item.getSubCategoryid();
            if (subCategoryid != null) {
                subCategoryid.getItemList().remove(item);
                subCategoryid = em.merge(subCategoryid);
            }
            Category categoryid = item.getCategoryid();
            if (categoryid != null) {
                categoryid.getItemList().remove(item);
                categoryid = em.merge(categoryid);
            }
            Brand brandid = item.getBrandid();
            if (brandid != null) {
                brandid.getItemList().remove(item);
                brandid = em.merge(brandid);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
