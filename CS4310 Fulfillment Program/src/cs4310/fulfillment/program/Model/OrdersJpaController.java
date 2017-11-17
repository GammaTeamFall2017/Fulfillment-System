package cs4310.fulfillment.program.Model;

import cs4310.fulfillment.program.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author diana
 */
public class OrdersJpaController implements Serializable {

    public OrdersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orders orders) {
        if (orders.getItemsOrderedCollection() == null) {
            orders.setItemsOrderedCollection(new ArrayList<ItemsOrdered>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ItemsOrdered> attachedItemsOrderedCollection = new ArrayList<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedCollectionItemsOrderedToAttach : orders.getItemsOrderedCollection()) {
                itemsOrderedCollectionItemsOrderedToAttach = em.getReference(itemsOrderedCollectionItemsOrderedToAttach.getClass(), itemsOrderedCollectionItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedCollection.add(itemsOrderedCollectionItemsOrderedToAttach);
            }
            orders.setItemsOrderedCollection(attachedItemsOrderedCollection);
            em.persist(orders);
            for (ItemsOrdered itemsOrderedCollectionItemsOrdered : orders.getItemsOrderedCollection()) {
                Orders oldOrderIdOfItemsOrderedCollectionItemsOrdered = itemsOrderedCollectionItemsOrdered.getOrderId();
                itemsOrderedCollectionItemsOrdered.setOrderId(orders);
                itemsOrderedCollectionItemsOrdered = em.merge(itemsOrderedCollectionItemsOrdered);
                if (oldOrderIdOfItemsOrderedCollectionItemsOrdered != null) {
                    oldOrderIdOfItemsOrderedCollectionItemsOrdered.getItemsOrderedCollection().remove(itemsOrderedCollectionItemsOrdered);
                    oldOrderIdOfItemsOrderedCollectionItemsOrdered = em.merge(oldOrderIdOfItemsOrderedCollectionItemsOrdered);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public Orders createAndReturn(Orders orders) {
        if (orders.getItemsOrderedCollection() == null) {
            orders.setItemsOrderedCollection(new ArrayList<ItemsOrdered>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ItemsOrdered> attachedItemsOrderedCollection = new ArrayList<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedCollectionItemsOrderedToAttach : orders.getItemsOrderedCollection()) {
                itemsOrderedCollectionItemsOrderedToAttach = em.getReference(itemsOrderedCollectionItemsOrderedToAttach.getClass(), itemsOrderedCollectionItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedCollection.add(itemsOrderedCollectionItemsOrderedToAttach);
            }
            orders.setItemsOrderedCollection(attachedItemsOrderedCollection);
            em.persist(orders);
            for (ItemsOrdered itemsOrderedCollectionItemsOrdered : orders.getItemsOrderedCollection()) {
                Orders oldOrderIdOfItemsOrderedCollectionItemsOrdered = itemsOrderedCollectionItemsOrdered.getOrderId();
                itemsOrderedCollectionItemsOrdered.setOrderId(orders);
                itemsOrderedCollectionItemsOrdered = em.merge(itemsOrderedCollectionItemsOrdered);
                if (oldOrderIdOfItemsOrderedCollectionItemsOrdered != null) {
                    oldOrderIdOfItemsOrderedCollectionItemsOrdered.getItemsOrderedCollection().remove(itemsOrderedCollectionItemsOrdered);
                    oldOrderIdOfItemsOrderedCollectionItemsOrdered = em.merge(oldOrderIdOfItemsOrderedCollectionItemsOrdered);
                }
            }
            em.getTransaction().commit();
            return orders;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orders orders) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders persistentOrders = em.find(Orders.class, orders.getOrderNumber());
            Collection<ItemsOrdered> itemsOrderedCollectionOld = persistentOrders.getItemsOrderedCollection();
            Collection<ItemsOrdered> itemsOrderedCollectionNew = orders.getItemsOrderedCollection();
            Collection<ItemsOrdered> attachedItemsOrderedCollectionNew = new ArrayList<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedCollectionNewItemsOrderedToAttach : itemsOrderedCollectionNew) {
                itemsOrderedCollectionNewItemsOrderedToAttach = em.getReference(itemsOrderedCollectionNewItemsOrderedToAttach.getClass(), itemsOrderedCollectionNewItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedCollectionNew.add(itemsOrderedCollectionNewItemsOrderedToAttach);
            }
            itemsOrderedCollectionNew = attachedItemsOrderedCollectionNew;
            orders.setItemsOrderedCollection(itemsOrderedCollectionNew);
            orders = em.merge(orders);
            for (ItemsOrdered itemsOrderedCollectionOldItemsOrdered : itemsOrderedCollectionOld) {
                if (!itemsOrderedCollectionNew.contains(itemsOrderedCollectionOldItemsOrdered)) {
                    itemsOrderedCollectionOldItemsOrdered.setOrderId(null);
                    itemsOrderedCollectionOldItemsOrdered = em.merge(itemsOrderedCollectionOldItemsOrdered);
                }
            }
            for (ItemsOrdered itemsOrderedCollectionNewItemsOrdered : itemsOrderedCollectionNew) {
                if (!itemsOrderedCollectionOld.contains(itemsOrderedCollectionNewItemsOrdered)) {
                    Orders oldOrderIdOfItemsOrderedCollectionNewItemsOrdered = itemsOrderedCollectionNewItemsOrdered.getOrderId();
                    itemsOrderedCollectionNewItemsOrdered.setOrderId(orders);
                    itemsOrderedCollectionNewItemsOrdered = em.merge(itemsOrderedCollectionNewItemsOrdered);
                    if (oldOrderIdOfItemsOrderedCollectionNewItemsOrdered != null && !oldOrderIdOfItemsOrderedCollectionNewItemsOrdered.equals(orders)) {
                        oldOrderIdOfItemsOrderedCollectionNewItemsOrdered.getItemsOrderedCollection().remove(itemsOrderedCollectionNewItemsOrdered);
                        oldOrderIdOfItemsOrderedCollectionNewItemsOrdered = em.merge(oldOrderIdOfItemsOrderedCollectionNewItemsOrdered);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orders.getOrderNumber();
                if (findOrders(id) == null) {
                    throw new NonexistentEntityException("The orders with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders orders;
            try {
                orders = em.getReference(Orders.class, id);
                orders.getOrderNumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orders with id " + id + " no longer exists.", enfe);
            }
            Collection<ItemsOrdered> itemsOrderedCollection = orders.getItemsOrderedCollection();
            for (ItemsOrdered itemsOrderedCollectionItemsOrdered : itemsOrderedCollection) {
                itemsOrderedCollectionItemsOrdered.setOrderId(null);
                itemsOrderedCollectionItemsOrdered = em.merge(itemsOrderedCollectionItemsOrdered);
            }
            em.remove(orders);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orders> findOrdersEntities() {
        return findOrdersEntities(true, -1, -1);
    }

    public List<Orders> findOrdersEntities(int maxResults, int firstResult) {
        return findOrdersEntities(false, maxResults, firstResult);
    }

    private List<Orders> findOrdersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orders.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Orders findOrders(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orders.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orders> rt = cq.from(Orders.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
