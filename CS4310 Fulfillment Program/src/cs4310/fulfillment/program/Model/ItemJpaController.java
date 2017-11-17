package cs4310.fulfillment.program.Model;

import cs4310.fulfillment.program.exceptions.IllegalOrphanException;
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
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) {
        if (item.getItemsOrderedCollection() == null) {
            item.setItemsOrderedCollection(new ArrayList<ItemsOrdered>());
        }
        if (item.getSubitemCollection() == null) {
            item.setSubitemCollection(new ArrayList<Subitem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ItemsOrdered> attachedItemsOrderedCollection = new ArrayList<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedCollectionItemsOrderedToAttach : item.getItemsOrderedCollection()) {
                itemsOrderedCollectionItemsOrderedToAttach = em.getReference(itemsOrderedCollectionItemsOrderedToAttach.getClass(), itemsOrderedCollectionItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedCollection.add(itemsOrderedCollectionItemsOrderedToAttach);
            }
            item.setItemsOrderedCollection(attachedItemsOrderedCollection);
            Collection<Subitem> attachedSubitemCollection = new ArrayList<Subitem>();
            for (Subitem subitemCollectionSubitemToAttach : item.getSubitemCollection()) {
                subitemCollectionSubitemToAttach = em.getReference(subitemCollectionSubitemToAttach.getClass(), subitemCollectionSubitemToAttach.getSubitemId());
                attachedSubitemCollection.add(subitemCollectionSubitemToAttach);
            }
            item.setSubitemCollection(attachedSubitemCollection);
            em.persist(item);
            for (ItemsOrdered itemsOrderedCollectionItemsOrdered : item.getItemsOrderedCollection()) {
                Item oldItemInOrderOfItemsOrderedCollectionItemsOrdered = itemsOrderedCollectionItemsOrdered.getItemInOrder();
                itemsOrderedCollectionItemsOrdered.setItemInOrder(item);
                itemsOrderedCollectionItemsOrdered = em.merge(itemsOrderedCollectionItemsOrdered);
                if (oldItemInOrderOfItemsOrderedCollectionItemsOrdered != null) {
                    oldItemInOrderOfItemsOrderedCollectionItemsOrdered.getItemsOrderedCollection().remove(itemsOrderedCollectionItemsOrdered);
                    oldItemInOrderOfItemsOrderedCollectionItemsOrdered = em.merge(oldItemInOrderOfItemsOrderedCollectionItemsOrdered);
                }
            }
            for (Subitem subitemCollectionSubitem : item.getSubitemCollection()) {
                Item oldItemIdOfSubitemCollectionSubitem = subitemCollectionSubitem.getItemId();
                subitemCollectionSubitem.setItemId(item);
                subitemCollectionSubitem = em.merge(subitemCollectionSubitem);
                if (oldItemIdOfSubitemCollectionSubitem != null) {
                    oldItemIdOfSubitemCollectionSubitem.getSubitemCollection().remove(subitemCollectionSubitem);
                    oldItemIdOfSubitemCollectionSubitem = em.merge(oldItemIdOfSubitemCollectionSubitem);
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
            Collection<ItemsOrdered> itemsOrderedCollectionOld = persistentItem.getItemsOrderedCollection();
            Collection<ItemsOrdered> itemsOrderedCollectionNew = item.getItemsOrderedCollection();
            Collection<Subitem> subitemCollectionOld = persistentItem.getSubitemCollection();
            Collection<Subitem> subitemCollectionNew = item.getSubitemCollection();
            List<String> illegalOrphanMessages = null;
            for (ItemsOrdered itemsOrderedCollectionOldItemsOrdered : itemsOrderedCollectionOld) {
                if (!itemsOrderedCollectionNew.contains(itemsOrderedCollectionOldItemsOrdered)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemsOrdered " + itemsOrderedCollectionOldItemsOrdered + " since its itemInOrder field is not nullable.");
                }
            }
            for (Subitem subitemCollectionOldSubitem : subitemCollectionOld) {
                if (!subitemCollectionNew.contains(subitemCollectionOldSubitem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Subitem " + subitemCollectionOldSubitem + " since its itemId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ItemsOrdered> attachedItemsOrderedCollectionNew = new ArrayList<ItemsOrdered>();
            for (ItemsOrdered itemsOrderedCollectionNewItemsOrderedToAttach : itemsOrderedCollectionNew) {
                itemsOrderedCollectionNewItemsOrderedToAttach = em.getReference(itemsOrderedCollectionNewItemsOrderedToAttach.getClass(), itemsOrderedCollectionNewItemsOrderedToAttach.getLineItemId());
                attachedItemsOrderedCollectionNew.add(itemsOrderedCollectionNewItemsOrderedToAttach);
            }
            itemsOrderedCollectionNew = attachedItemsOrderedCollectionNew;
            item.setItemsOrderedCollection(itemsOrderedCollectionNew);
            Collection<Subitem> attachedSubitemCollectionNew = new ArrayList<Subitem>();
            for (Subitem subitemCollectionNewSubitemToAttach : subitemCollectionNew) {
                subitemCollectionNewSubitemToAttach = em.getReference(subitemCollectionNewSubitemToAttach.getClass(), subitemCollectionNewSubitemToAttach.getSubitemId());
                attachedSubitemCollectionNew.add(subitemCollectionNewSubitemToAttach);
            }
            subitemCollectionNew = attachedSubitemCollectionNew;
            item.setSubitemCollection(subitemCollectionNew);
            item = em.merge(item);
            for (ItemsOrdered itemsOrderedCollectionNewItemsOrdered : itemsOrderedCollectionNew) {
                if (!itemsOrderedCollectionOld.contains(itemsOrderedCollectionNewItemsOrdered)) {
                    Item oldItemInOrderOfItemsOrderedCollectionNewItemsOrdered = itemsOrderedCollectionNewItemsOrdered.getItemInOrder();
                    itemsOrderedCollectionNewItemsOrdered.setItemInOrder(item);
                    itemsOrderedCollectionNewItemsOrdered = em.merge(itemsOrderedCollectionNewItemsOrdered);
                    if (oldItemInOrderOfItemsOrderedCollectionNewItemsOrdered != null && !oldItemInOrderOfItemsOrderedCollectionNewItemsOrdered.equals(item)) {
                        oldItemInOrderOfItemsOrderedCollectionNewItemsOrdered.getItemsOrderedCollection().remove(itemsOrderedCollectionNewItemsOrdered);
                        oldItemInOrderOfItemsOrderedCollectionNewItemsOrdered = em.merge(oldItemInOrderOfItemsOrderedCollectionNewItemsOrdered);
                    }
                }
            }
            for (Subitem subitemCollectionNewSubitem : subitemCollectionNew) {
                if (!subitemCollectionOld.contains(subitemCollectionNewSubitem)) {
                    Item oldItemIdOfSubitemCollectionNewSubitem = subitemCollectionNewSubitem.getItemId();
                    subitemCollectionNewSubitem.setItemId(item);
                    subitemCollectionNewSubitem = em.merge(subitemCollectionNewSubitem);
                    if (oldItemIdOfSubitemCollectionNewSubitem != null && !oldItemIdOfSubitemCollectionNewSubitem.equals(item)) {
                        oldItemIdOfSubitemCollectionNewSubitem.getSubitemCollection().remove(subitemCollectionNewSubitem);
                        oldItemIdOfSubitemCollectionNewSubitem = em.merge(oldItemIdOfSubitemCollectionNewSubitem);
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
            Collection<ItemsOrdered> itemsOrderedCollectionOrphanCheck = item.getItemsOrderedCollection();
            for (ItemsOrdered itemsOrderedCollectionOrphanCheckItemsOrdered : itemsOrderedCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the ItemsOrdered " + itemsOrderedCollectionOrphanCheckItemsOrdered + " in its itemsOrderedCollection field has a non-nullable itemInOrder field.");
            }
            Collection<Subitem> subitemCollectionOrphanCheck = item.getSubitemCollection();
            for (Subitem subitemCollectionOrphanCheckSubitem : subitemCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the Subitem " + subitemCollectionOrphanCheckSubitem + " in its subitemCollection field has a non-nullable itemId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
