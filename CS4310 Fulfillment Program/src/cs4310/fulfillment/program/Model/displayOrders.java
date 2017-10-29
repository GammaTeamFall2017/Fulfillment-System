/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Model;

import cs4310.fulfillment.program.Model.Employee;
import cs4310.fulfillment.program.Model.EmployeeJpaController;
import cs4310.fulfillment.program.Model.Item;
import cs4310.fulfillment.program.Model.Orders;
import cs4310.fulfillment.program.Model.OrdersJpaController;
import cs4310.fulfillment.program.Model.Subitem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.Order;

/**
 *
 * @author diana
 */
public class displayOrders {
    public static void display() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
        OrdersJpaController instance = new OrdersJpaController(emf);
        List<Orders> orderList = instance.findOrdersEntities();  
        /*
        System.out.println("create");
        Orders newOrder = new Orders();
        //employee.setEmployeeId(2);
        newOrder.setTableNumber("3");
        newOrder.setTotalPrice(new BigDecimal("89.99"));
        */
   
        //instance.create(newOrder);
        
        for (Orders currentOrder: orderList){
            System.out.println("Order Number: " + currentOrder.getOrderNumber());
            Set<Item> itemList = currentOrder.getItemSet();
            for (Item currentItem: itemList){
                System.out.println("Item: " + currentItem.getItemName());
                 Set<Subitem> subItemList = currentOrder.getSubitemSet();
                 for (Subitem currentSubItem: subItemList){
                     System.out.println("Sub Item: " + currentSubItem.getSubitemName());
                 }
            }
           
        }
        
    }
    public static void main(String[] args) {
        display();
    }
}
