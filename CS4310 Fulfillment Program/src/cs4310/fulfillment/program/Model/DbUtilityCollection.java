/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Model;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author diana
 */
public class DbUtilityCollection {
    // Sample function to display all orders and an order's items and subitems that were ordered
    public static void displayOrderIDs() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
        OrdersJpaController instance = new OrdersJpaController(emf);
        List<Orders> orderList = instance.findOrdersEntities(); 
        
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
                    System.out.println("Item ID: " + item.getItemId());
                }
              
                savedItemNumber = item.getItemId();
                if(order.getOrderNumber().equals(savedOrderNumber)){
                    
                    System.out.println("Subitem ID: " + subitem.getSubitemId());
                }
                    
            }
        }
    }    
    public static void main(String[] args) {
        displayOrderIDs();
    }
}

