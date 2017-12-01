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
    @NamedQuery(name = "Service.findAll", query = "SELECT s FROM Service s"),
    @NamedQuery(name = "Service.findById", query = "SELECT s FROM Service s WHERE s.id = :id"),
    @NamedQuery(name = "Service.findByItem", query = "SELECT s FROM Service s WHERE s.item = :item"),
    @NamedQuery(name = "Service.findByReciveDate", query = "SELECT s FROM Service s WHERE s.reciveDate = :reciveDate"),
    @NamedQuery(name = "Service.findByReleaseDate", query = "SELECT s FROM Service s WHERE s.releaseDate = :releaseDate"),
    @NamedQuery(name = "Service.findByImei", query = "SELECT s FROM Service s WHERE s.imei = :imei"),
    @NamedQuery(name = "Service.findByStatus", query = "SELECT s FROM Service s WHERE s.status = :status")})
public class Service implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    private String item;
    @Temporal(TemporalType.TIMESTAMP)
    private Date reciveDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;
    private String imei;
    @Lob
    private String desc;
    @Lob
    private String situation;
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceid")
    private List<Serviceinvoice> serviceinvoiceList;
    @JoinColumn(name = "Customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customerid;

    public Service() {
    }

    public Service(Integer id) {
        this.id = id;
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

    public Date getReciveDate() {
        return reciveDate;
    }

    public void setReciveDate(Date reciveDate) {
        this.reciveDate = reciveDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @XmlTransient
    public List<Serviceinvoice> getServiceinvoiceList() {
        return serviceinvoiceList;
    }

    public void setServiceinvoiceList(List<Serviceinvoice> serviceinvoiceList) {
        this.serviceinvoiceList = serviceinvoiceList;
    }

    public Customer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Customer customerid) {
        this.customerid = customerid;
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
        if (!(object instanceof Service)) {
            return false;
        }
        Service other = (Service) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.Service[ id=" + id + " ]";
    }
    
}
