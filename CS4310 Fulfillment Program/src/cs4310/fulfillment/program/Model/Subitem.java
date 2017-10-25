package cs4310.fulfillment.program.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
    @UniqueConstraint(columnNames = {"subitem_id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subitem.findAll", query = "SELECT s FROM Subitem s")
    , @NamedQuery(name = "Subitem.findBySubitemId", query = "SELECT s FROM Subitem s WHERE s.subitemId = :subitemId")
    , @NamedQuery(name = "Subitem.findBySubitemName", query = "SELECT s FROM Subitem s WHERE s.subitemName = :subitemName")
    , @NamedQuery(name = "Subitem.findBySubitemEta", query = "SELECT s FROM Subitem s WHERE s.subitemEta = :subitemEta")
    , @NamedQuery(name = "Subitem.findBySubitemPrice", query = "SELECT s FROM Subitem s WHERE s.subitemPrice = :subitemPrice")
    , @NamedQuery(name = "Subitem.findBySubitemType", query = "SELECT s FROM Subitem s WHERE s.subitemType = :subitemType")})
public class Subitem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "subitem_id", nullable = false)
    private Integer subitemId;
    @Basic(optional = false)
    @Column(name = "subitem_name", nullable = false, length = 45)
    private String subitemName;
    @Basic(optional = false)
    @Column(name = "subitem_eta", nullable = false)
    private int subitemEta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "subitem_price", nullable = false, precision = 7, scale = 2)
    private BigDecimal subitemPrice;
    @Basic(optional = false)
    @Column(name = "subitem_type", nullable = false, length = 45)
    private String subitemType;
    @ManyToMany(mappedBy = "subitemCollection")
    private Collection<Orders> ordersCollection;
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", nullable = false)
    @ManyToOne(optional = false)
    private Item itemId;

    public Subitem() {
    }

    public Subitem(Integer subitemId) {
        this.subitemId = subitemId;
    }

    public Subitem(Integer subitemId, String subitemName, int subitemEta, BigDecimal subitemPrice, String subitemType) {
        this.subitemId = subitemId;
        this.subitemName = subitemName;
        this.subitemEta = subitemEta;
        this.subitemPrice = subitemPrice;
        this.subitemType = subitemType;
    }

    public Integer getSubitemId() {
        return subitemId;
    }

    public void setSubitemId(Integer subitemId) {
        this.subitemId = subitemId;
    }

    public String getSubitemName() {
        return subitemName;
    }

    public void setSubitemName(String subitemName) {
        this.subitemName = subitemName;
    }

    public int getSubitemEta() {
        return subitemEta;
    }

    public void setSubitemEta(int subitemEta) {
        this.subitemEta = subitemEta;
    }

    public BigDecimal getSubitemPrice() {
        return subitemPrice;
    }

    public void setSubitemPrice(BigDecimal subitemPrice) {
        this.subitemPrice = subitemPrice;
    }

    public String getSubitemType() {
        return subitemType;
    }

    public void setSubitemType(String subitemType) {
        this.subitemType = subitemType;
    }

    @XmlTransient
    public Collection<Orders> getOrdersCollection() {
        return ordersCollection;
    }

    public void setOrdersCollection(Collection<Orders> ordersCollection) {
        this.ordersCollection = ordersCollection;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subitemId != null ? subitemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subitem)) {
            return false;
        }
        Subitem other = (Subitem) object;
        if ((this.subitemId == null && other.subitemId != null) || (this.subitemId != null && !this.subitemId.equals(other.subitemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cs4310.fulfillment.program.Model.Subitem[ subitemId=" + subitemId + " ]";
    }
    
}
