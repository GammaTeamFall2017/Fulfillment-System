/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Employee;
import cs4310.fulfillment.program.Model.Item;
import cs4310.fulfillment.program.Model.ItemsOrdered;
import cs4310.fulfillment.program.Model.Orders;
import cs4310.fulfillment.program.Model.Subitem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author diana
 * Class to test out DbUtilityCollection functions, and connection to database
 *  WARNING: Please look at SDS Section 7 for information on what is allowed in the database itself
 *  Pay special attention to the fields where NULL is prohibited.
 *  - Cannot be NULL fields are constraints put on the database itself, if an attempt is made to create database entries that disobey the constraints, 
 *    this program will possibly crash. It is meant that empty values are prevented from being entered into the database in the Controller code itself.
 */
public class TestDbUtilityCollection {
    public static void main(String[] args) throws Exception {
        DbUtilityCollection instance = new DbUtilityCollection();
        /* Employee employee = instance.getEmployeeByUsername("uname");
        System.out.println("Username: " + employee.getUsername() + " Role: " +employee.getRole());

        System.out.println("True or not: " + instance.authenticateEmployee("uname", "pw"));
        
        instance.displayOrderIDs();
        
        Orders order = new Orders();
        System.out.println("order id: )" + order.getOrderNumber());*/
        ////////////////////////////////////////////////////////////////////////////////////////////
        
        
        // Create new order and line items example, then remove the new order at the end
        
        // This order will not be saved in the database, but is an example of how to add items and subitems to an order
        Orders order = instance.createNewOrder(); // creates a new order entry in the database, to generate a key for order
    
        // First get items and subitems from database that we want to put in our order
        Item item1 = instance.getItemByID(1);
        Item item2 = instance.getItemByID(2);
        Subitem subitem3 = instance.getSubitemByID(3);
        Subitem subitem4 = instance.getSubitemByID(4);
        Subitem subitem6 = instance.getSubitemByID(6);
        Subitem subitem8 = instance.getSubitemByID(8);
        
        // Then we create a new set of ItemsOrdered and add the items and their subitems to the set.
        // Add rows of the same item until all of its subitems are added
        // If an item is ordered with no subitems, leave the subitem field null, and we would only need one row for this type of item
        Collection<ItemsOrdered> tempNewItemsInOrder = new ArrayList<ItemsOrdered>();
        ItemsOrdered itemInOrder = instance.addItemsToOrder(order, item1, subitem3, 1, "none");
        tempNewItemsInOrder.add(itemInOrder);
        // System.out.println(itemInOrder); // Debug statement
        
        ItemsOrdered itemInOrder2 = instance.addItemsToOrder(order, item1, subitem4, 1, "none");
        tempNewItemsInOrder.add(itemInOrder2);
        // System.out.println(itemInOrder2); // Debug statement
        
        // Calculate Total Price of 1st item and its subitems
        BigDecimal tempTotalPrice = new BigDecimal(0.00);
        tempTotalPrice.add(instance.getTotalItemPrice(tempNewItemsInOrder, item1));
        System.out.println("Test price item 1:" + tempTotalPrice);
        
        ItemsOrdered itemInOrder3 = instance.addItemsToOrder(order, item2, subitem6, 1, "none");
        tempNewItemsInOrder.add(itemInOrder3);
        // System.out.println(itemInOrder3); // Debug statement
        
        ItemsOrdered itemInOrder4 = instance.addItemsToOrder(order, item2, subitem8, 1, "none");
        tempNewItemsInOrder.add(itemInOrder4);
        // System.out.println(itemInOrder4); // Debug statement
        
        // Calculate Total Price of 2nd item and its subitems and add to total
        tempTotalPrice.add(instance.getTotalItemPrice(tempNewItemsInOrder, item2));
        System.out.println("Test price item 2:" + tempTotalPrice);
        
        /* // Debug statements
        // Test what was saved in the line item collection
        System.out.println("Test our set:");
        // If these are the items we want in the order, first add each line item as entries into the database to ItemsOrdered 
        for (Iterator<ItemsOrdered> i = tempNewItemsInOrder.iterator(); i.hasNext();) {
            ItemsOrdered e =  i.next();
            System.out.println("sub item id: " + e.getSubitemOrdered());

        } */
        
        Collection<ItemsOrdered> newItemsInOrder = new ArrayList<ItemsOrdered>(); // Save the ItemsOrdered created in dataabase to new set
        // If these are the items we want in the order, first add each line item as entries into the database to ItemsOrdered 
        for (Iterator<ItemsOrdered> i = tempNewItemsInOrder.iterator(); i.hasNext();) {
            ItemsOrdered e =  i.next();
            
            ItemsOrdered tempItemsOrdered = instance.createNewItemsOrdered(e);
            newItemsInOrder.add(tempItemsOrdered);
            // System.out.println("line item id: " + e.getLineItemId()); // Debug statement

        }
        
        
        
        // Then we can attach these items and subitems in our order to the order object itself:
        order.setItemsOrderedCollection(newItemsInOrder);
        
        // Get current time/date then set it in order during updatde
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date(cal.getTimeInMillis());

        
        // Select our order to update, attach our new items, set current price and current date, and proceed to update the database entry for this order
        instance.updateNewOrder(order, newItemsInOrder, "No Table", tempTotalPrice, currentDate);
        
        // Display what items and subitems we have added so far as kitchen view line items (just to demo)

        System.out.println("newItemsInOrderSet: ");
        instance.displayKitchenOrderLineItems(order.getItemsOrderedCollection());
        
        // Remove the new test order and its line items created in the to clean up the database. If successful, nothing new seems to appear in the database.
        // Comment out the next line if you want to see the order in the database.
        Collection getLineItems = order.getItemsOrderedCollection(); // get the order's collection of line items
         for (Iterator<ItemsOrdered> i = getLineItems.iterator(); i.hasNext();) {
            ItemsOrdered e =  i.next();
            instance.removeOrderLineItem(e); // Remove every line item associate with the order you want to remove
        }
        instance.removeOrder(order); // Remove the order itself

        
    }
}
