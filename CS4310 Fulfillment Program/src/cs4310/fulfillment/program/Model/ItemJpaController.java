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
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) {
        if (item.getOrdersSet() == null) {
            item.setOrdersSet(new HashSet<Orders>());
        }
        if (item.getSubitemSet() == null) {
            item.setSubitemSet(new HashSet<Subitem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Orders> attachedOrdersSet = new HashSet<Orders>();
            for (Orders ordersSetOrdersToAttach : item.getOrdersSet()) {
                ordersSetOrdersToAttach = em.getReference(ordersSetOrdersToAttach.getClass(), ordersSetOrdersToAttach.getOrderNumber());
                attachedOrdersSet.add(ordersSetOrdersToAttach);
            }
            item.setOrdersSet(attachedOrdersSet);
            Set<Subitem> attachedSubitemSet = new HashSet<Subitem>();
            for (Subitem subitemSetSubitemToAttach : item.getSubitemSet()) {
                subitemSetSubitemToAttach = em.getReference(subitemSetSubitemToAttach.getClass(), subitemSetSubitemToAttach.getSubitemId());
                attachedSubitemSet.add(subitemSetSubitemToAttach);
            }
            item.setSubitemSet(attachedSubitemSet);
            em.persist(item);
            for (Orders ordersSetOrders : item.getOrdersSet()) {
                ordersSetOrders.getItemSet().add(item);
                ordersSetOrders = em.merge(ordersSetOrders);
            }
            for (Subitem subitemSetSubitem : item.getSubitemSet()) {
                Item oldItemIdOfSubitemSetSubitem = subitemSetSubitem.getItemId();
                subitemSetSubitem.setItemId(item);
                subitemSetSubitem = em.merge(subitemSetSubitem);
                if (oldItemIdOfSubitemSetSubitem != null) {
                    oldItemIdOfSubitemSetSubitem.getSubitemSet().remove(subitemSetSubitem);
                    oldItemIdOfSubitemSetSubitem = em.merge(oldItemIdOfSubitemSetSubitem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getItemId());
            Set<Orders> ordersSetOld = persistentItem.getOrdersSet();
            Set<Orders> ordersSetNew = item.getOrdersSet();
            Set<Subitem> subitemSetOld = persistentItem.getSubitemSet();
            Set<Subitem> subitemSetNew = item.getSubitemSet();
            List<String> illegalOrphanMessages = null;
            for (Subitem subitemSetOldSubitem : subitemSetOld) {
                if (!subitemSetNew.contains(subitemSetOldSubitem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Subitem " + subitemSetOldSubitem + " since its itemId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<Orders> attachedOrdersSetNew = new HashSet<Orders>();
            for (Orders ordersSetNewOrdersToAttach : ordersSetNew) {
                ordersSetNewOrdersToAttach = em.getReference(ordersSetNewOrdersToAttach.getClass(), ordersSetNewOrdersToAttach.getOrderNumber());
                attachedOrdersSetNew.add(ordersSetNewOrdersToAttach);
            }
            ordersSetNew = attachedOrdersSetNew;
            item.setOrdersSet(ordersSetNew);
            Set<Subitem> attachedSubitemSetNew = new HashSet<Subitem>();
            for (Subitem subitemSetNewSubitemToAttach : subitemSetNew) {
                subitemSetNewSubitemToAttach = em.getReference(subitemSetNewSubitemToAttach.getClass(), subitemSetNewSubitemToAttach.getSubitemId());
                attachedSubitemSetNew.add(subitemSetNewSubitemToAttach);
            }
            subitemSetNew = attachedSubitemSetNew;
            item.setSubitemSet(subitemSetNew);
            item = em.merge(item);
            for (Orders ordersSetOldOrders : ordersSetOld) {
                if (!ordersSetNew.contains(ordersSetOldOrders)) {
                    ordersSetOldOrders.getItemSet().remove(item);
                    ordersSetOldOrders = em.merge(ordersSetOldOrders);
                }
            }
            for (Orders ordersSetNewOrders : ordersSetNew) {
                if (!ordersSetOld.contains(ordersSetNewOrders)) {
                    ordersSetNewOrders.getItemSet().add(item);
                    ordersSetNewOrders = em.merge(ordersSetNewOrders);
                }
            }
            for (Subitem subitemSetNewSubitem : subitemSetNew) {
                if (!subitemSetOld.contains(subitemSetNewSubitem)) {
                    Item oldItemIdOfSubitemSetNewSubitem = subitemSetNewSubitem.getItemId();
                    subitemSetNewSubitem.setItemId(item);
                    subitemSetNewSubitem = em.merge(subitemSetNewSubitem);
                    if (oldItemIdOfSubitemSetNewSubitem != null && !oldItemIdOfSubitemSetNewSubitem.equals(item)) {
                        oldItemIdOfSubitemSetNewSubitem.getSubitemSet().remove(subitemSetNewSubitem);
                        oldItemIdOfSubitemSetNewSubitem = em.merge(oldItemIdOfSubitemSetNewSubitem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = item.getItemId();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getItemId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<Subitem> subitemSetOrphanCheck = item.getSubitemSet();
            for (Subitem subitemSetOrphanCheckSubitem : subitemSetOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the Subitem " + subitemSetOrphanCheckSubitem + " in its subitemSet field has a non-nullable itemId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<Orders> ordersSet = item.getOrdersSet();
            for (Orders ordersSetOrders : ordersSet) {
                ordersSetOrders.getItemSet().remove(item);
                ordersSetOrders = em.merge(ordersSetOrders);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
