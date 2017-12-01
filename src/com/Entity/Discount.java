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
    @NamedQuery(name = "Discount.findAll", query = "SELECT d FROM Discount d"),
    @NamedQuery(name = "Discount.findById", query = "SELECT d FROM Discount d WHERE d.id = :id"),
    @NamedQuery(name = "Discount.findByDiscount", query = "SELECT d FROM Discount d WHERE d.discount = :discount"),
    @NamedQuery(name = "Discount.findByValue", query = "SELECT d FROM Discount d WHERE d.value = :value"),
    @NamedQuery(name = "Discount.findByDate", query = "SELECT d FROM Discount d WHERE d.date = :date"),
    @NamedQuery(name = "Discount.findByStatus", query = "SELECT d FROM Discount d WHERE d.status = :status")})
public class Discount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Integer id;
    private String discount;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double value;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discountid")
    private List<Customertype> customertypeList;
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userid;

    public Discount() {
    }

    public Discount(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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
    public List<Customertype> getCustomertypeList() {
        return customertypeList;
    }

    public void setCustomertypeList(List<Customertype> customertypeList) {
        this.customertypeList = customertypeList;
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
        if (!(object instanceof Discount)) {
            return false;
        }
        Discount other = (Discount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.Discount[ id=" + id + " ]";
    }
    
}
