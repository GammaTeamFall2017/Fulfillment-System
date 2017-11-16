package cs4310.fulfillment.program.Model;

import cs4310.fulfillment.program.Model.Employee;
import cs4310.fulfillment.program.Model.EmployeeJpaController;
import cs4310.fulfillment.program.Model.Item;
import cs4310.fulfillment.program.Model.ItemJpaController;
import cs4310.fulfillment.program.Model.ItemsOrdered;
import cs4310.fulfillment.program.Model.Orders;
import cs4310.fulfillment.program.Model.OrdersJpaController;
import cs4310.fulfillment.program.Model.Subitem;
import cs4310.fulfillment.program.Model.SubitemJpaController;
import cs4310.fulfillment.program.exceptions.IllegalOrphanException;
import cs4310.fulfillment.program.exceptions.NonexistentEntityException;
import static java.lang.reflect.Array.set;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author diana
 * DbUtilityCollection is a wrapper using Entity Controller functions, modified for specific Scene Views
 */
public class DbUtilityCollection {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
    private OrdersJpaController orderInstance = new OrdersJpaController(emf);
    private EmployeeJpaController employeeInstance = new EmployeeJpaController(emf);
    private ItemJpaController itemInstance = new ItemJpaController(emf);
    private SubitemJpaController subItemInstance = new SubitemJpaController(emf);
    private ItemsOrderedJpaController itemsOrderedInstance = new ItemsOrderedJpaController(emf);
    
    //***** Create New Order Functions *****//
    
    public ItemsOrdered createNewItemsOrdered(ItemsOrdered itemsOrdered){
        return itemsOrderedInstance.createAndReturn(itemsOrdered);
    }
    
    // First instantiate a new order by creating it in the database before adding ItemsOrdered to it
    // Needed to generate an automatic primary key ID for an Orders object
    public Orders createNewOrder(){
        Orders newOrder = new Orders();
        newOrder.setTableNumber("No table");
        newOrder.setKitchenComplete(Boolean.FALSE);
        newOrder.setRequestWaitstaff(Boolean.FALSE);
        newOrder.setOrderPaid(Boolean.FALSE);
        BigDecimal num = new BigDecimal(0.00);
        newOrder.setTotalPrice(num);
        return orderInstance.createAndReturn(newOrder);
    }
    
    // Edit Order after adding all of the ItemsOrdered to it and submitting order at the Menu/Order scene
    public void updateNewOrder(Orders newOrder, Collection<ItemsOrdered> itemSet, String newTable, BigDecimal newPrice, Date timeCreated) throws NonexistentEntityException, Exception{
        newOrder.setItemsOrderedCollection(itemSet);
        newOrder.setTableNumber(newTable);
        newOrder.setTotalPrice(newPrice);
        newOrder.setDateCreated(timeCreated);
        newOrder.setKitchenComplete(Boolean.FALSE);
        newOrder.setRequestWaitstaff(Boolean.FALSE);
        newOrder.setOrderPaid(Boolean.FALSE);
        orderInstance.edit(newOrder);
    }
    
    // If new order is canceled, need to remove it from the database
    public void removeOrder(Orders newOrder) throws IllegalOrphanException, cs4310.fulfillment.program.exceptions.NonexistentEntityException{
        orderInstance.destroy(newOrder.getOrderNumber());
    }
    
    public void removeOrderLineItem(ItemsOrdered lineItem) throws IllegalOrphanException, cs4310.fulfillment.program.exceptions.NonexistentEntityException{
        itemsOrderedInstance.destroy(lineItem.getLineItemId());
    }
    
    public Item getItemByID(Integer id){
        return itemInstance.findItem(id);
    }
    
    public Subitem getSubitemByID(Integer id){
        return subItemInstance.findSubitem(id);
    }
    
    // Keep using this function to add selected items in the current order and store this function's results into a Set of ItemsOrdered: Set<ItemsOrdered>
    public ItemsOrdered addItemsToOrder(Orders order, Item item, Subitem subitem, int itemQuantity, String specialInstructions){
        ItemsOrdered newItemOrdered = new ItemsOrdered();
        newItemOrdered.setOrderId(order);
        newItemOrdered.setItemInOrder(item);
        newItemOrdered.setSubitemOrdered(subitem);
        newItemOrdered.setItemQuantity(itemQuantity);
        newItemOrdered.setSpecialInstructions(specialInstructions);
        return newItemOrdered;
    }
    
