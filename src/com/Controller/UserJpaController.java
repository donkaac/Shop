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
import com.Entity.Usertype;
import com.Entity.Invoice;
import java.util.ArrayList;
import java.util.List;
import com.Entity.Customer;
import com.Entity.Purchaseorder;
import com.Entity.Customertype;
import com.Entity.Grn;
import com.Entity.Login;
import com.Entity.Supplier;
import com.Entity.UserHasAction;
import com.Entity.Agent;
import com.Entity.Discount;
import com.Entity.Category;
import com.Entity.Tax;
import com.Entity.Batch;
import com.Entity.Subcategory;
import com.Entity.Item;
import com.Entity.Brand;
import com.Entity.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Achi
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws PreexistingEntityException, Exception {
        if (user.getInvoiceList() == null) {
            user.setInvoiceList(new ArrayList<Invoice>());
        }
        if (user.getCustomerList() == null) {
            user.setCustomerList(new ArrayList<Customer>());
        }
        if (user.getPurchaseorderList() == null) {
            user.setPurchaseorderList(new ArrayList<Purchaseorder>());
        }
        if (user.getCustomertypeList() == null) {
            user.setCustomertypeList(new ArrayList<Customertype>());
        }
        if (user.getGrnList() == null) {
            user.setGrnList(new ArrayList<Grn>());
        }
        if (user.getLoginList() == null) {
            user.setLoginList(new ArrayList<Login>());
        }
        if (user.getSupplierList() == null) {
            user.setSupplierList(new ArrayList<Supplier>());
        }
        if (user.getUserHasActionList() == null) {
            user.setUserHasActionList(new ArrayList<UserHasAction>());
        }
        if (user.getAgentList() == null) {
            user.setAgentList(new ArrayList<Agent>());
        }
        if (user.getDiscountList() == null) {
            user.setDiscountList(new ArrayList<Discount>());
        }
        if (user.getCategoryList() == null) {
            user.setCategoryList(new ArrayList<Category>());
        }
        if (user.getTaxList() == null) {
            user.setTaxList(new ArrayList<Tax>());
        }
        if (user.getBatchList() == null) {
            user.setBatchList(new ArrayList<Batch>());
        }
        if (user.getSubcategoryList() == null) {
            user.setSubcategoryList(new ArrayList<Subcategory>());
        }
        if (user.getItemList() == null) {
            user.setItemList(new ArrayList<Item>());
        }
        if (user.getBrandList() == null) {
            user.setBrandList(new ArrayList<Brand>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usertype userTypeid = user.getUserTypeid();
            if (userTypeid != null) {
                userTypeid = em.getReference(userTypeid.getClass(), userTypeid.getId());
                user.setUserTypeid(userTypeid);
            }
            List<Invoice> attachedInvoiceList = new ArrayList<Invoice>();
            for (Invoice invoiceListInvoiceToAttach : user.getInvoiceList()) {
                invoiceListInvoiceToAttach = em.getReference(invoiceListInvoiceToAttach.getClass(), invoiceListInvoiceToAttach.getId());
                attachedInvoiceList.add(invoiceListInvoiceToAttach);
            }
            user.setInvoiceList(attachedInvoiceList);
            List<Customer> attachedCustomerList = new ArrayList<Customer>();
            for (Customer customerListCustomerToAttach : user.getCustomerList()) {
                customerListCustomerToAttach = em.getReference(customerListCustomerToAttach.getClass(), customerListCustomerToAttach.getId());
                attachedCustomerList.add(customerListCustomerToAttach);
            }
            user.setCustomerList(attachedCustomerList);
            List<Purchaseorder> attachedPurchaseorderList = new ArrayList<Purchaseorder>();
            for (Purchaseorder purchaseorderListPurchaseorderToAttach : user.getPurchaseorderList()) {
                purchaseorderListPurchaseorderToAttach = em.getReference(purchaseorderListPurchaseorderToAttach.getClass(), purchaseorderListPurchaseorderToAttach.getId());
                attachedPurchaseorderList.add(purchaseorderListPurchaseorderToAttach);
            }
            user.setPurchaseorderList(attachedPurchaseorderList);
            List<Customertype> attachedCustomertypeList = new ArrayList<Customertype>();
            for (Customertype customertypeListCustomertypeToAttach : user.getCustomertypeList()) {
                customertypeListCustomertypeToAttach = em.getReference(customertypeListCustomertypeToAttach.getClass(), customertypeListCustomertypeToAttach.getId());
                attachedCustomertypeList.add(customertypeListCustomertypeToAttach);
            }
            user.setCustomertypeList(attachedCustomertypeList);
            List<Grn> attachedGrnList = new ArrayList<Grn>();
            for (Grn grnListGrnToAttach : user.getGrnList()) {
                grnListGrnToAttach = em.getReference(grnListGrnToAttach.getClass(), grnListGrnToAttach.getId());
                attachedGrnList.add(grnListGrnToAttach);
            }
            user.setGrnList(attachedGrnList);
            List<Login> attachedLoginList = new ArrayList<Login>();
            for (Login loginListLoginToAttach : user.getLoginList()) {
                loginListLoginToAttach = em.getReference(loginListLoginToAttach.getClass(), loginListLoginToAttach.getId());
                attachedLoginList.add(loginListLoginToAttach);
            }
            user.setLoginList(attachedLoginList);
            List<Supplier> attachedSupplierList = new ArrayList<Supplier>();
            for (Supplier supplierListSupplierToAttach : user.getSupplierList()) {
                supplierListSupplierToAttach = em.getReference(supplierListSupplierToAttach.getClass(), supplierListSupplierToAttach.getId());
                attachedSupplierList.add(supplierListSupplierToAttach);
            }
            user.setSupplierList(attachedSupplierList);
            List<UserHasAction> attachedUserHasActionList = new ArrayList<UserHasAction>();
            for (UserHasAction userHasActionListUserHasActionToAttach : user.getUserHasActionList()) {
                userHasActionListUserHasActionToAttach = em.getReference(userHasActionListUserHasActionToAttach.getClass(), userHasActionListUserHasActionToAttach.getId());
                attachedUserHasActionList.add(userHasActionListUserHasActionToAttach);
            }
            user.setUserHasActionList(attachedUserHasActionList);
            List<Agent> attachedAgentList = new ArrayList<Agent>();
            for (Agent agentListAgentToAttach : user.getAgentList()) {
                agentListAgentToAttach = em.getReference(agentListAgentToAttach.getClass(), agentListAgentToAttach.getId());
                attachedAgentList.add(agentListAgentToAttach);
            }
            user.setAgentList(attachedAgentList);
            List<Discount> attachedDiscountList = new ArrayList<Discount>();
            for (Discount discountListDiscountToAttach : user.getDiscountList()) {
                discountListDiscountToAttach = em.getReference(discountListDiscountToAttach.getClass(), discountListDiscountToAttach.getId());
                attachedDiscountList.add(discountListDiscountToAttach);
            }
            user.setDiscountList(attachedDiscountList);
            List<Category> attachedCategoryList = new ArrayList<Category>();
            for (Category categoryListCategoryToAttach : user.getCategoryList()) {
                categoryListCategoryToAttach = em.getReference(categoryListCategoryToAttach.getClass(), categoryListCategoryToAttach.getId());
                attachedCategoryList.add(categoryListCategoryToAttach);
            }
            user.setCategoryList(attachedCategoryList);
            List<Tax> attachedTaxList = new ArrayList<Tax>();
            for (Tax taxListTaxToAttach : user.getTaxList()) {
                taxListTaxToAttach = em.getReference(taxListTaxToAttach.getClass(), taxListTaxToAttach.getId());
                attachedTaxList.add(taxListTaxToAttach);
            }
            user.setTaxList(attachedTaxList);
            List<Batch> attachedBatchList = new ArrayList<Batch>();
            for (Batch batchListBatchToAttach : user.getBatchList()) {
                batchListBatchToAttach = em.getReference(batchListBatchToAttach.getClass(), batchListBatchToAttach.getId());
                attachedBatchList.add(batchListBatchToAttach);
            }
            user.setBatchList(attachedBatchList);
            List<Subcategory> attachedSubcategoryList = new ArrayList<Subcategory>();
            for (Subcategory subcategoryListSubcategoryToAttach : user.getSubcategoryList()) {
                subcategoryListSubcategoryToAttach = em.getReference(subcategoryListSubcategoryToAttach.getClass(), subcategoryListSubcategoryToAttach.getId());
                attachedSubcategoryList.add(subcategoryListSubcategoryToAttach);
            }
            user.setSubcategoryList(attachedSubcategoryList);
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : user.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getId());
                attachedItemList.add(itemListItemToAttach);
            }
            user.setItemList(attachedItemList);
            List<Brand> attachedBrandList = new ArrayList<Brand>();
            for (Brand brandListBrandToAttach : user.getBrandList()) {
                brandListBrandToAttach = em.getReference(brandListBrandToAttach.getClass(), brandListBrandToAttach.getId());
                attachedBrandList.add(brandListBrandToAttach);
            }
            user.setBrandList(attachedBrandList);
            em.persist(user);
            if (userTypeid != null) {
                userTypeid.getUserList().add(user);
                userTypeid = em.merge(userTypeid);
            }
            for (Invoice invoiceListInvoice : user.getInvoiceList()) {
                User oldUseridOfInvoiceListInvoice = invoiceListInvoice.getUserid();
                invoiceListInvoice.setUserid(user);
                invoiceListInvoice = em.merge(invoiceListInvoice);
                if (oldUseridOfInvoiceListInvoice != null) {
                    oldUseridOfInvoiceListInvoice.getInvoiceList().remove(invoiceListInvoice);
                    oldUseridOfInvoiceListInvoice = em.merge(oldUseridOfInvoiceListInvoice);
                }
            }
            for (Customer customerListCustomer : user.getCustomerList()) {
                User oldUseridOfCustomerListCustomer = customerListCustomer.getUserid();
                customerListCustomer.setUserid(user);
                customerListCustomer = em.merge(customerListCustomer);
                if (oldUseridOfCustomerListCustomer != null) {
                    oldUseridOfCustomerListCustomer.getCustomerList().remove(customerListCustomer);
                    oldUseridOfCustomerListCustomer = em.merge(oldUseridOfCustomerListCustomer);
                }
            }
            for (Purchaseorder purchaseorderListPurchaseorder : user.getPurchaseorderList()) {
                User oldUseridOfPurchaseorderListPurchaseorder = purchaseorderListPurchaseorder.getUserid();
                purchaseorderListPurchaseorder.setUserid(user);
                purchaseorderListPurchaseorder = em.merge(purchaseorderListPurchaseorder);
                if (oldUseridOfPurchaseorderListPurchaseorder != null) {
                    oldUseridOfPurchaseorderListPurchaseorder.getPurchaseorderList().remove(purchaseorderListPurchaseorder);
                    oldUseridOfPurchaseorderListPurchaseorder = em.merge(oldUseridOfPurchaseorderListPurchaseorder);
                }
            }
            for (Customertype customertypeListCustomertype : user.getCustomertypeList()) {
                User oldUseridOfCustomertypeListCustomertype = customertypeListCustomertype.getUserid();
                customertypeListCustomertype.setUserid(user);
                customertypeListCustomertype = em.merge(customertypeListCustomertype);
                if (oldUseridOfCustomertypeListCustomertype != null) {
                    oldUseridOfCustomertypeListCustomertype.getCustomertypeList().remove(customertypeListCustomertype);
                    oldUseridOfCustomertypeListCustomertype = em.merge(oldUseridOfCustomertypeListCustomertype);
                }
            }
            for (Grn grnListGrn : user.getGrnList()) {
                User oldUseridOfGrnListGrn = grnListGrn.getUserid();
                grnListGrn.setUserid(user);
                grnListGrn = em.merge(grnListGrn);
                if (oldUseridOfGrnListGrn != null) {
                    oldUseridOfGrnListGrn.getGrnList().remove(grnListGrn);
                    oldUseridOfGrnListGrn = em.merge(oldUseridOfGrnListGrn);
                }
            }
            for (Login loginListLogin : user.getLoginList()) {
                User oldUseridOfLoginListLogin = loginListLogin.getUserid();
                loginListLogin.setUserid(user);
                loginListLogin = em.merge(loginListLogin);
                if (oldUseridOfLoginListLogin != null) {
                    oldUseridOfLoginListLogin.getLoginList().remove(loginListLogin);
                    oldUseridOfLoginListLogin = em.merge(oldUseridOfLoginListLogin);
                }
            }
            for (Supplier supplierListSupplier : user.getSupplierList()) {
                User oldUseridOfSupplierListSupplier = supplierListSupplier.getUserid();
                supplierListSupplier.setUserid(user);
                supplierListSupplier = em.merge(supplierListSupplier);
                if (oldUseridOfSupplierListSupplier != null) {
                    oldUseridOfSupplierListSupplier.getSupplierList().remove(supplierListSupplier);
                    oldUseridOfSupplierListSupplier = em.merge(oldUseridOfSupplierListSupplier);
                }
            }
            for (UserHasAction userHasActionListUserHasAction : user.getUserHasActionList()) {
                User oldUseridOfUserHasActionListUserHasAction = userHasActionListUserHasAction.getUserid();
                userHasActionListUserHasAction.setUserid(user);
                userHasActionListUserHasAction = em.merge(userHasActionListUserHasAction);
                if (oldUseridOfUserHasActionListUserHasAction != null) {
                    oldUseridOfUserHasActionListUserHasAction.getUserHasActionList().remove(userHasActionListUserHasAction);
                    oldUseridOfUserHasActionListUserHasAction = em.merge(oldUseridOfUserHasActionListUserHasAction);
                }
            }
            for (Agent agentListAgent : user.getAgentList()) {
                User oldUseridOfAgentListAgent = agentListAgent.getUserid();
                agentListAgent.setUserid(user);
                agentListAgent = em.merge(agentListAgent);
                if (oldUseridOfAgentListAgent != null) {
                    oldUseridOfAgentListAgent.getAgentList().remove(agentListAgent);
                    oldUseridOfAgentListAgent = em.merge(oldUseridOfAgentListAgent);
                }
            }
            for (Discount discountListDiscount : user.getDiscountList()) {
                User oldUseridOfDiscountListDiscount = discountListDiscount.getUserid();
                discountListDiscount.setUserid(user);
                discountListDiscount = em.merge(discountListDiscount);
                if (oldUseridOfDiscountListDiscount != null) {
                    oldUseridOfDiscountListDiscount.getDiscountList().remove(discountListDiscount);
                    oldUseridOfDiscountListDiscount = em.merge(oldUseridOfDiscountListDiscount);
                }
            }
            for (Category categoryListCategory : user.getCategoryList()) {
                User oldUseridOfCategoryListCategory = categoryListCategory.getUserid();
                categoryListCategory.setUserid(user);
                categoryListCategory = em.merge(categoryListCategory);
                if (oldUseridOfCategoryListCategory != null) {
                    oldUseridOfCategoryListCategory.getCategoryList().remove(categoryListCategory);
                    oldUseridOfCategoryListCategory = em.merge(oldUseridOfCategoryListCategory);
                }
            }
            for (Tax taxListTax : user.getTaxList()) {
                User oldUseridOfTaxListTax = taxListTax.getUserid();
                taxListTax.setUserid(user);
                taxListTax = em.merge(taxListTax);
                if (oldUseridOfTaxListTax != null) {
                    oldUseridOfTaxListTax.getTaxList().remove(taxListTax);
                    oldUseridOfTaxListTax = em.merge(oldUseridOfTaxListTax);
                }
            }
            for (Batch batchListBatch : user.getBatchList()) {
                User oldUseridOfBatchListBatch = batchListBatch.getUserid();
                batchListBatch.setUserid(user);
                batchListBatch = em.merge(batchListBatch);
                if (oldUseridOfBatchListBatch != null) {
                    oldUseridOfBatchListBatch.getBatchList().remove(batchListBatch);
                    oldUseridOfBatchListBatch = em.merge(oldUseridOfBatchListBatch);
                }
            }
            for (Subcategory subcategoryListSubcategory : user.getSubcategoryList()) {
                User oldUseridOfSubcategoryListSubcategory = subcategoryListSubcategory.getUserid();
                subcategoryListSubcategory.setUserid(user);
                subcategoryListSubcategory = em.merge(subcategoryListSubcategory);
                if (oldUseridOfSubcategoryListSubcategory != null) {
                    oldUseridOfSubcategoryListSubcategory.getSubcategoryList().remove(subcategoryListSubcategory);
                    oldUseridOfSubcategoryListSubcategory = em.merge(oldUseridOfSubcategoryListSubcategory);
                }
            }
            for (Item itemListItem : user.getItemList()) {
                User oldUseridOfItemListItem = itemListItem.getUserid();
                itemListItem.setUserid(user);
                itemListItem = em.merge(itemListItem);
                if (oldUseridOfItemListItem != null) {
                    oldUseridOfItemListItem.getItemList().remove(itemListItem);
                    oldUseridOfItemListItem = em.merge(oldUseridOfItemListItem);
                }
            }
            for (Brand brandListBrand : user.getBrandList()) {
                User oldUseridOfBrandListBrand = brandListBrand.getUserid();
                brandListBrand.setUserid(user);
                brandListBrand = em.merge(brandListBrand);
                if (oldUseridOfBrandListBrand != null) {
                    oldUseridOfBrandListBrand.getBrandList().remove(brandListBrand);
                    oldUseridOfBrandListBrand = em.merge(oldUseridOfBrandListBrand);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUser(user.getId()) != null) {
                throw new PreexistingEntityException("User " + user + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            Usertype userTypeidOld = persistentUser.getUserTypeid();
            Usertype userTypeidNew = user.getUserTypeid();
            List<Invoice> invoiceListOld = persistentUser.getInvoiceList();
            List<Invoice> invoiceListNew = user.getInvoiceList();
            List<Customer> customerListOld = persistentUser.getCustomerList();
            List<Customer> customerListNew = user.getCustomerList();
            List<Purchaseorder> purchaseorderListOld = persistentUser.getPurchaseorderList();
            List<Purchaseorder> purchaseorderListNew = user.getPurchaseorderList();
            List<Customertype> customertypeListOld = persistentUser.getCustomertypeList();
            List<Customertype> customertypeListNew = user.getCustomertypeList();
            List<Grn> grnListOld = persistentUser.getGrnList();
            List<Grn> grnListNew = user.getGrnList();
            List<Login> loginListOld = persistentUser.getLoginList();
            List<Login> loginListNew = user.getLoginList();
            List<Supplier> supplierListOld = persistentUser.getSupplierList();
            List<Supplier> supplierListNew = user.getSupplierList();
            List<UserHasAction> userHasActionListOld = persistentUser.getUserHasActionList();
            List<UserHasAction> userHasActionListNew = user.getUserHasActionList();
            List<Agent> agentListOld = persistentUser.getAgentList();
            List<Agent> agentListNew = user.getAgentList();
            List<Discount> discountListOld = persistentUser.getDiscountList();
            List<Discount> discountListNew = user.getDiscountList();
            List<Category> categoryListOld = persistentUser.getCategoryList();
            List<Category> categoryListNew = user.getCategoryList();
            List<Tax> taxListOld = persistentUser.getTaxList();
            List<Tax> taxListNew = user.getTaxList();
            List<Batch> batchListOld = persistentUser.getBatchList();
            List<Batch> batchListNew = user.getBatchList();
            List<Subcategory> subcategoryListOld = persistentUser.getSubcategoryList();
            List<Subcategory> subcategoryListNew = user.getSubcategoryList();
            List<Item> itemListOld = persistentUser.getItemList();
            List<Item> itemListNew = user.getItemList();
            List<Brand> brandListOld = persistentUser.getBrandList();
            List<Brand> brandListNew = user.getBrandList();
            List<String> illegalOrphanMessages = null;
            for (Invoice invoiceListOldInvoice : invoiceListOld) {
                if (!invoiceListNew.contains(invoiceListOldInvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Invoice " + invoiceListOldInvoice + " since its userid field is not nullable.");
                }
            }
            for (Customer customerListOldCustomer : customerListOld) {
                if (!customerListNew.contains(customerListOldCustomer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Customer " + customerListOldCustomer + " since its userid field is not nullable.");
                }
            }
            for (Purchaseorder purchaseorderListOldPurchaseorder : purchaseorderListOld) {
                if (!purchaseorderListNew.contains(purchaseorderListOldPurchaseorder)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Purchaseorder " + purchaseorderListOldPurchaseorder + " since its userid field is not nullable.");
                }
            }
            for (Customertype customertypeListOldCustomertype : customertypeListOld) {
                if (!customertypeListNew.contains(customertypeListOldCustomertype)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Customertype " + customertypeListOldCustomertype + " since its userid field is not nullable.");
                }
            }
            for (Grn grnListOldGrn : grnListOld) {
                if (!grnListNew.contains(grnListOldGrn)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Grn " + grnListOldGrn + " since its userid field is not nullable.");
                }
            }
            for (Login loginListOldLogin : loginListOld) {
                if (!loginListNew.contains(loginListOldLogin)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Login " + loginListOldLogin + " since its userid field is not nullable.");
                }
            }
            for (Supplier supplierListOldSupplier : supplierListOld) {
                if (!supplierListNew.contains(supplierListOldSupplier)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Supplier " + supplierListOldSupplier + " since its userid field is not nullable.");
                }
            }
            for (UserHasAction userHasActionListOldUserHasAction : userHasActionListOld) {
                if (!userHasActionListNew.contains(userHasActionListOldUserHasAction)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserHasAction " + userHasActionListOldUserHasAction + " since its userid field is not nullable.");
                }
            }
            for (Agent agentListOldAgent : agentListOld) {
                if (!agentListNew.contains(agentListOldAgent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agent " + agentListOldAgent + " since its userid field is not nullable.");
                }
            }
            for (Discount discountListOldDiscount : discountListOld) {
                if (!discountListNew.contains(discountListOldDiscount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Discount " + discountListOldDiscount + " since its userid field is not nullable.");
                }
            }
            for (Category categoryListOldCategory : categoryListOld) {
                if (!categoryListNew.contains(categoryListOldCategory)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Category " + categoryListOldCategory + " since its userid field is not nullable.");
                }
            }
            for (Tax taxListOldTax : taxListOld) {
                if (!taxListNew.contains(taxListOldTax)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tax " + taxListOldTax + " since its userid field is not nullable.");
                }
            }
            for (Batch batchListOldBatch : batchListOld) {
                if (!batchListNew.contains(batchListOldBatch)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Batch " + batchListOldBatch + " since its userid field is not nullable.");
                }
            }
            for (Subcategory subcategoryListOldSubcategory : subcategoryListOld) {
                if (!subcategoryListNew.contains(subcategoryListOldSubcategory)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Subcategory " + subcategoryListOldSubcategory + " since its userid field is not nullable.");
                }
            }
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Item " + itemListOldItem + " since its userid field is not nullable.");
                }
            }
            for (Brand brandListOldBrand : brandListOld) {
                if (!brandListNew.contains(brandListOldBrand)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Brand " + brandListOldBrand + " since its userid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userTypeidNew != null) {
                userTypeidNew = em.getReference(userTypeidNew.getClass(), userTypeidNew.getId());
                user.setUserTypeid(userTypeidNew);
            }
            List<Invoice> attachedInvoiceListNew = new ArrayList<Invoice>();
            for (Invoice invoiceListNewInvoiceToAttach : invoiceListNew) {
                invoiceListNewInvoiceToAttach = em.getReference(invoiceListNewInvoiceToAttach.getClass(), invoiceListNewInvoiceToAttach.getId());
                attachedInvoiceListNew.add(invoiceListNewInvoiceToAttach);
            }
            invoiceListNew = attachedInvoiceListNew;
            user.setInvoiceList(invoiceListNew);
            List<Customer> attachedCustomerListNew = new ArrayList<Customer>();
            for (Customer customerListNewCustomerToAttach : customerListNew) {
                customerListNewCustomerToAttach = em.getReference(customerListNewCustomerToAttach.getClass(), customerListNewCustomerToAttach.getId());
                attachedCustomerListNew.add(customerListNewCustomerToAttach);
            }
            customerListNew = attachedCustomerListNew;
            user.setCustomerList(customerListNew);
            List<Purchaseorder> attachedPurchaseorderListNew = new ArrayList<Purchaseorder>();
            for (Purchaseorder purchaseorderListNewPurchaseorderToAttach : purchaseorderListNew) {
                purchaseorderListNewPurchaseorderToAttach = em.getReference(purchaseorderListNewPurchaseorderToAttach.getClass(), purchaseorderListNewPurchaseorderToAttach.getId());
                attachedPurchaseorderListNew.add(purchaseorderListNewPurchaseorderToAttach);
            }
            purchaseorderListNew = attachedPurchaseorderListNew;
            user.setPurchaseorderList(purchaseorderListNew);
            List<Customertype> attachedCustomertypeListNew = new ArrayList<Customertype>();
            for (Customertype customertypeListNewCustomertypeToAttach : customertypeListNew) {
                customertypeListNewCustomertypeToAttach = em.getReference(customertypeListNewCustomertypeToAttach.getClass(), customertypeListNewCustomertypeToAttach.getId());
                attachedCustomertypeListNew.add(customertypeListNewCustomertypeToAttach);
            }
            customertypeListNew = attachedCustomertypeListNew;
            user.setCustomertypeList(customertypeListNew);
            List<Grn> attachedGrnListNew = new ArrayList<Grn>();
            for (Grn grnListNewGrnToAttach : grnListNew) {
                grnListNewGrnToAttach = em.getReference(grnListNewGrnToAttach.getClass(), grnListNewGrnToAttach.getId());
                attachedGrnListNew.add(grnListNewGrnToAttach);
            }
            grnListNew = attachedGrnListNew;
            user.setGrnList(grnListNew);
            List<Login> attachedLoginListNew = new ArrayList<Login>();
            for (Login loginListNewLoginToAttach : loginListNew) {
                loginListNewLoginToAttach = em.getReference(loginListNewLoginToAttach.getClass(), loginListNewLoginToAttach.getId());
                attachedLoginListNew.add(loginListNewLoginToAttach);
            }
            loginListNew = attachedLoginListNew;
            user.setLoginList(loginListNew);
            List<Supplier> attachedSupplierListNew = new ArrayList<Supplier>();
            for (Supplier supplierListNewSupplierToAttach : supplierListNew) {
                supplierListNewSupplierToAttach = em.getReference(supplierListNewSupplierToAttach.getClass(), supplierListNewSupplierToAttach.getId());
                attachedSupplierListNew.add(supplierListNewSupplierToAttach);
            }
            supplierListNew = attachedSupplierListNew;
            user.setSupplierList(supplierListNew);
            List<UserHasAction> attachedUserHasActionListNew = new ArrayList<UserHasAction>();
            for (UserHasAction userHasActionListNewUserHasActionToAttach : userHasActionListNew) {
                userHasActionListNewUserHasActionToAttach = em.getReference(userHasActionListNewUserHasActionToAttach.getClass(), userHasActionListNewUserHasActionToAttach.getId());
                attachedUserHasActionListNew.add(userHasActionListNewUserHasActionToAttach);
            }
            userHasActionListNew = attachedUserHasActionListNew;
            user.setUserHasActionList(userHasActionListNew);
            List<Agent> attachedAgentListNew = new ArrayList<Agent>();
            for (Agent agentListNewAgentToAttach : agentListNew) {
                agentListNewAgentToAttach = em.getReference(agentListNewAgentToAttach.getClass(), agentListNewAgentToAttach.getId());
                attachedAgentListNew.add(agentListNewAgentToAttach);
            }
            agentListNew = attachedAgentListNew;
            user.setAgentList(agentListNew);
            List<Discount> attachedDiscountListNew = new ArrayList<Discount>();
            for (Discount discountListNewDiscountToAttach : discountListNew) {
                discountListNewDiscountToAttach = em.getReference(discountListNewDiscountToAttach.getClass(), discountListNewDiscountToAttach.getId());
                attachedDiscountListNew.add(discountListNewDiscountToAttach);
            }
            discountListNew = attachedDiscountListNew;
            user.setDiscountList(discountListNew);
            List<Category> attachedCategoryListNew = new ArrayList<Category>();
            for (Category categoryListNewCategoryToAttach : categoryListNew) {
                categoryListNewCategoryToAttach = em.getReference(categoryListNewCategoryToAttach.getClass(), categoryListNewCategoryToAttach.getId());
                attachedCategoryListNew.add(categoryListNewCategoryToAttach);
            }
            categoryListNew = attachedCategoryListNew;
            user.setCategoryList(categoryListNew);
            List<Tax> attachedTaxListNew = new ArrayList<Tax>();
            for (Tax taxListNewTaxToAttach : taxListNew) {
                taxListNewTaxToAttach = em.getReference(taxListNewTaxToAttach.getClass(), taxListNewTaxToAttach.getId());
                attachedTaxListNew.add(taxListNewTaxToAttach);
            }
            taxListNew = attachedTaxListNew;
            user.setTaxList(taxListNew);
            List<Batch> attachedBatchListNew = new ArrayList<Batch>();
            for (Batch batchListNewBatchToAttach : batchListNew) {
                batchListNewBatchToAttach = em.getReference(batchListNewBatchToAttach.getClass(), batchListNewBatchToAttach.getId());
                attachedBatchListNew.add(batchListNewBatchToAttach);
            }
            batchListNew = attachedBatchListNew;
            user.setBatchList(batchListNew);
            List<Subcategory> attachedSubcategoryListNew = new ArrayList<Subcategory>();
            for (Subcategory subcategoryListNewSubcategoryToAttach : subcategoryListNew) {
                subcategoryListNewSubcategoryToAttach = em.getReference(subcategoryListNewSubcategoryToAttach.getClass(), subcategoryListNewSubcategoryToAttach.getId());
                attachedSubcategoryListNew.add(subcategoryListNewSubcategoryToAttach);
            }
            subcategoryListNew = attachedSubcategoryListNew;
            user.setSubcategoryList(subcategoryListNew);
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getId());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            user.setItemList(itemListNew);
            List<Brand> attachedBrandListNew = new ArrayList<Brand>();
            for (Brand brandListNewBrandToAttach : brandListNew) {
                brandListNewBrandToAttach = em.getReference(brandListNewBrandToAttach.getClass(), brandListNewBrandToAttach.getId());
                attachedBrandListNew.add(brandListNewBrandToAttach);
            }
            brandListNew = attachedBrandListNew;
            user.setBrandList(brandListNew);
            user = em.merge(user);
            if (userTypeidOld != null && !userTypeidOld.equals(userTypeidNew)) {
                userTypeidOld.getUserList().remove(user);
                userTypeidOld = em.merge(userTypeidOld);
            }
            if (userTypeidNew != null && !userTypeidNew.equals(userTypeidOld)) {
                userTypeidNew.getUserList().add(user);
                userTypeidNew = em.merge(userTypeidNew);
            }
            for (Invoice invoiceListNewInvoice : invoiceListNew) {
                if (!invoiceListOld.contains(invoiceListNewInvoice)) {
                    User oldUseridOfInvoiceListNewInvoice = invoiceListNewInvoice.getUserid();
                    invoiceListNewInvoice.setUserid(user);
                    invoiceListNewInvoice = em.merge(invoiceListNewInvoice);
                    if (oldUseridOfInvoiceListNewInvoice != null && !oldUseridOfInvoiceListNewInvoice.equals(user)) {
                        oldUseridOfInvoiceListNewInvoice.getInvoiceList().remove(invoiceListNewInvoice);
                        oldUseridOfInvoiceListNewInvoice = em.merge(oldUseridOfInvoiceListNewInvoice);
                    }
                }
            }
            for (Customer customerListNewCustomer : customerListNew) {
                if (!customerListOld.contains(customerListNewCustomer)) {
                    User oldUseridOfCustomerListNewCustomer = customerListNewCustomer.getUserid();
                    customerListNewCustomer.setUserid(user);
                    customerListNewCustomer = em.merge(customerListNewCustomer);
                    if (oldUseridOfCustomerListNewCustomer != null && !oldUseridOfCustomerListNewCustomer.equals(user)) {
                        oldUseridOfCustomerListNewCustomer.getCustomerList().remove(customerListNewCustomer);
                        oldUseridOfCustomerListNewCustomer = em.merge(oldUseridOfCustomerListNewCustomer);
                    }
                }
            }
            for (Purchaseorder purchaseorderListNewPurchaseorder : purchaseorderListNew) {
                if (!purchaseorderListOld.contains(purchaseorderListNewPurchaseorder)) {
                    User oldUseridOfPurchaseorderListNewPurchaseorder = purchaseorderListNewPurchaseorder.getUserid();
                    purchaseorderListNewPurchaseorder.setUserid(user);
                    purchaseorderListNewPurchaseorder = em.merge(purchaseorderListNewPurchaseorder);
                    if (oldUseridOfPurchaseorderListNewPurchaseorder != null && !oldUseridOfPurchaseorderListNewPurchaseorder.equals(user)) {
                        oldUseridOfPurchaseorderListNewPurchaseorder.getPurchaseorderList().remove(purchaseorderListNewPurchaseorder);
                        oldUseridOfPurchaseorderListNewPurchaseorder = em.merge(oldUseridOfPurchaseorderListNewPurchaseorder);
                    }
                }
            }
            for (Customertype customertypeListNewCustomertype : customertypeListNew) {
                if (!customertypeListOld.contains(customertypeListNewCustomertype)) {
                    User oldUseridOfCustomertypeListNewCustomertype = customertypeListNewCustomertype.getUserid();
                    customertypeListNewCustomertype.setUserid(user);
                    customertypeListNewCustomertype = em.merge(customertypeListNewCustomertype);
                    if (oldUseridOfCustomertypeListNewCustomertype != null && !oldUseridOfCustomertypeListNewCustomertype.equals(user)) {
                        oldUseridOfCustomertypeListNewCustomertype.getCustomertypeList().remove(customertypeListNewCustomertype);
                        oldUseridOfCustomertypeListNewCustomertype = em.merge(oldUseridOfCustomertypeListNewCustomertype);
                    }
                }
            }
            for (Grn grnListNewGrn : grnListNew) {
                if (!grnListOld.contains(grnListNewGrn)) {
                    User oldUseridOfGrnListNewGrn = grnListNewGrn.getUserid();
                    grnListNewGrn.setUserid(user);
                    grnListNewGrn = em.merge(grnListNewGrn);
                    if (oldUseridOfGrnListNewGrn != null && !oldUseridOfGrnListNewGrn.equals(user)) {
                        oldUseridOfGrnListNewGrn.getGrnList().remove(grnListNewGrn);
                        oldUseridOfGrnListNewGrn = em.merge(oldUseridOfGrnListNewGrn);
                    }
                }
            }
            for (Login loginListNewLogin : loginListNew) {
                if (!loginListOld.contains(loginListNewLogin)) {
                    User oldUseridOfLoginListNewLogin = loginListNewLogin.getUserid();
                    loginListNewLogin.setUserid(user);
                    loginListNewLogin = em.merge(loginListNewLogin);
                    if (oldUseridOfLoginListNewLogin != null && !oldUseridOfLoginListNewLogin.equals(user)) {
                        oldUseridOfLoginListNewLogin.getLoginList().remove(loginListNewLogin);
                        oldUseridOfLoginListNewLogin = em.merge(oldUseridOfLoginListNewLogin);
                    }
                }
            }
            for (Supplier supplierListNewSupplier : supplierListNew) {
                if (!supplierListOld.contains(supplierListNewSupplier)) {
                    User oldUseridOfSupplierListNewSupplier = supplierListNewSupplier.getUserid();
                    supplierListNewSupplier.setUserid(user);
                    supplierListNewSupplier = em.merge(supplierListNewSupplier);
                    if (oldUseridOfSupplierListNewSupplier != null && !oldUseridOfSupplierListNewSupplier.equals(user)) {
                        oldUseridOfSupplierListNewSupplier.getSupplierList().remove(supplierListNewSupplier);
                        oldUseridOfSupplierListNewSupplier = em.merge(oldUseridOfSupplierListNewSupplier);
                    }
                }
            }
            for (UserHasAction userHasActionListNewUserHasAction : userHasActionListNew) {
                if (!userHasActionListOld.contains(userHasActionListNewUserHasAction)) {
                    User oldUseridOfUserHasActionListNewUserHasAction = userHasActionListNewUserHasAction.getUserid();
                    userHasActionListNewUserHasAction.setUserid(user);
                    userHasActionListNewUserHasAction = em.merge(userHasActionListNewUserHasAction);
                    if (oldUseridOfUserHasActionListNewUserHasAction != null && !oldUseridOfUserHasActionListNewUserHasAction.equals(user)) {
                        oldUseridOfUserHasActionListNewUserHasAction.getUserHasActionList().remove(userHasActionListNewUserHasAction);
                        oldUseridOfUserHasActionListNewUserHasAction = em.merge(oldUseridOfUserHasActionListNewUserHasAction);
                    }
                }
            }
            for (Agent agentListNewAgent : agentListNew) {
                if (!agentListOld.contains(agentListNewAgent)) {
                    User oldUseridOfAgentListNewAgent = agentListNewAgent.getUserid();
                    agentListNewAgent.setUserid(user);
                    agentListNewAgent = em.merge(agentListNewAgent);
                    if (oldUseridOfAgentListNewAgent != null && !oldUseridOfAgentListNewAgent.equals(user)) {
                        oldUseridOfAgentListNewAgent.getAgentList().remove(agentListNewAgent);
                        oldUseridOfAgentListNewAgent = em.merge(oldUseridOfAgentListNewAgent);
                    }
                }
            }
            for (Discount discountListNewDiscount : discountListNew) {
                if (!discountListOld.contains(discountListNewDiscount)) {
                    User oldUseridOfDiscountListNewDiscount = discountListNewDiscount.getUserid();
                    discountListNewDiscount.setUserid(user);
                    discountListNewDiscount = em.merge(discountListNewDiscount);
                    if (oldUseridOfDiscountListNewDiscount != null && !oldUseridOfDiscountListNewDiscount.equals(user)) {
                        oldUseridOfDiscountListNewDiscount.getDiscountList().remove(discountListNewDiscount);
                        oldUseridOfDiscountListNewDiscount = em.merge(oldUseridOfDiscountListNewDiscount);
                    }
                }
            }
            for (Category categoryListNewCategory : categoryListNew) {
                if (!categoryListOld.contains(categoryListNewCategory)) {
                    User oldUseridOfCategoryListNewCategory = categoryListNewCategory.getUserid();
                    categoryListNewCategory.setUserid(user);
                    categoryListNewCategory = em.merge(categoryListNewCategory);
                    if (oldUseridOfCategoryListNewCategory != null && !oldUseridOfCategoryListNewCategory.equals(user)) {
                        oldUseridOfCategoryListNewCategory.getCategoryList().remove(categoryListNewCategory);
                        oldUseridOfCategoryListNewCategory = em.merge(oldUseridOfCategoryListNewCategory);
                    }
                }
            }
            for (Tax taxListNewTax : taxListNew) {
                if (!taxListOld.contains(taxListNewTax)) {
                    User oldUseridOfTaxListNewTax = taxListNewTax.getUserid();
                    taxListNewTax.setUserid(user);
                    taxListNewTax = em.merge(taxListNewTax);
                    if (oldUseridOfTaxListNewTax != null && !oldUseridOfTaxListNewTax.equals(user)) {
                        oldUseridOfTaxListNewTax.getTaxList().remove(taxListNewTax);
                        oldUseridOfTaxListNewTax = em.merge(oldUseridOfTaxListNewTax);
                    }
                }
            }
            for (Batch batchListNewBatch : batchListNew) {
                if (!batchListOld.contains(batchListNewBatch)) {
                    User oldUseridOfBatchListNewBatch = batchListNewBatch.getUserid();
                    batchListNewBatch.setUserid(user);
                    batchListNewBatch = em.merge(batchListNewBatch);
                    if (oldUseridOfBatchListNewBatch != null && !oldUseridOfBatchListNewBatch.equals(user)) {
                        oldUseridOfBatchListNewBatch.getBatchList().remove(batchListNewBatch);
                        oldUseridOfBatchListNewBatch = em.merge(oldUseridOfBatchListNewBatch);
                    }
                }
            }
            for (Subcategory subcategoryListNewSubcategory : subcategoryListNew) {
                if (!subcategoryListOld.contains(subcategoryListNewSubcategory)) {
                    User oldUseridOfSubcategoryListNewSubcategory = subcategoryListNewSubcategory.getUserid();
                    subcategoryListNewSubcategory.setUserid(user);
                    subcategoryListNewSubcategory = em.merge(subcategoryListNewSubcategory);
                    if (oldUseridOfSubcategoryListNewSubcategory != null && !oldUseridOfSubcategoryListNewSubcategory.equals(user)) {
                        oldUseridOfSubcategoryListNewSubcategory.getSubcategoryList().remove(subcategoryListNewSubcategory);
                        oldUseridOfSubcategoryListNewSubcategory = em.merge(oldUseridOfSubcategoryListNewSubcategory);
                    }
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    User oldUseridOfItemListNewItem = itemListNewItem.getUserid();
                    itemListNewItem.setUserid(user);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldUseridOfItemListNewItem != null && !oldUseridOfItemListNewItem.equals(user)) {
                        oldUseridOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldUseridOfItemListNewItem = em.merge(oldUseridOfItemListNewItem);
                    }
                }
            }
            for (Brand brandListNewBrand : brandListNew) {
                if (!brandListOld.contains(brandListNewBrand)) {
                    User oldUseridOfBrandListNewBrand = brandListNewBrand.getUserid();
                    brandListNewBrand.setUserid(user);
                    brandListNewBrand = em.merge(brandListNewBrand);
                    if (oldUseridOfBrandListNewBrand != null && !oldUseridOfBrandListNewBrand.equals(user)) {
                        oldUseridOfBrandListNewBrand.getBrandList().remove(brandListNewBrand);
                        oldUseridOfBrandListNewBrand = em.merge(oldUseridOfBrandListNewBrand);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Invoice> invoiceListOrphanCheck = user.getInvoiceList();
            for (Invoice invoiceListOrphanCheckInvoice : invoiceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Invoice " + invoiceListOrphanCheckInvoice + " in its invoiceList field has a non-nullable userid field.");
            }
            List<Customer> customerListOrphanCheck = user.getCustomerList();
            for (Customer customerListOrphanCheckCustomer : customerListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Customer " + customerListOrphanCheckCustomer + " in its customerList field has a non-nullable userid field.");
            }
            List<Purchaseorder> purchaseorderListOrphanCheck = user.getPurchaseorderList();
            for (Purchaseorder purchaseorderListOrphanCheckPurchaseorder : purchaseorderListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Purchaseorder " + purchaseorderListOrphanCheckPurchaseorder + " in its purchaseorderList field has a non-nullable userid field.");
            }
            List<Customertype> customertypeListOrphanCheck = user.getCustomertypeList();
            for (Customertype customertypeListOrphanCheckCustomertype : customertypeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Customertype " + customertypeListOrphanCheckCustomertype + " in its customertypeList field has a non-nullable userid field.");
            }
            List<Grn> grnListOrphanCheck = user.getGrnList();
            for (Grn grnListOrphanCheckGrn : grnListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Grn " + grnListOrphanCheckGrn + " in its grnList field has a non-nullable userid field.");
            }
            List<Login> loginListOrphanCheck = user.getLoginList();
            for (Login loginListOrphanCheckLogin : loginListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Login " + loginListOrphanCheckLogin + " in its loginList field has a non-nullable userid field.");
            }
            List<Supplier> supplierListOrphanCheck = user.getSupplierList();
            for (Supplier supplierListOrphanCheckSupplier : supplierListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Supplier " + supplierListOrphanCheckSupplier + " in its supplierList field has a non-nullable userid field.");
            }
            List<UserHasAction> userHasActionListOrphanCheck = user.getUserHasActionList();
            for (UserHasAction userHasActionListOrphanCheckUserHasAction : userHasActionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the UserHasAction " + userHasActionListOrphanCheckUserHasAction + " in its userHasActionList field has a non-nullable userid field.");
            }
            List<Agent> agentListOrphanCheck = user.getAgentList();
            for (Agent agentListOrphanCheckAgent : agentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Agent " + agentListOrphanCheckAgent + " in its agentList field has a non-nullable userid field.");
            }
            List<Discount> discountListOrphanCheck = user.getDiscountList();
            for (Discount discountListOrphanCheckDiscount : discountListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Discount " + discountListOrphanCheckDiscount + " in its discountList field has a non-nullable userid field.");
            }
            List<Category> categoryListOrphanCheck = user.getCategoryList();
            for (Category categoryListOrphanCheckCategory : categoryListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Category " + categoryListOrphanCheckCategory + " in its categoryList field has a non-nullable userid field.");
            }
            List<Tax> taxListOrphanCheck = user.getTaxList();
            for (Tax taxListOrphanCheckTax : taxListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Tax " + taxListOrphanCheckTax + " in its taxList field has a non-nullable userid field.");
            }
            List<Batch> batchListOrphanCheck = user.getBatchList();
            for (Batch batchListOrphanCheckBatch : batchListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Batch " + batchListOrphanCheckBatch + " in its batchList field has a non-nullable userid field.");
            }
            List<Subcategory> subcategoryListOrphanCheck = user.getSubcategoryList();
            for (Subcategory subcategoryListOrphanCheckSubcategory : subcategoryListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Subcategory " + subcategoryListOrphanCheckSubcategory + " in its subcategoryList field has a non-nullable userid field.");
            }
            List<Item> itemListOrphanCheck = user.getItemList();
            for (Item itemListOrphanCheckItem : itemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Item " + itemListOrphanCheckItem + " in its itemList field has a non-nullable userid field.");
            }
            List<Brand> brandListOrphanCheck = user.getBrandList();
            for (Brand brandListOrphanCheckBrand : brandListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Brand " + brandListOrphanCheckBrand + " in its brandList field has a non-nullable userid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usertype userTypeid = user.getUserTypeid();
            if (userTypeid != null) {
                userTypeid.getUserList().remove(user);
                userTypeid = em.merge(userTypeid);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
