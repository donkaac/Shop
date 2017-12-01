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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "grn_has_item", catalog = "msdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrnHasItem.findAll", query = "SELECT g FROM GrnHasItem g"),
    @NamedQuery(name = "GrnHasItem.findById", query = "SELECT g FROM GrnHasItem g WHERE g.id = :id"),
    @NamedQuery(name = "GrnHasItem.findByQty", query = "SELECT g FROM GrnHasItem g WHERE g.qty = :qty"),
    @NamedQuery(name = "GrnHasItem.findByBuyPrice", query = "SELECT g FROM GrnHasItem g WHERE g.buyPrice = :buyPrice"),
    @NamedQuery(name = "GrnHasItem.findBySellPrice", query = "SELECT g FROM GrnHasItem g WHERE g.sellPrice = :sellPrice"),
    @NamedQuery(name = "GrnHasItem.findByExpireDate", query = "SELECT g FROM GrnHasItem g WHERE g.expireDate = :expireDate")})
public class GrnHasItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double qty;
    @Column(name = "buy_price")
    private Double buyPrice;
    @Column(name = "sell_price")
    private Double sellPrice;
    @Column(name = "expire_date")
    @Temporal(TemporalType.DATE)
    private Date expireDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grnHasItemid")
    private List<Batch> batchList;
    @JoinColumn(name = "Item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Item itemid;
    @JoinColumn(name = "Grn_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Grn grnid;

    public GrnHasItem() {
    }

    public GrnHasItem(Integer id) {
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

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @XmlTransient
    public List<Batch> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<Batch> batchList) {
        this.batchList = batchList;
    }

    public Item getItemid() {
        return itemid;
    }

    public void setItemid(Item itemid) {
        this.itemid = itemid;
    }

    public Grn getGrnid() {
        return grnid;
    }

    public void setGrnid(Grn grnid) {
        this.grnid = grnid;
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
        if (!(object instanceof GrnHasItem)) {
            return false;
        }
        GrnHasItem other = (GrnHasItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.GrnHasItem[ id=" + id + " ]";
    }
    
}
