package cs4310.fulfillment.program.Model;

import cs4310.fulfillment.program.Model.exceptions.NonexistentEntityException;
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
public class SubitemJpaController implements Serializable {

    public SubitemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subitem subitem) {
        if (subitem.getItemsOrderedCollection() == null) {
            subitem.setItemsOrderedCollection(new ArrayList<ItemsOrdered>());
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
            Collection<ItemsOrdered> attachedItemsOrderedCollection = new ArrayList<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedCollectionItemsOrderedToAttach : subitem.getItemsOrderedCollection()) {
                itemsOrderedCollectionItemsOrderedToAttach = em.getReference(itemsOrderedCollectionItemsOrderedToAttach.getClass(), itemsOrderedCollectionItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedCollection.add(itemsOrderedCollectionItemsOrderedToAttach);
            }
            subitem.setItemsOrderedCollection(attachedItemsOrderedCollection);
            em.persist(subitem);
            if (itemId != null) {
                itemId.getSubitemCollection().add(subitem);
                itemId = em.merge(itemId);
            }
            for (ItemsOrdered itemsOrderedCollectionItemsOrdered : subitem.getItemsOrderedCollection()) {
                Subitem oldSubitemOrderedOfItemsOrderedCollectionItemsOrdered = itemsOrderedCollectionItemsOrdered.getSubitemOrdered();
                itemsOrderedCollectionItemsOrdered.setSubitemOrdered(subitem);
                itemsOrderedCollectionItemsOrdered = em.merge(itemsOrderedCollectionItemsOrdered);
                if (oldSubitemOrderedOfItemsOrderedCollectionItemsOrdered != null) {
                    oldSubitemOrderedOfItemsOrderedCollectionItemsOrdered.getItemsOrderedCollection().remove(itemsOrderedCollectionItemsOrdered);
                    oldSubitemOrderedOfItemsOrderedCollectionItemsOrdered = em.merge(oldSubitemOrderedOfItemsOrderedCollectionItemsOrdered);
                }
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
            Collection<ItemsOrdered> itemsOrderedCollectionOld = persistentSubitem.getItemsOrderedCollection();
            Collection<ItemsOrdered> itemsOrderedCollectionNew = subitem.getItemsOrderedCollection();
            if (itemIdNew != null) {
                itemIdNew = em.getReference(itemIdNew.getClass(), itemIdNew.getItemId());
                subitem.setItemId(itemIdNew);
            }
            Collection<ItemsOrdered> attachedItemsOrderedCollectionNew = new ArrayList<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedCollectionNewItemsOrderedToAttach : itemsOrderedCollectionNew) {
                itemsOrderedCollectionNewItemsOrderedToAttach = em.getReference(itemsOrderedCollectionNewItemsOrderedToAttach.getClass(), itemsOrderedCollectionNewItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedCollectionNew.add(itemsOrderedCollectionNewItemsOrderedToAttach);
            }
            itemsOrderedCollectionNew = attachedItemsOrderedCollectionNew;
            subitem.setItemsOrderedCollection(itemsOrderedCollectionNew);
            subitem = em.merge(subitem);
            if (itemIdOld != null && !itemIdOld.equals(itemIdNew)) {
                itemIdOld.getSubitemCollection().remove(subitem);
                itemIdOld = em.merge(itemIdOld);
            }
            if (itemIdNew != null && !itemIdNew.equals(itemIdOld)) {
                itemIdNew.getSubitemCollection().add(subitem);
                itemIdNew = em.merge(itemIdNew);
            }
            for (ItemsOrdered itemsOrderedCollectionOldItemsOrdered : itemsOrderedCollectionOld) {
                if (!itemsOrderedCollectionNew.contains(itemsOrderedCollectionOldItemsOrdered)) {
                    itemsOrderedCollectionOldItemsOrdered.setSubitemOrdered(null);
                    itemsOrderedCollectionOldItemsOrdered = em.merge(itemsOrderedCollectionOldItemsOrdered);
                }
            }
            for (ItemsOrdered itemsOrderedCollectionNewItemsOrdered : itemsOrderedCollectionNew) {
                if (!itemsOrderedCollectionOld.contains(itemsOrderedCollectionNewItemsOrdered)) {
                    Subitem oldSubitemOrderedOfItemsOrderedCollectionNewItemsOrdered = itemsOrderedCollectionNewItemsOrdered.getSubitemOrdered();
                    itemsOrderedCollectionNewItemsOrdered.setSubitemOrdered(subitem);
                    itemsOrderedCollectionNewItemsOrdered = em.merge(itemsOrderedCollectionNewItemsOrdered);
                    if (oldSubitemOrderedOfItemsOrderedCollectionNewItemsOrdered != null && !oldSubitemOrderedOfItemsOrderedCollectionNewItemsOrdered.equals(subitem)) {
                        oldSubitemOrderedOfItemsOrderedCollectionNewItemsOrdered.getItemsOrderedCollection().remove(itemsOrderedCollectionNewItemsOrdered);
                        oldSubitemOrderedOfItemsOrderedCollectionNewItemsOrdered = em.merge(oldSubitemOrderedOfItemsOrderedCollectionNewItemsOrdered);
                    }
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
                itemId.getSubitemCollection().remove(subitem);
                itemId = em.merge(itemId);
            }
            Collection<ItemsOrdered> itemsOrderedCollection = subitem.getItemsOrderedCollection();
            for (ItemsOrdered itemsOrderedCollectionItemsOrdered : itemsOrderedCollection) {
                itemsOrderedCollectionItemsOrdered.setSubitemOrdered(null);
                itemsOrderedCollectionItemsOrdered = em.merge(itemsOrderedCollectionItemsOrdered);
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
