/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import java.io.Serializable;
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
    @NamedQuery(name = "Batch.findAll", query = "SELECT b FROM Batch b"),
    @NamedQuery(name = "Batch.findById", query = "SELECT b FROM Batch b WHERE b.id = :id")})
public class Batch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Lob
    private String barcode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "batchid")
    private List<InvoiceHasItem> invoiceHasItemList;
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userid;
    @JoinColumn(name = "Grn_Has_Item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GrnHasItem grnHasItemid;

    public Batch() {
    }

    public Batch(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @XmlTransient
    public List<InvoiceHasItem> getInvoiceHasItemList() {
        return invoiceHasItemList;
    }

    public void setInvoiceHasItemList(List<InvoiceHasItem> invoiceHasItemList) {
        this.invoiceHasItemList = invoiceHasItemList;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    public GrnHasItem getGrnHasItemid() {
        return grnHasItemid;
    }

    public void setGrnHasItemid(GrnHasItem grnHasItemid) {
        this.grnHasItemid = grnHasItemid;
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
        if (!(object instanceof Batch)) {
            return false;
        }
        Batch other = (Batch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.Batch[ id=" + id + " ]";
    }
    
}
