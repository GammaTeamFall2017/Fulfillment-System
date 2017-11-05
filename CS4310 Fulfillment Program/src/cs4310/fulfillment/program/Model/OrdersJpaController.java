/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Model;

import cs4310.fulfillment.program.Model.exceptions.IllegalOrphanException;
import cs4310.fulfillment.program.Model.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
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
        if (orders.getItemsOrderedSet() == null) {
            orders.setItemsOrderedSet(new HashSet<ItemsOrdered>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<ItemsOrdered> attachedItemsOrderedSet = new HashSet<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedSetItemsOrderedToAttach : orders.getItemsOrderedSet()) {
                itemsOrderedSetItemsOrderedToAttach = em.getReference(itemsOrderedSetItemsOrderedToAttach.getClass(), itemsOrderedSetItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedSet.add(itemsOrderedSetItemsOrderedToAttach);
            }
            orders.setItemsOrderedSet(attachedItemsOrderedSet);
            em.persist(orders);
            for (ItemsOrdered itemsOrderedSetItemsOrdered : orders.getItemsOrderedSet()) {
                Orders oldOrderIdOfItemsOrderedSetItemsOrdered = itemsOrderedSetItemsOrdered.getOrderId();
                itemsOrderedSetItemsOrdered.setOrderId(orders);
                itemsOrderedSetItemsOrdered = em.merge(itemsOrderedSetItemsOrdered);
                if (oldOrderIdOfItemsOrderedSetItemsOrdered != null) {
                    oldOrderIdOfItemsOrderedSetItemsOrdered.getItemsOrderedSet().remove(itemsOrderedSetItemsOrdered);
                    oldOrderIdOfItemsOrderedSetItemsOrdered = em.merge(oldOrderIdOfItemsOrderedSetItemsOrdered);
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
        if (orders.getItemsOrderedSet() == null) {
            orders.setItemsOrderedSet(new HashSet<ItemsOrdered>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<ItemsOrdered> attachedItemsOrderedSet = new HashSet<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedSetItemsOrderedToAttach : orders.getItemsOrderedSet()) {
                itemsOrderedSetItemsOrderedToAttach = em.getReference(itemsOrderedSetItemsOrderedToAttach.getClass(), itemsOrderedSetItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedSet.add(itemsOrderedSetItemsOrderedToAttach);
            }
            orders.setItemsOrderedSet(attachedItemsOrderedSet);
            em.persist(orders);
            for (ItemsOrdered itemsOrderedSetItemsOrdered : orders.getItemsOrderedSet()) {
                Orders oldOrderIdOfItemsOrderedSetItemsOrdered = itemsOrderedSetItemsOrdered.getOrderId();
                itemsOrderedSetItemsOrdered.setOrderId(orders);
                itemsOrderedSetItemsOrdered = em.merge(itemsOrderedSetItemsOrdered);
                if (oldOrderIdOfItemsOrderedSetItemsOrdered != null) {
                    oldOrderIdOfItemsOrderedSetItemsOrdered.getItemsOrderedSet().remove(itemsOrderedSetItemsOrdered);
                    oldOrderIdOfItemsOrderedSetItemsOrdered = em.merge(oldOrderIdOfItemsOrderedSetItemsOrdered);
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

    public void edit(Orders orders) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders persistentOrders = em.find(Orders.class, orders.getOrderNumber());
            Set<ItemsOrdered> itemsOrderedSetOld = persistentOrders.getItemsOrderedSet();
            Set<ItemsOrdered> itemsOrderedSetNew = orders.getItemsOrderedSet();
            List<String> illegalOrphanMessages = null;
            for (ItemsOrdered itemsOrderedSetOldItemsOrdered : itemsOrderedSetOld) {
                if (!itemsOrderedSetNew.contains(itemsOrderedSetOldItemsOrdered)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemsOrdered " + itemsOrderedSetOldItemsOrdered + " since its orderId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<ItemsOrdered> attachedItemsOrderedSetNew = new HashSet<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedSetNewItemsOrderedToAttach : itemsOrderedSetNew) {
                itemsOrderedSetNewItemsOrderedToAttach = em.getReference(itemsOrderedSetNewItemsOrderedToAttach.getClass(), itemsOrderedSetNewItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedSetNew.add(itemsOrderedSetNewItemsOrderedToAttach);
            }
            itemsOrderedSetNew = attachedItemsOrderedSetNew;
            orders.setItemsOrderedSet(itemsOrderedSetNew);
            orders = em.merge(orders);
            for (ItemsOrdered itemsOrderedSetNewItemsOrdered : itemsOrderedSetNew) {
                if (!itemsOrderedSetOld.contains(itemsOrderedSetNewItemsOrdered)) {
                    Orders oldOrderIdOfItemsOrderedSetNewItemsOrdered = itemsOrderedSetNewItemsOrdered.getOrderId();
                    itemsOrderedSetNewItemsOrdered.setOrderId(orders);
                    itemsOrderedSetNewItemsOrdered = em.merge(itemsOrderedSetNewItemsOrdered);
                    if (oldOrderIdOfItemsOrderedSetNewItemsOrdered != null && !oldOrderIdOfItemsOrderedSetNewItemsOrdered.equals(orders)) {
                        oldOrderIdOfItemsOrderedSetNewItemsOrdered.getItemsOrderedSet().remove(itemsOrderedSetNewItemsOrdered);
                        oldOrderIdOfItemsOrderedSetNewItemsOrdered = em.merge(oldOrderIdOfItemsOrderedSetNewItemsOrdered);
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Set<ItemsOrdered> itemsOrderedSetOrphanCheck = orders.getItemsOrderedSet();
            for (ItemsOrdered itemsOrderedSetOrphanCheckItemsOrdered : itemsOrderedSetOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orders (" + orders + ") cannot be destroyed since the ItemsOrdered " + itemsOrderedSetOrphanCheckItemsOrdered + " in its itemsOrderedSet field has a non-nullable orderId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
