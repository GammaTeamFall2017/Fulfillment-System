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
public class SubitemJpaController implements Serializable {

    public SubitemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subitem subitem) {
        if (subitem.getOrdersSet() == null) {
            subitem.setOrdersSet(new HashSet<Orders>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item itemId = subitem.getItemId();
            if (itemId != null) {
                itemId = em.getReference(itemId.getClass(), itemId.getItemId());
                subitem.setItemId(itemId);
            }
            Set<Orders> attachedOrdersSet = new HashSet<Orders>();
            for (Orders ordersSetOrdersToAttach : subitem.getOrdersSet()) {
                ordersSetOrdersToAttach = em.getReference(ordersSetOrdersToAttach.getClass(), ordersSetOrdersToAttach.getOrderNumber());
                attachedOrdersSet.add(ordersSetOrdersToAttach);
            }
            subitem.setOrdersSet(attachedOrdersSet);
            em.persist(subitem);
            if (itemId != null) {
                itemId.getSubitemSet().add(subitem);
                itemId = em.merge(itemId);
            }
            for (Orders ordersSetOrders : subitem.getOrdersSet()) {
                ordersSetOrders.getSubitemSet().add(subitem);
                ordersSetOrders = em.merge(ordersSetOrders);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subitem subitem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subitem persistentSubitem = em.find(Subitem.class, subitem.getSubitemId());
            Item itemIdOld = persistentSubitem.getItemId();
            Item itemIdNew = subitem.getItemId();
            Set<Orders> ordersSetOld = persistentSubitem.getOrdersSet();
            Set<Orders> ordersSetNew = subitem.getOrdersSet();
            if (itemIdNew != null) {
                itemIdNew = em.getReference(itemIdNew.getClass(), itemIdNew.getItemId());
                subitem.setItemId(itemIdNew);
            }
            Set<Orders> attachedOrdersSetNew = new HashSet<Orders>();
            for (Orders ordersSetNewOrdersToAttach : ordersSetNew) {
                ordersSetNewOrdersToAttach = em.getReference(ordersSetNewOrdersToAttach.getClass(), ordersSetNewOrdersToAttach.getOrderNumber());
                attachedOrdersSetNew.add(ordersSetNewOrdersToAttach);
            }
            ordersSetNew = attachedOrdersSetNew;
            subitem.setOrdersSet(ordersSetNew);
            subitem = em.merge(subitem);
            if (itemIdOld != null && !itemIdOld.equals(itemIdNew)) {
                itemIdOld.getSubitemSet().remove(subitem);
                itemIdOld = em.merge(itemIdOld);
            }
            if (itemIdNew != null && !itemIdNew.equals(itemIdOld)) {
                itemIdNew.getSubitemSet().add(subitem);
                itemIdNew = em.merge(itemIdNew);
            }
            for (Orders ordersSetOldOrders : ordersSetOld) {
                if (!ordersSetNew.contains(ordersSetOldOrders)) {
                    ordersSetOldOrders.getSubitemSet().remove(subitem);
                    ordersSetOldOrders = em.merge(ordersSetOldOrders);
                }
            }
            for (Orders ordersSetNewOrders : ordersSetNew) {
                if (!ordersSetOld.contains(ordersSetNewOrders)) {
                    ordersSetNewOrders.getSubitemSet().add(subitem);
                    ordersSetNewOrders = em.merge(ordersSetNewOrders);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subitem.getSubitemId();
                if (findSubitem(id) == null) {
                    throw new NonexistentEntityException("The subitem with id " + id + " no longer exists.");
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
            Subitem subitem;
            try {
                subitem = em.getReference(Subitem.class, id);
                subitem.getSubitemId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subitem with id " + id + " no longer exists.", enfe);
            }
            Item itemId = subitem.getItemId();
            if (itemId != null) {
                itemId.getSubitemSet().remove(subitem);
                itemId = em.merge(itemId);
            }
            Set<Orders> ordersSet = subitem.getOrdersSet();
            for (Orders ordersSetOrders : ordersSet) {
                ordersSetOrders.getSubitemSet().remove(subitem);
                ordersSetOrders = em.merge(ordersSetOrders);
            }
            em.remove(subitem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subitem> findSubitemEntities() {
        return findSubitemEntities(true, -1, -1);
    }

    public List<Subitem> findSubitemEntities(int maxResults, int firstResult) {
        return findSubitemEntities(false, maxResults, firstResult);
    }

    private List<Subitem> findSubitemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subitem.class));
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

    public Subitem findSubitem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subitem.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubitemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subitem> rt = cq.from(Subitem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
