/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Item;
import cs4310.fulfillment.program.Model.Orders;//remove when finished
import cs4310.fulfillment.program.Model.ItemsOrdered;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.io.IOException;
import java.util.List;
import cs4310.fulfillment.program.Model.Subitem;
import cs4310.fulfillment.program.exceptions.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Popup;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FXML Controller class
 *
 * @author John
 */
public class ListOfOrdersSceneController implements Initializable {

    @FXML
    private HBox hbox;
    @FXML
    private AnchorPane orderAnchorPane;
    DbUtilityCollection db;

    /**
     * Initializes the controller class.
     */
    private SceneController newScene = new SceneController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        db = new DbUtilityCollection();
        hbox.setStyle("fx-background-color: black;");
        //Remove order debug only remove later
        /*
        try{
             for(int i = 0 ; i<2;i++)
                db.removeOrder(db.getKitchenOrders().get(i+1));
            }catch(IllegalOrphanException e){
            }catch(NonexistentEntityException e){}
        //
         */
        // Load all kitchen orders
        List<Orders> kitchenOrders = db.getKitchenOrders();
        int totalOrders = kitchenOrders.size();

        int itemsPerOrder = 20;
        VBox vbox[] = new VBox[totalOrders];
    
        //loads information of order to each vbox
        int count = 0; // needed for vbox[]
        for (Orders currentOrder: kitchenOrders){
            Button bt = new Button("Table " + currentOrder.getTableNumber());
            Button orderComplete = new Button("Order Complete");
            orderComplete.setId("" + currentOrder.getOrderNumber());
            
            bt.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                //creates popup window, need to find way for adjustOrderScene to know which order called it
                public void handle(ActionEvent e) {
                    try {
                        newScene.setScene("/cs4310/fulfillment/program/View/AdjustOrderPopup.fxml", (Button) e.getSource());

                    } catch (Exception es) {
                        //System.out.println("Unable to set the scene: " + es);
                        es.printStackTrace();;

                    }
                }
            });
            
            orderComplete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                //creates popup window, need to find way for adjustOrderScene to know which order called it
                public void handle(ActionEvent e) {
                    int count = 0;
                    int id = Integer.parseInt(((Button) e.getSource()).getId());
                    System.out.println(kitchenOrders.size());
                    while (kitchenOrders.get(count).getOrderNumber() != id) {
                        count++;
                    }
                    kitchenOrders.get(count).setKitchenComplete(true);

                    try {
                        db.updateKitchenOrder(kitchenOrders.get(count));
                    } catch (Exception ex) {
                        System.out.println("Unable to request waitstaff " + e);
                    }
                    if(CS4310FulfillmentProgram.getCurrentUserRole().equals("admin")){
                        newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersSceneAdmin.fxml", (Button) e.getSource());
                    }else
                        newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersScene.fxml", (Button) e.getSource());
                    //System.out.println(db.getKitchenOrders().get(0).getKitchenComplete());

                }
            });
            
            Label orderNumber = new Label("Order Number: " + currentOrder.getOrderNumber() + "\nOrder Time : \n   " + currentOrder.getDateCreated());
            orderNumber.setStyle("-fx-text-fill: white;");
            //Label kitchenComplete = new Label("Food is ready");
            
            Label label = new Label();           
            if(count < itemsPerOrder){
                vbox[count] = new VBox();
          
                bt.setMinWidth(320);
                //adds style to vbox
                vbox[count].setStyle("-fx-border-style: solid;"
                        + "-fx-border-width: 1;"
                        + "-fx-min-height: 619px;"
                        + "-fx-max-width: 320px;"
                        + "-fx-border-color: black;"
                        + "-fx-background-color : transparent;");
                //adds button and order number label
                vbox[count].getChildren().add(bt);
                vbox[count].getChildren().add(orderComplete);
                vbox[count].getChildren().add(orderNumber);
                
                // Get all Items in the order at once             
                Collection<ItemsOrdered> currentOrdersItems = currentOrder.getItemsOrderedCollection();
                
                //adds order information adds each item as label with information below it
                db.displayKitchenOrder(currentOrdersItems, vbox[count], label);
          
            }
        
        count++; // counter for vbox
        }

        for (int i = 0; i < totalOrders; i++) {
            hbox.getChildren().add(vbox[i]);
        }

    }

    @FXML
    public void handleLogoutButton(ActionEvent e) throws IOException {
        newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button) e.getSource());
    }

    @FXML
    public void handleRefreshButton(ActionEvent e) throws IOException {
        newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersScene.fxml", (Button) e.getSource());
    }

    @FXML
    public void handleBackButton(ActionEvent e) throws IOException {
        newScene.setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", (Button) e.getSource());
    }

    @FXML
    public void handleRefreshButtonAdmin(ActionEvent e) throws IOException {
        newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersSceneAdmin.fxml", (Button) e.getSource());
    }
}
