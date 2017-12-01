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
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByMiddleName", query = "SELECT u FROM User u WHERE u.middleName = :middleName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByNic", query = "SELECT u FROM User u WHERE u.nic = :nic"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByAddress", query = "SELECT u FROM User u WHERE u.address = :address"),
    @NamedQuery(name = "User.findByAddress1", query = "SELECT u FROM User u WHERE u.address1 = :address1"),
    @NamedQuery(name = "User.findByContact", query = "SELECT u FROM User u WHERE u.contact = :contact"),
    @NamedQuery(name = "User.findByContact1", query = "SELECT u FROM User u WHERE u.contact1 = :contact1"),
    @NamedQuery(name = "User.findByDate", query = "SELECT u FROM User u WHERE u.date = :date"),
    @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nic;
    private String email;
    private String address;
    private String address1;
    private String contact;
    private String contact1;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Invoice> invoiceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Customer> customerList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Purchaseorder> purchaseorderList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Customertype> customertypeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Grn> grnList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Login> loginList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Supplier> supplierList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<UserHasAction> userHasActionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Agent> agentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Discount> discountList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Category> categoryList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Tax> taxList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Batch> batchList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Subcategory> subcategoryList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Item> itemList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Brand> brandList;
    @JoinColumn(name = "UserType_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usertype userTypeid;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
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
    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @XmlTransient
    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @XmlTransient
    public List<Purchaseorder> getPurchaseorderList() {
        return purchaseorderList;
    }

    public void setPurchaseorderList(List<Purchaseorder> purchaseorderList) {
        this.purchaseorderList = purchaseorderList;
    }

    @XmlTransient
    public List<Customertype> getCustomertypeList() {
        return customertypeList;
    }

    public void setCustomertypeList(List<Customertype> customertypeList) {
        this.customertypeList = customertypeList;
    }

    @XmlTransient
    public List<Grn> getGrnList() {
        return grnList;
    }

    public void setGrnList(List<Grn> grnList) {
        this.grnList = grnList;
    }

    @XmlTransient
    public List<Login> getLoginList() {
        return loginList;
    }

    public void setLoginList(List<Login> loginList) {
        this.loginList = loginList;
    }

    @XmlTransient
    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    @XmlTransient
    public List<UserHasAction> getUserHasActionList() {
        return userHasActionList;
    }

    public void setUserHasActionList(List<UserHasAction> userHasActionList) {
        this.userHasActionList = userHasActionList;
    }

    @XmlTransient
    public List<Agent> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<Agent> agentList) {
        this.agentList = agentList;
    }

    @XmlTransient
    public List<Discount> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<Discount> discountList) {
        this.discountList = discountList;
    }

    @XmlTransient
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @XmlTransient
    public List<Tax> getTaxList() {
        return taxList;
    }

    public void setTaxList(List<Tax> taxList) {
        this.taxList = taxList;
    }

    @XmlTransient
    public List<Batch> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<Batch> batchList) {
        this.batchList = batchList;
    }

    @XmlTransient
    public List<Subcategory> getSubcategoryList() {
        return subcategoryList;
    }

    public void setSubcategoryList(List<Subcategory> subcategoryList) {
        this.subcategoryList = subcategoryList;
    }

    @XmlTransient
    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @XmlTransient
    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public Usertype getUserTypeid() {
        return userTypeid;
    }

    public void setUserTypeid(Usertype userTypeid) {
        this.userTypeid = userTypeid;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Entity.User[ id=" + id + " ]";
    }
    
}
