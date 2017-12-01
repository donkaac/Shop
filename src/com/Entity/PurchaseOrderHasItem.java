/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Achi
 */
@Entity
@Table(name = "purchase_order_has_item", catalog = "msdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderHasItem.findAll", query = "SELECT p FROM PurchaseOrderHasItem p"),
    @NamedQuery(name = "PurchaseOrderHasItem.findById", query = "SELECT p FROM PurchaseOrderHasItem p WHERE p.id = :id"),
    @NamedQuery(name = "PurchaseOrderHasItem.findByQty", query = "SELECT p FROM PurchaseOrderHasItem p WHERE p.qty = :qty")})
public class PurchaseOrderHasItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double qty;
    @JoinColumn(name = "PurchaseOrder_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Purchaseorder purchaseOrderid;
    @JoinColumn(name = "Item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Item itemid;

    public PurchaseOrderHasItem() {
    }

    public PurchaseOrderHasItem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Purchaseorder getPurchaseOrderid() {
        return purchaseOrderid;
    }

    public void setPurchaseOrderid(Purchaseorder purchaseOrderid) {
        this.purchaseOrderid = purchaseOrderid;
    }

    public Item getItemid() {
        return itemid;
    }

    public void setItemid(Item itemid) {
        this.itemid = itemid;
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
        if (!(object instanceof PurchaseOrderHasItem)) {
            return false;
        }
        PurchaseOrderHasItem other = (PurchaseOrderHasItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.PurchaseOrderHasItem[ id=" + id + " ]";
    }
    
}
