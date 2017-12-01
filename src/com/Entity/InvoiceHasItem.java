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
@Table(name = "invoice_has_item", catalog = "msdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvoiceHasItem.findAll", query = "SELECT i FROM InvoiceHasItem i"),
    @NamedQuery(name = "InvoiceHasItem.findById", query = "SELECT i FROM InvoiceHasItem i WHERE i.id = :id"),
    @NamedQuery(name = "InvoiceHasItem.findByQty", query = "SELECT i FROM InvoiceHasItem i WHERE i.qty = :qty"),
    @NamedQuery(name = "InvoiceHasItem.findByPrice", query = "SELECT i FROM InvoiceHasItem i WHERE i.price = :price")})
public class InvoiceHasItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double qty;
    private Double price;
    @JoinColumn(name = "Invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Invoice invoiceid;
    @JoinColumn(name = "Batch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Batch batchid;

    public InvoiceHasItem() {
    }

    public InvoiceHasItem(Integer id) {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Invoice getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(Invoice invoiceid) {
        this.invoiceid = invoiceid;
    }

    public Batch getBatchid() {
        return batchid;
    }

    public void setBatchid(Batch batchid) {
        this.batchid = batchid;
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
        if (!(object instanceof InvoiceHasItem)) {
            return false;
        }
        InvoiceHasItem other = (InvoiceHasItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.InvoiceHasItem[ id=" + id + " ]";
    }
    
}
