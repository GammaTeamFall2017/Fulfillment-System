/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.Employee;
import cs4310.fulfillment.program.Model.Item;
import cs4310.fulfillment.program.Model.ItemsOrdered;
import cs4310.fulfillment.program.Model.Orders;
import cs4310.fulfillment.program.Model.Subitem;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author diana
 * Class to test out DbUtilityCollection functions
 */
public class TestDbUtilityCollection {
    public static void main(String[] args) {
        DbUtilityCollection instance = new DbUtilityCollection();
        /* Employee employee = instance.getEmployeeByUsername("uname");
        System.out.println("Username: " + employee.getUsername() + " Role: " +employee.getRole());

        System.out.println("True or not: " + instance.authenticateEmployee("uname", "pw"));
        
        instance.displayOrderIDs();
        
        Orders order = new Orders();
        System.out.println("order id: )" + order.getOrderNumber());*/
        
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date(cal.getTimeInMillis());
        
        // Display new Items test:
        Orders order = new Orders();
        order.setOrderNumber(2);
        order.setDateCreated(currentDate);
        
        Item item1 = instance.getItemByID(1);
        Item item2 = instance.getItemByID(2);
        Subitem subitem1 = instance.getSubitemByID(1);
        Subitem subitem3 = instance.getSubitemByID(3);
        Subitem subitem4 = instance.getSubitemByID(4);
        Subitem subitem6 = instance.getSubitemByID(6);
        Subitem subitem8 = instance.getSubitemByID(8);
        
        Set<ItemsOrdered> newItemsInOrderSet = new HashSet<ItemsOrdered>();
        ItemsOrdered itemInOrder = instance.addItemsToOrder(order, item1, subitem3, 1, "none");
        itemInOrder.setLineItemId(1);
        newItemsInOrderSet.add(itemInOrder);
        
        ItemsOrdered itemInOrder2 = instance.addItemsToOrder(order, item1, subitem4, 1, "none");
        itemInOrder2.setLineItemId(2);
        newItemsInOrderSet.add(itemInOrder2);
        
        ItemsOrdered itemInOrder3 = instance.addItemsToOrder(order, item2, subitem6, 1, "none");
        itemInOrder3.setLineItemId(3);
        newItemsInOrderSet.add(itemInOrder3);

        ItemsOrdered itemInOrder4 = instance.addItemsToOrder(order, item2, subitem8, 1, "none");
        itemInOrder4.setLineItemId(4);
        newItemsInOrderSet.add(itemInOrder4);
        
        instance.displayKitchenOrderLineItems(newItemsInOrderSet);
        //System.out.println("Print set: " + newItemsInOrderSet);
        
        
    }
}
