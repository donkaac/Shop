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
import javax.persistence.Lob;
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
    @NamedQuery(name = "Servicetype.findAll", query = "SELECT s FROM Servicetype s"),
    @NamedQuery(name = "Servicetype.findById", query = "SELECT s FROM Servicetype s WHERE s.id = :id"),
    @NamedQuery(name = "Servicetype.findByType", query = "SELECT s FROM Servicetype s WHERE s.type = :type"),
    @NamedQuery(name = "Servicetype.findByStatus", query = "SELECT s FROM Servicetype s WHERE s.status = :status")})
public class Servicetype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    private String type;
    @Lob
    private String desc;
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceTypeid")
    private List<Serviceinvoice> serviceinvoiceList;

    public Servicetype() {
    }

    public Servicetype(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    public List<Serviceinvoice> getServiceinvoiceList() {
        return serviceinvoiceList;
    }

    public void setServiceinvoiceList(List<Serviceinvoice> serviceinvoiceList) {
        this.serviceinvoiceList = serviceinvoiceList;
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
        if (!(object instanceof Servicetype)) {
            return false;
        }
        Servicetype other = (Servicetype) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.Servicetype[ id=" + id + " ]";
    }
    
}