    //***** END Create New Order Functions *****//
    
    
    //*****     Update Order Functions    *****//
    
    // Update Order when it's done in the kitchen
    public void updateKitchenOrder(Orders order) throws NonexistentEntityException, Exception{
        order.setKitchenComplete(true);
        orderInstance.edit(order);
    }
    
    // Update Order when it's paid
    public void updatePayOrder(Orders order) throws NonexistentEntityException, Exception{
        order.setOrderPaid(true);
        orderInstance.edit(order);
    }
    
    // Update Order when waitstaff help is requested
    public void requestWaitstaffUpdate(Orders order) throws NonexistentEntityException, Exception{
        order.setRequestWaitstaff(true);
        orderInstance.edit(order);
    }
    
    // Update Order when waitstaff help is done
    public void finishRequestWaitstaffUpdate(Orders order) throws NonexistentEntityException, Exception{
        order.setRequestWaitstaff(false);
        orderInstance.edit(order);
    }
    
    //*****  END Update Order Functions   *****//
    
    
    //*****   Item related Functions      *****//
    
    // Gets all Items from the database. Use this function on a new list of items to save the retrieved list.
    // Example: List<Item> allItems = getAllItems();
    public List<Item> getAllItems(){
       return itemInstance.findItemEntities();
    }
    
    // Calculate Item price with or without addons
    public BigDecimal getTotalItemPrice(Collection<ItemsOrdered> itemSet, Item item){
        BigDecimal total = item.getItemPrice(); // save total for Item
        for (Iterator<ItemsOrdered> i = itemSet.iterator(); i.hasNext();) {
            ItemsOrdered e =  i.next();
            
            Item tempItem = e.getItemInOrder();
            Subitem tempSubitem = e.getSubitemOrdered();
            if(tempItem.equals(item)){
                total = total.add(tempSubitem.getSubitemPrice());
            }
        }
        return total;
    }
    
    // Calculate Item ETA
    public int getTotalItemETA(Collection<ItemsOrdered> itemSet, Item item){
        int total = item.getItemEta(); // save total for Item
        for (Iterator<ItemsOrdered> i = itemSet.iterator(); i.hasNext();) {
            ItemsOrdered e =  i.next();
            
            Item tempItem = e.getItemInOrder();
            Subitem tempSubitem = e.getSubitemOrdered();
            if(tempItem.equals(item)){
                total = total + tempSubitem.getSubitemEta();
            }
        }
        return total;
    }
    
    // Display an order's line items
    public void displayOrderLineItems(Collection<ItemsOrdered> itemSet){
        Item saveItem = null; // save previous item to compare
        for (Iterator<ItemsOrdered> i = itemSet.iterator(); i.hasNext();) {
            ItemsOrdered e =  i.next();
            
            if (e.getItemInOrder().equals(saveItem)) {
               ;
            }else{
                Item item = e.getItemInOrder();
                saveItem = item;
                
                System.out.println("\tItem: " + item.getItemName() +"\t\t\t$" + getTotalItemPrice(itemSet, item)); // Have to replace this with Labels or table cells we can attach the data to
               
            }
            Subitem subitem = e.getSubitemOrdered();
            System.out.println("\t\tSubitem: " + subitem.getSubitemName()); // Have to replace this with Labels or table cells we can attach the data to
        }
    }
    
