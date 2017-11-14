/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Controller.SceneController;
import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Item;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author jacobh
 */
public class MenuOrderScene  implements Initializable {
    
    @FXML private Button submitOrder;
    @FXML private Label totalPrice;
    @FXML private VBox VBoxQuantity;
    @FXML private VBox VBoxFood;
    @FXML private VBox VBoxPrice;
    @FXML private VBox VBoxButtons;
    @FXML private AnchorPane foodButtonPane;
    @FXML private AnchorPane orderPane;
    
    SceneController newScene = new SceneController();   
    DbUtilityCollection newDB = new DbUtilityCollection();
    List<Item> itemsArray = new ArrayList<Item>();
    List<Button> itemButtons = new ArrayList<Button>();
    int buttonsPerRow = 6;
    int buttonHeight = 100;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //itemsArray = newDB.getAllItems();
        foodButtonPane.setStyle("-fx-box-border: transparent;");
        orderPane.setStyle("-fx-box-border: transparent; -fx-background-color: #e0e0e0");
        
        BigDecimal bd = new BigDecimal("1.5");
        //This is to temp fill a array of items
        
        for(int i = 0; i < 4; i++){
            Item tempItems = new Item(i, "food" + i, i+1, bd );
            itemsArray.add(tempItems);
        }
        
        //end of temp area
        
        int row = (itemsArray.size() / buttonsPerRow) + 1;//gets the total rows if there are 6 buttons in a row
        int col = buttonsPerRow;
        int end = itemsArray.size() % buttonsPerRow;//gets the total for the last row
        
        int counter = 0;
        for(int i = 0; i < row; i++){
            int buttonsWidth = 720 / buttonsPerRow; //720 is the size of the VBox
            HBox tempHBox = new HBox(buttonsWidth);
            tempHBox.setPrefWidth(buttonsWidth); 
            tempHBox.setPrefHeight(buttonHeight);
            if(i == (row - 1)){
               col = end;
            }            
            for(int j = 0; j < col; j++) {
                Item tempItem = itemsArray.get(counter++);
                Pane newPane = new Pane();
                Button newButton = new Button(tempItem.getItemName());
                newButton.setOnAction(new EventHandler<ActionEvent>(){
                    @Override public void handle(ActionEvent e){
                    
                        //add this scene in when it is created
                        //newScene.setScene("/cs4310/fulfillment/program/View/.fxml", (Button)e.getSource());
                    
                    }
                });
                newButton.setMinWidth(tempHBox.getPrefWidth());
                newButton.setMinHeight(tempHBox.getPrefHeight());
                newPane.getChildren().add(newButton);
                tempHBox.getChildren().add(newPane);
                tempHBox.setFillHeight(true);
                
            }
            VBoxButtons.getChildren().add(tempHBox);
        }
    }
    
    @FXML public void handleSubmitButton(ActionEvent event) throws IOException{
        newScene.setScene("/cs4310/fulfillment/program/View/EstimateTimeOfArrival.fxml", submitOrder);
    }
    @FXML private void handleCancelButton(ActionEvent e) throws IOException{
        newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button)e.getSource());
    }
    
    @FXML private void handleItemButton(ActionEvent e) throws IOException{
        //This is for when a customize item scene is added
        //newScene.setScene("/cs4310/fulfillment/program/View/CustomizeItemScene.fxml", (Button)e.getSource());
        System.out.println("First item in array is " + itemsArray.get(1).getItemName());
        
    }
    
    @FXML public void addRowToButtons(){
        
    }
    
    @FXML public void addColToButton(){
        
    }
    
    
    
    
    
}
