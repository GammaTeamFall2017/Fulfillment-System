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
        //
        int totalOrders = db.getKitchenOrders().size();

        int itemsPerOrder = 10;
        VBox vbox[] = new VBox[totalOrders];

        //loads information of order to each vbox
        for (int i = 0; i < totalOrders; i++) {

            Button bt = new Button("Table " + db.getKitchenOrders().get(i).getTableNumber());
            Button orderComplete = new Button("Order Complete");
            orderComplete.setId("" + db.getKitchenOrders().get(i).getOrderNumber());
            bt.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                //creates popup window, need to find way for adjustOrderScene to know which order called it
                public void handle(ActionEvent e) {
                    try {
                        newScene.setScene("/cs4310/fulfillment/program/View/AdjustOrderPopup.fxml", (Button) e.getSource());

                    } catch (Exception es) {
                        System.out.println("Unable to set the scene: " + es);

                    }
                }
            });
            orderComplete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                //creates popup window, need to find way for adjustOrderScene to know which order called it
                public void handle(ActionEvent e) {
                    int count = 0;
                    int id = Integer.parseInt(((Button) e.getSource()).getId());
                    System.out.println(db.getKitchenOrders().size());
                    while (db.getKitchenOrders().get(count).getOrderNumber() != id) {
                        count++;
                    }
                    db.getKitchenOrders().get(count).setKitchenComplete(true);

                    try {
                        db.updateKitchenOrder(db.getKitchenOrders().get(count));
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
            Label orderNumber = new Label("Order Number: " + db.getKitchenOrders().get(i).getOrderNumber() + "\nOrder Time : \n   " + db.getKitchenOrders().get(i).getDateCreated());
            orderNumber.setStyle("-fx-text-fill: white;");
            //Label kitchenComplete = new Label("Food is ready");
            Label label[] = new Label[itemsPerOrder];
            vbox[i] = new VBox();
            bt.setMinWidth(320);
            //adds style to vbox
            vbox[i].setStyle("-fx-border-style: solid;"
                    + "-fx-border-width: 1;"
                    + "-fx-min-height: 619px;"
                    + "-fx-max-width: 320px;"
                    + "-fx-border-color: black;"
                    + "-fx-background-color : transparent;");
            //adds button and order number label
            vbox[i].getChildren().add(bt);
            vbox[i].getChildren().add(orderComplete);
            vbox[i].getChildren().add(orderNumber);
            //adds order information adds each item as label with information below it
            for (int x = 0; x < db.getKitchenOrders().get(i).getItemsOrderedCollection().size(); x++) {
                //System.out.println("Items per order " + db.getKitchenOrders().get(i).getItemsOrderedCollection().size());
                List list = (List) db.getKitchenOrders().get(i).getItemsOrderedCollection();

                ItemsOrdered itemO = (ItemsOrdered) list.get(x);
                String itemInfo = "";

                itemInfo += "Item: " + itemO.getItemInOrder().getItemName();
                itemInfo += "\nItem Quantity : " + itemO.getItemQuantity();

                if (itemO.getSubitemOrdered() != null) {
                    itemInfo += "\n   Sub Item : " + itemO.getSubitemOrdered().getSubitemName();
                }
                itemInfo += "\n   Special Instructions : " + itemO.getSpecialInstructions();

                label[x] = new Label(itemInfo);
                label[x].setWrapText(true);
                label[x].setStyle("-fx-text-fill: white;");
                vbox[i].getChildren().add(label[x]);
            }

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
