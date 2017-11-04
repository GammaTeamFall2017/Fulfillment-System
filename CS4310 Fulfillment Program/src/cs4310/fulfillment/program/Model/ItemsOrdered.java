/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author diana
 */
@Entity
@Table(name = "ITEMS_ORDERED", catalog = "q7bcl9r2zulifvor", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"line_item_id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemsOrdered.findAll", query = "SELECT i FROM ItemsOrdered i")
    , @NamedQuery(name = "ItemsOrdered.findByLineItemId", query = "SELECT i FROM ItemsOrdered i WHERE i.lineItemId = :lineItemId")
    , @NamedQuery(name = "ItemsOrdered.findByItemQuantity", query = "SELECT i FROM ItemsOrdered i WHERE i.itemQuantity = :itemQuantity")
    , @NamedQuery(name = "ItemsOrdered.findBySpecialInstructions", query = "SELECT i FROM ItemsOrdered i WHERE i.specialInstructions = :specialInstructions")})
public class ItemsOrdered implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "line_item_id", nullable = false)
    private Integer lineItemId;
    @Basic(optional = false)
    @Column(name = "item_quantity", nullable = false)
    private int itemQuantity;
    @Column(name = "special_instructions", length = 255)
    private String specialInstructions;
    @JoinColumn(name = "item_in_order", referencedColumnName = "item_id", nullable = false)
    @ManyToOne(optional = false)
    private Item itemInOrder;
    @JoinColumn(name = "order_id", referencedColumnName = "order_number", nullable = false)
    @ManyToOne(optional = false)
    private Orders orderId;
    @JoinColumn(name = "subitem_ordered", referencedColumnName = "subitem_id")
    @ManyToOne
    private Subitem subitemOrdered;

    public ItemsOrdered() {
    }

    public ItemsOrdered(Integer lineItemId) {
        this.lineItemId = lineItemId;
    }

    public ItemsOrdered(Integer lineItemId, int itemQuantity) {
        this.lineItemId = lineItemId;
        this.itemQuantity = itemQuantity;
    }

    public Integer getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Integer lineItemId) {
        this.lineItemId = lineItemId;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Item getItemInOrder() {
        return itemInOrder;
    }

    public void setItemInOrder(Item itemInOrder) {
        this.itemInOrder = itemInOrder;
    }

    public Orders getOrderId() {
        return orderId;
    }

    public void setOrderId(Orders orderId) {
        this.orderId = orderId;
    }

    public Subitem getSubitemOrdered() {
        return subitemOrdered;
    }

    public void setSubitemOrdered(Subitem subitemOrdered) {
        this.subitemOrdered = subitemOrdered;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lineItemId != null ? lineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemsOrdered)) {
            return false;
        }
        ItemsOrdered other = (ItemsOrdered) object;
        if ((this.lineItemId == null && other.lineItemId != null) || (this.lineItemId != null && !this.lineItemId.equals(other.lineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ItemsOrdered[ lineItemId=" + lineItemId + getOrderId() + getItemInOrder() + getSubitemOrdered() +" ]";
    }
    
}
