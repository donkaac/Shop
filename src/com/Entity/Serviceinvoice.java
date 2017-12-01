/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Achi
 */
@Entity
@Table(catalog = "msdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Serviceinvoice.findAll", query = "SELECT s FROM Serviceinvoice s"),
    @NamedQuery(name = "Serviceinvoice.findById", query = "SELECT s FROM Serviceinvoice s WHERE s.id = :id"),
    @NamedQuery(name = "Serviceinvoice.findByDate", query = "SELECT s FROM Serviceinvoice s WHERE s.date = :date"),
    @NamedQuery(name = "Serviceinvoice.findByPrice", query = "SELECT s FROM Serviceinvoice s WHERE s.price = :price"),
    @NamedQuery(name = "Serviceinvoice.findByStatus", query = "SELECT s FROM Serviceinvoice s WHERE s.status = :status")})
public class Serviceinvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double price;
    private Boolean status;
    @JoinColumn(name = "ServiceType_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Servicetype serviceTypeid;
    @JoinColumn(name = "Service_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Service serviceid;

    public Serviceinvoice() {
    }

    public Serviceinvoice(Integer id) {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Servicetype getServiceTypeid() {
        return serviceTypeid;
    }

    public void setServiceTypeid(Servicetype serviceTypeid) {
        this.serviceTypeid = serviceTypeid;
    }

    public Service getServiceid() {
        return serviceid;
    }

    public void setServiceid(Service serviceid) {
        this.serviceid = serviceid;
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
        if (!(object instanceof Serviceinvoice)) {
            return false;
        }
        Serviceinvoice other = (Serviceinvoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.Serviceinvoice[ id=" + id + " ]";
    }
    
}
