package cs4310.fulfillment.program.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diana
 */
@Entity
@Table(catalog = "q7bcl9r2zulifvor", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"order_number"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o")
    , @NamedQuery(name = "Orders.findByOrderNumber", query = "SELECT o FROM Orders o WHERE o.orderNumber = :orderNumber")
    , @NamedQuery(name = "Orders.findByTableNumber", query = "SELECT o FROM Orders o WHERE o.tableNumber = :tableNumber")
    , @NamedQuery(name = "Orders.findByKitchenComplete", query = "SELECT o FROM Orders o WHERE o.kitchenComplete = :kitchenComplete")
    , @NamedQuery(name = "Orders.findByRequestWaitstaff", query = "SELECT o FROM Orders o WHERE o.requestWaitstaff = :requestWaitstaff")
    , @NamedQuery(name = "Orders.findByOrderPaid", query = "SELECT o FROM Orders o WHERE o.orderPaid = :orderPaid")
    , @NamedQuery(name = "Orders.findByTotalPrice", query = "SELECT o FROM Orders o WHERE o.totalPrice = :totalPrice")
    , @NamedQuery(name = "Orders.findByDateCreated", query = "SELECT o FROM Orders o WHERE o.dateCreated = :dateCreated")})
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;
    @Basic(optional = false)
    @Column(name = "table_number", nullable = false, length = 45)
    private String tableNumber;
    @Column(name = "kitchen_complete")
    private Boolean kitchenComplete;
    @Column(name = "request_waitstaff")
    private Boolean requestWaitstaff;
    @Column(name = "order_paid")
    private Boolean orderPaid = false;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_price", precision = 7, scale = 2)
    private BigDecimal totalPrice;
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @OneToMany(mappedBy = "orderId")
    private Collection<ItemsOrdered> itemsOrderedCollection;

    public Orders() {
    }

    public Orders(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Orders(Integer orderNumber, String tableNumber) {
        this.orderNumber = orderNumber;
        this.tableNumber = tableNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Boolean getKitchenComplete() {
        return kitchenComplete;
    }

    public void setKitchenComplete(Boolean kitchenComplete) {
        this.kitchenComplete = kitchenComplete;
    }

    public Boolean getRequestWaitstaff() {
        return requestWaitstaff;
    }

    public void setRequestWaitstaff(Boolean requestWaitstaff) {
        this.requestWaitstaff = requestWaitstaff;
    }

    public Boolean getOrderPaid() {
        return orderPaid;
    }

    public void setOrderPaid(Boolean orderPaid) {
        this.orderPaid = orderPaid;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDateCreated() {
        //Just incase order has no date setting default to current time, debuggin purpose remove later if needed
        if(dateCreated == null){dateCreated = new Date();}
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @XmlTransient
    public Collection<ItemsOrdered> getItemsOrderedCollection() {
        return itemsOrderedCollection;
    }

    public void setItemsOrderedCollection(Collection<ItemsOrdered> itemsOrderedCollection) {
        this.itemsOrderedCollection = itemsOrderedCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderNumber != null ? orderNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.orderNumber == null && other.orderNumber != null) || (this.orderNumber != null && !this.orderNumber.equals(other.orderNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cs4310.fulfillment.program.Model.Orders[ orderNumber=" + orderNumber + " ]";
    }
    
}
