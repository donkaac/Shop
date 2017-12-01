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
    @NamedQuery(name = "Purchaseorder.findAll", query = "SELECT p FROM Purchaseorder p"),
    @NamedQuery(name = "Purchaseorder.findById", query = "SELECT p FROM Purchaseorder p WHERE p.id = :id"),
    @NamedQuery(name = "Purchaseorder.findByDate", query = "SELECT p FROM Purchaseorder p WHERE p.date = :date"),
    @NamedQuery(name = "Purchaseorder.findByStatus", query = "SELECT p FROM Purchaseorder p WHERE p.status = :status")})
public class Purchaseorder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrderid")
    private List<PurchaseOrderHasItem> purchaseOrderHasItemList;
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userid;

    public Purchaseorder() {
    }

    public Purchaseorder(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @XmlTransient
    public List<PurchaseOrderHasItem> getPurchaseOrderHasItemList() {
        return purchaseOrderHasItemList;
    }

    public void setPurchaseOrderHasItemList(List<PurchaseOrderHasItem> purchaseOrderHasItemList) {
        this.purchaseOrderHasItemList = purchaseOrderHasItemList;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
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
        if (!(object instanceof Purchaseorder)) {
            return false;
        }
        Purchaseorder other = (Purchaseorder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.Purchaseorder[ id=" + id + " ]";
    }
    
}
