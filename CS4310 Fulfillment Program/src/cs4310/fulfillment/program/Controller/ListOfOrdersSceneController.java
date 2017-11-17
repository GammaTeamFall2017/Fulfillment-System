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

    /**
     * Initializes the controller class.
     */
    private SceneController newScene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newScene = new SceneController();
        DbUtilityCollection db = new DbUtilityCollection();
        //Just to test number of orders and items per order, will add database functionality later
        int totalOrders = 5;
        int itemsPerOrder = 10;
        //
        VBox vbox[] = new VBox[totalOrders];

        // db.displayKitchenOrders();
        //System.out.println(db.getTotalOrder());
        //db.displayKitchenOrders();
        //Testing add to Hbox
        /*
        orderAnchorPane.setStyle("-fx-box-border: transparent; -fx-background-color: #e0e0e0");
        Button button = new Button();
        Pane newPane = new Pane();
        button.setPrefWidth(2000);
        newPane.getChildren().add(button);
        HBox tempHBox = new HBox(50);
        
        tempHBox.getChildren().add(newPane);
        tempHBox.setFillHeight(true);
        
        
        VBox tempVBox = new VBox(50);
        Button button = new Button();
        Button button2 = new Button();
        tempVBox.getChildren().addAll(button, button2);
        VBox tempVBox2 = new VBox(50);
        tempVBox2.getChildren().addAll(button, button2);
        
        hbox.setAlignment(Pos.CENTER); // default TOP_LEFT
        VBox vbox1 = new VBox(50);
        VBox vbox2 = new VBox(50);
        VBox vbox3 = new VBox(50);
        Button button = new Button();
        Button button1 = new Button();
        Button button2 = new Button();
        Button button3 = new Button();
        vbox1.getChildren().add(button);
        vbox1.getChildren().add(button3);
        vbox2.getChildren().add(button1);
        vbox3.getChildren().add(button2);
        
        hbox.getChildren().add(vbox1);
        hbox.getChildren().add(vbox2);
        hbox.getChildren().add(vbox3);
         */
        //loads information of order to each vbox
        for (int i = 0; i < totalOrders; i++) {
            Button bt = new Button("Table " + (i + 1));
            Label label[] = new Label[itemsPerOrder];
            vbox[i] = new VBox();
            bt.setMinWidth(320);
            //adds style to vbox
            vbox[i].setStyle("-fx-border-style: solid;"
                    + "-fx-border-width: 1;"
                    + "-fx-min-height: 619px;"
                    + "-fx-max-width: 320px;"
                    + "-fx-border-color: black");
            //adds item labels to vbox
            vbox[i].getChildren().add(bt);
            for (int x = 0; x < itemsPerOrder; x++) {
                label[x] = new Label("Food");
                label[x].setWrapText(true);
                vbox[i].getChildren().add(label[x]);
            }

        }

        for (int i = 0; i < totalOrders; i++) {
            hbox.getChildren().add(vbox[i]);
        }

    }
    @FXML
    public void handleLogoutButton(ActionEvent e) throws IOException{
        newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button)e.getSource());
    }
    @FXML
    public void handleRefreshButton(ActionEvent e) throws IOException{
        newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersScene.fxml", (Button)e.getSource());
     }
}

        

