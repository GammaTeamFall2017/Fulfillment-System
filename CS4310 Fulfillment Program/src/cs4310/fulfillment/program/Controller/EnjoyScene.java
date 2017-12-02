/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Orders;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 *
 * @author Udoka
 */
public class EnjoyScene implements Initializable {
    
    @FXML Button done;
    @FXML Button requestWaitstaff;
    private Orders newOrder = new Orders();
    private DbUtilityCollection DatabaseConnecter = new DbUtilityCollection();

    @Override public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public Orders getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Orders newOrder) {
        this.newOrder = newOrder;
    }
    
    public void handleRequestWaitstaffButton(ActionEvent event) throws IOException{
        //send info to the menuorderscene for the waitstaff
        newOrder.setRequestWaitstaff(true);
        System.out.println("newOrder" + newOrder.getTotalPrice());
        try{
            DatabaseConnecter.requestWaitstaffUpdate(newOrder);
        }
        catch(Exception e){
            System.out.println("Unable to request waitstaff " + e);
        }  
    }
    
    public void handleDoneButton(ActionEvent event) throws IOException{

        new SceneController().setScene("/cs4310/fulfillment/program/View/EnjoyScene.fxml", (Button)event.getSource());
    }
}