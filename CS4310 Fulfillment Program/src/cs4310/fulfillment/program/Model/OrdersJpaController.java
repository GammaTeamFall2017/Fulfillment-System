/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Model;

import cs4310.fulfillment.program.Model.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        if (orders.getSubitemSet() == null) {
            orders.setSubitemSet(new HashSet<Subitem>());
        }
        if (orders.getItemSet() == null) {
            orders.setItemSet(new HashSet<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Subitem> attachedSubitemSet = new HashSet<Subitem>();
            for (Subitem subitemSetSubitemToAttach : orders.getSubitemSet()) {
                subitemSetSubitemToAttach = em.getReference(subitemSetSubitemToAttach.getClass(), subitemSetSubitemToAttach.getSubitemId());
                attachedSubitemSet.add(subitemSetSubitemToAttach);
            }
            orders.setSubitemSet(attachedSubitemSet);
            Set<Item> attachedItemSet = new HashSet<Item>();
            for (Item itemSetItemToAttach : orders.getItemSet()) {
                itemSetItemToAttach = em.getReference(itemSetItemToAttach.getClass(), itemSetItemToAttach.getItemId());
                attachedItemSet.add(itemSetItemToAttach);
            }
            orders.setItemSet(attachedItemSet);
            em.persist(orders);
            for (Subitem subitemSetSubitem : orders.getSubitemSet()) {
                subitemSetSubitem.getOrdersSet().add(orders);
                subitemSetSubitem = em.merge(subitemSetSubitem);
            }
            for (Item itemSetItem : orders.getItemSet()) {
                itemSetItem.getOrdersSet().add(orders);
                itemSetItem = em.merge(itemSetItem);
            }
            em.getTransaction().commit();
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
            Set<Subitem> subitemSetOld = persistentOrders.getSubitemSet();
            Set<Subitem> subitemSetNew = orders.getSubitemSet();
            Set<Item> itemSetOld = persistentOrders.getItemSet();
            Set<Item> itemSetNew = orders.getItemSet();
            Set<Subitem> attachedSubitemSetNew = new HashSet<Subitem>();
            for (Subitem subitemSetNewSubitemToAttach : subitemSetNew) {
                subitemSetNewSubitemToAttach = em.getReference(subitemSetNewSubitemToAttach.getClass(), subitemSetNewSubitemToAttach.getSubitemId());
                attachedSubitemSetNew.add(subitemSetNewSubitemToAttach);
            }
            subitemSetNew = attachedSubitemSetNew;
            orders.setSubitemSet(subitemSetNew);
            Set<Item> attachedItemSetNew = new HashSet<Item>();
            for (Item itemSetNewItemToAttach : itemSetNew) {
                itemSetNewItemToAttach = em.getReference(itemSetNewItemToAttach.getClass(), itemSetNewItemToAttach.getItemId());
                attachedItemSetNew.add(itemSetNewItemToAttach);
            }
            itemSetNew = attachedItemSetNew;
            orders.setItemSet(itemSetNew);
            orders = em.merge(orders);
            for (Subitem subitemSetOldSubitem : subitemSetOld) {
                if (!subitemSetNew.contains(subitemSetOldSubitem)) {
                    subitemSetOldSubitem.getOrdersSet().remove(orders);
                    subitemSetOldSubitem = em.merge(subitemSetOldSubitem);
                }
            }
            for (Subitem subitemSetNewSubitem : subitemSetNew) {
                if (!subitemSetOld.contains(subitemSetNewSubitem)) {
                    subitemSetNewSubitem.getOrdersSet().add(orders);
                    subitemSetNewSubitem = em.merge(subitemSetNewSubitem);
                }
            }
            for (Item itemSetOldItem : itemSetOld) {
                if (!itemSetNew.contains(itemSetOldItem)) {
                    itemSetOldItem.getOrdersSet().remove(orders);
                    itemSetOldItem = em.merge(itemSetOldItem);
                }
            }
            for (Item itemSetNewItem : itemSetNew) {
                if (!itemSetOld.contains(itemSetNewItem)) {
                    itemSetNewItem.getOrdersSet().add(orders);
                    itemSetNewItem = em.merge(itemSetNewItem);
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
            Set<Subitem> subitemSet = orders.getSubitemSet();
            for (Subitem subitemSetSubitem : subitemSet) {
                subitemSetSubitem.getOrdersSet().remove(orders);
                subitemSetSubitem = em.merge(subitemSetSubitem);
            }
            Set<Item> itemSet = orders.getItemSet();
            for (Item itemSetItem : itemSet) {
                itemSetItem.getOrdersSet().remove(orders);
                itemSetItem = em.merge(itemSetItem);
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
