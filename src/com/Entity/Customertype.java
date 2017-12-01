/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
    @NamedQuery(name = "Customertype.findAll", query = "SELECT c FROM Customertype c"),
    @NamedQuery(name = "Customertype.findById", query = "SELECT c FROM Customertype c WHERE c.id = :id"),
    @NamedQuery(name = "Customertype.findByCustomerType", query = "SELECT c FROM Customertype c WHERE c.customerType = :customerType"),
    @NamedQuery(name = "Customertype.findByStatus", query = "SELECT c FROM Customertype c WHERE c.status = :status")})
public class Customertype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "customer_type")
    private String customerType;
    @Lob
    private String desc;
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerTypeid")
    private List<Customer> customerList;
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userid;
    @JoinColumn(name = "Discount_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Discount discountid;

    public Customertype() {
    }

    public Customertype(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @XmlTransient
    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    public Discount getDiscountid() {
        return discountid;
    }

    public void setDiscountid(Discount discountid) {
        this.discountid = discountid;
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
        if (!(object instanceof Customertype)) {
            return false;
        }
        Customertype other = (Customertype) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.Customertype[ id=" + id + " ]";
    }
    
}
