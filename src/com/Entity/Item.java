/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Achi
 */
@Entity
@Table(catalog = "msdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i"),
    @NamedQuery(name = "Item.findById", query = "SELECT i FROM Item i WHERE i.id = :id"),
    @NamedQuery(name = "Item.findByItem", query = "SELECT i FROM Item i WHERE i.item = :item"),
    @NamedQuery(name = "Item.findByItemNo", query = "SELECT i FROM Item i WHERE i.itemNo = :itemNo"),
    @NamedQuery(name = "Item.findByImeiNo", query = "SELECT i FROM Item i WHERE i.imeiNo = :imeiNo"),
    @NamedQuery(name = "Item.findByColor", query = "SELECT i FROM Item i WHERE i.color = :color"),
    @NamedQuery(name = "Item.findByDate", query = "SELECT i FROM Item i WHERE i.date = :date"),
    @NamedQuery(name = "Item.findByStatus", query = "SELECT i FROM Item i WHERE i.status = :status")})
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    private String item;
    private String itemNo;
    private String imeiNo;
    private String color;
    @Lob
    private String desc;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    private boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemid")
    private List<PurchaseOrderHasItem> purchaseOrderHasItemList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemid")
    private List<ItemHasImage> itemHasImageList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemid")
    private List<Stock> stockList;
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userid;
    @JoinColumn(name = "SubCategory_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subcategory subCategoryid;
    @JoinColumn(name = "Category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Category categoryid;
    @JoinColumn(name = "Brand_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Brand brandid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemid")
    private List<GrnHasItem> grnHasItemList;

    public Item() {
    }

    public Item(Integer id) {
        this.id = id;
    }

    public Item(Integer id, boolean status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @XmlTransient
    public List<PurchaseOrderHasItem> getPurchaseOrderHasItemList() {
        return purchaseOrderHasItemList;
    }

    public void setPurchaseOrderHasItemList(List<PurchaseOrderHasItem> purchaseOrderHasItemList) {
        this.purchaseOrderHasItemList = purchaseOrderHasItemList;
    }

    @XmlTransient
    public List<ItemHasImage> getItemHasImageList() {
        return itemHasImageList;
    }

    public void setItemHasImageList(List<ItemHasImage> itemHasImageList) {
        this.itemHasImageList = itemHasImageList;
    }

    @XmlTransient
    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    public Subcategory getSubCategoryid() {
        return subCategoryid;
    }

    public void setSubCategoryid(Subcategory subCategoryid) {
        this.subCategoryid = subCategoryid;
    }

    public Category getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Category categoryid) {
        this.categoryid = categoryid;
    }

    public Brand getBrandid() {
        return brandid;
    }

    public void setBrandid(Brand brandid) {
        this.brandid = brandid;
    }

    @XmlTransient
    public List<GrnHasItem> getGrnHasItemList() {
        return grnHasItemList;
    }

    public void setGrnHasItemList(List<GrnHasItem> grnHasItemList) {
        this.grnHasItemList = grnHasItemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.Item[ id=" + id + " ]";
    }
    
}
