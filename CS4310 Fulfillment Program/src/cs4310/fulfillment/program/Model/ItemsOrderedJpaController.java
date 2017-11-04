/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Model;

import cs4310.fulfillment.program.Model.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author diana
 */
public class ItemsOrderedJpaController implements Serializable {

    public ItemsOrderedJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemsOrdered itemsOrdered) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item itemInOrder = itemsOrdered.getItemInOrder();
            if (itemInOrder != null) {
                itemInOrder = em.getReference(itemInOrder.getClass(), itemInOrder.getItemId());
                itemsOrdered.setItemInOrder(itemInOrder);
            }
            Orders orderId = itemsOrdered.getOrderId();
            if (orderId != null) {
                orderId = em.getReference(orderId.getClass(), orderId.getOrderNumber());
                itemsOrdered.setOrderId(orderId);
            }
            Subitem subitemOrdered = itemsOrdered.getSubitemOrdered();
            if (subitemOrdered != null) {
                subitemOrdered = em.getReference(subitemOrdered.getClass(), subitemOrdered.getSubitemId());
                itemsOrdered.setSubitemOrdered(subitemOrdered);
            }
            em.persist(itemsOrdered);
            if (itemInOrder != null) {
                itemInOrder.getItemsOrderedSet().add(itemsOrdered);
                itemInOrder = em.merge(itemInOrder);
            }
            if (orderId != null) {
                orderId.getItemsOrderedSet().add(itemsOrdered);
                orderId = em.merge(orderId);
            }
            if (subitemOrdered != null) {
                subitemOrdered.getItemsOrderedSet().add(itemsOrdered);
                subitemOrdered = em.merge(subitemOrdered);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItemsOrdered itemsOrdered) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemsOrdered persistentItemsOrdered = em.find(ItemsOrdered.class, itemsOrdered.getLineItemId());
            Item itemInOrderOld = persistentItemsOrdered.getItemInOrder();
            Item itemInOrderNew = itemsOrdered.getItemInOrder();
            Orders orderIdOld = persistentItemsOrdered.getOrderId();
            Orders orderIdNew = itemsOrdered.getOrderId();
            Subitem subitemOrderedOld = persistentItemsOrdered.getSubitemOrdered();
            Subitem subitemOrderedNew = itemsOrdered.getSubitemOrdered();
            if (itemInOrderNew != null) {
                itemInOrderNew = em.getReference(itemInOrderNew.getClass(), itemInOrderNew.getItemId());
                itemsOrdered.setItemInOrder(itemInOrderNew);
            }
            if (orderIdNew != null) {
                orderIdNew = em.getReference(orderIdNew.getClass(), orderIdNew.getOrderNumber());
                itemsOrdered.setOrderId(orderIdNew);
            }
            if (subitemOrderedNew != null) {
                subitemOrderedNew = em.getReference(subitemOrderedNew.getClass(), subitemOrderedNew.getSubitemId());
                itemsOrdered.setSubitemOrdered(subitemOrderedNew);
            }
            itemsOrdered = em.merge(itemsOrdered);
            if (itemInOrderOld != null && !itemInOrderOld.equals(itemInOrderNew)) {
                itemInOrderOld.getItemsOrderedSet().remove(itemsOrdered);
                itemInOrderOld = em.merge(itemInOrderOld);
            }
            if (itemInOrderNew != null && !itemInOrderNew.equals(itemInOrderOld)) {
                itemInOrderNew.getItemsOrderedSet().add(itemsOrdered);
                itemInOrderNew = em.merge(itemInOrderNew);
            }
            if (orderIdOld != null && !orderIdOld.equals(orderIdNew)) {
                orderIdOld.getItemsOrderedSet().remove(itemsOrdered);
                orderIdOld = em.merge(orderIdOld);
            }
            if (orderIdNew != null && !orderIdNew.equals(orderIdOld)) {
                orderIdNew.getItemsOrderedSet().add(itemsOrdered);
                orderIdNew = em.merge(orderIdNew);
            }
            if (subitemOrderedOld != null && !subitemOrderedOld.equals(subitemOrderedNew)) {
                subitemOrderedOld.getItemsOrderedSet().remove(itemsOrdered);
                subitemOrderedOld = em.merge(subitemOrderedOld);
            }
            if (subitemOrderedNew != null && !subitemOrderedNew.equals(subitemOrderedOld)) {
                subitemOrderedNew.getItemsOrderedSet().add(itemsOrdered);
                subitemOrderedNew = em.merge(subitemOrderedNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = itemsOrdered.getLineItemId();
                if (findItemsOrdered(id) == null) {
                    throw new NonexistentEntityException("The itemsOrdered with id " + id + " no longer exists.");
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
            ItemsOrdered itemsOrdered;
            try {
                itemsOrdered = em.getReference(ItemsOrdered.class, id);
                itemsOrdered.getLineItemId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemsOrdered with id " + id + " no longer exists.", enfe);
            }
            Item itemInOrder = itemsOrdered.getItemInOrder();
            if (itemInOrder != null) {
                itemInOrder.getItemsOrderedSet().remove(itemsOrdered);
                itemInOrder = em.merge(itemInOrder);
            }
            Orders orderId = itemsOrdered.getOrderId();
            if (orderId != null) {
                orderId.getItemsOrderedSet().remove(itemsOrdered);
                orderId = em.merge(orderId);
            }
            Subitem subitemOrdered = itemsOrdered.getSubitemOrdered();
            if (subitemOrdered != null) {
                subitemOrdered.getItemsOrderedSet().remove(itemsOrdered);
                subitemOrdered = em.merge(subitemOrdered);
            }
            em.remove(itemsOrdered);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ItemsOrdered> findItemsOrderedEntities() {
        return findItemsOrderedEntities(true, -1, -1);
    }

    public List<ItemsOrdered> findItemsOrderedEntities(int maxResults, int firstResult) {
        return findItemsOrderedEntities(false, maxResults, firstResult);
    }

    private List<ItemsOrdered> findItemsOrderedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemsOrdered.class));
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

    public ItemsOrdered findItemsOrdered(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemsOrdered.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemsOrderedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemsOrdered> rt = cq.from(ItemsOrdered.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
