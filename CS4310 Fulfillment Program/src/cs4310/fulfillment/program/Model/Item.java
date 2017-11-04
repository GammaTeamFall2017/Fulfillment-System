/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diana
 */
@Entity
@Table(catalog = "q7bcl9r2zulifvor", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"item_id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
    , @NamedQuery(name = "Item.findByItemId", query = "SELECT i FROM Item i WHERE i.itemId = :itemId")
    , @NamedQuery(name = "Item.findByItemName", query = "SELECT i FROM Item i WHERE i.itemName = :itemName")
    , @NamedQuery(name = "Item.findByItemDescription", query = "SELECT i FROM Item i WHERE i.itemDescription = :itemDescription")
    , @NamedQuery(name = "Item.findByItemEta", query = "SELECT i FROM Item i WHERE i.itemEta = :itemEta")
    , @NamedQuery(name = "Item.findByItemPrice", query = "SELECT i FROM Item i WHERE i.itemPrice = :itemPrice")})
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "item_id", nullable = false)
    private Integer itemId;
    @Basic(optional = false)
    @Column(name = "item_name", nullable = false, length = 45)
    private String itemName;
    @Column(name = "item_description", length = 255)
    private String itemDescription;
    @Basic(optional = false)
    @Column(name = "item_eta", nullable = false)
    private int itemEta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "item_price", nullable = false, precision = 7, scale = 2)
    private BigDecimal itemPrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemInOrder")
    private Set<ItemsOrdered> itemsOrderedSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemId")
    private Set<Subitem> subitemSet;

    public Item() {
    }

    public Item(Integer itemId) {
        this.itemId = itemId;
    }

    public Item(Integer itemId, String itemName, int itemEta, BigDecimal itemPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemEta = itemEta;
        this.itemPrice = itemPrice;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getItemEta() {
        return itemEta;
    }

    public void setItemEta(int itemEta) {
        this.itemEta = itemEta;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    @XmlTransient
    public Set<ItemsOrdered> getItemsOrderedSet() {
        return itemsOrderedSet;
    }

    public void setItemsOrderedSet(Set<ItemsOrdered> itemsOrderedSet) {
        this.itemsOrderedSet = itemsOrderedSet;
    }

    @XmlTransient
    public Set<Subitem> getSubitemSet() {
        return subitemSet;
    }

    public void setSubitemSet(Set<Subitem> subitemSet) {
        this.subitemSet = subitemSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Item[ itemId=" + itemId + " ]";
    }
    
}