    // Display the kitchen's view of an order's line items, needed for displayKitchenOrders()
    public void displayKitchenOrderLineItems(Collection<ItemsOrdered> itemSet){
        Item saveItem = null; // save previous item to compare
        for (Iterator<ItemsOrdered> i = itemSet.iterator(); i.hasNext();) {
            ItemsOrdered e =  i.next();
            Orders order = e.getOrderId();
            if (e.getItemInOrder().equals(saveItem)) {
               ;
            }else{
                Item item = e.getItemInOrder();
                saveItem = item;
                
                System.out.println("\tItem: " + item.getItemName() +"\t\t\t$" + getTotalItemPrice(itemSet, item)); // Have to replace this with Labels or table cells we can attach the data to
                // Kitchen Timer
                Date orderTime = order.getDateCreated();
                Calendar cal = Calendar.getInstance();
                Date currentTime = new Date(cal.getTimeInMillis());
                // Calculate remaining time
                long timeDifferenceMins = (currentTime.getTime() - orderTime.getTime())/ (60 * 1000) % 60; // time difference in minutes
                long timeAllocatedForOrder = (long)getTotalItemETA(itemSet, item);
                long minutesRemaining = timeAllocatedForOrder - timeDifferenceMins;
                if(minutesRemaining > 0){
                    System.out.println("Total time for order remaining: " + minutesRemaining + " minutes"); // Have to replace this with Labels or table cells we can attach the data to
                }else{
                    System.out.println("Total time for order remaining: 0 minutes"); // Have to replace this with Labels or table cells we can attach the data to
                }
            }
            Subitem subitem = e.getSubitemOrdered();
            System.out.println("\t\tSubitem: " + subitem.getSubitemName()); // Have to replace this with Labels or table cells we can attach the data to
        }
    }
    
    public void displayItemsOnMenu(){
        List<Item> storeItems = itemInstance.findItemEntities();
        for (Item currentItem: storeItems){ 
            System.out.println("Item: " + currentItem.getItemName()); // Have to replace this with buttons we can attach the data to
        }
    }
    // Display whole list of kitchen orders, needs displayKitchenOrderLineItems()
    public void displayKitchenOrders(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
        OrdersJpaController orderInstance2 = new OrdersJpaController(emf);
        List<Orders> orderList = orderInstance2.findOrdersEntities(); 
        
        for (Orders currentOrder: orderList){  
            if(currentOrder.getKitchenComplete() == false){
                System.out.println(currentOrder.getOrderNumber());
                displayKitchenOrderLineItems(currentOrder.getItemsOrderedCollection());
                    
            }
        }
    }
    //Testing
    public int getTotalOrder(){
        return orderInstance.getOrdersCount();
    }
    //end testing
    //***** END Item related Functions  *****//
    
    /*
    // Sample function to display all orders and an order's items and subitems that were ordere
    public void displayOrderIDs() {
        List<Orders> orderList = orderInstance.findOrdersEntities(); 
        
        for (Orders currentOrder: orderList){      
            Set<ItemsOrdered> lineItemSet = currentOrder.getItemsOrderedSet();
            Integer savedOrderNumber = currentOrder.getOrderNumber();
            System.out.println("For Order ID: " + savedOrderNumber);
            Integer savedItemNumber = 0;
            for (ItemsOrdered currentLineItem: lineItemSet){
                Orders order = currentLineItem.getOrderId();
                Item item = currentLineItem.getItemInOrder();
                Subitem subitem = currentLineItem.getSubitemOrdered();
                
                if(savedItemNumber.equals(item.getItemId())){
                    ;
                }else{
                    System.out.println("\tItem ID: " + item.getItemId() + " Item name: " + item.getItemName());
                }
              
                savedItemNumber = item.getItemId();
                if(order.getOrderNumber().equals(savedOrderNumber)){
                    
                    System.out.println("\t\tSubitem ID: " + subitem.getSubitemId() + " Subitem name: " + subitem.getSubitemName());
                }
                    
            }
        }
    }
    */
    
    //*****  Employee related Functions  *****//
    
    // input is from input fields on UI
    public boolean authenticateEmployee(String username, String password){
        List<Employee> employeeList = employeeInstance.findEmployeeEntities();
         for (Employee currentEmployee: employeeList){
             if(currentEmployee.getUsername().equals(username)){
                 if(currentEmployee.getPassword().equals(password)){
                     return true;
                 }
                 else{
                     return false;
                 }
             }
         }
         // if username not in database, return false
         return false;   
    }
    
    public Employee getEmployeeByUsername(String username){      
        List<Employee> employeeList = employeeInstance.findEmployeeEntities();
        for (Employee currentEmployee: employeeList){
            if(currentEmployee.getUsername().equals(username)){
               return currentEmployee;         
            }
        }
        return null;
    }
    
    public Employee createNewEmployee(Employee newEmployee){
        return employeeInstance.createAndReturn(newEmployee);
    }
    
    //***** END Employee related Functions  *****//
    
}

