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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author jacobh
 */
public class MenuOrderScene  implements Initializable {
    
    @FXML private Button submitOrder;
    @FXML private Button requestWaitstaff;
    @FXML private Button adminOptions;
    @FXML private Button selectTable;
    @FXML private VBox VBoxQuantity;
    @FXML private VBox VBoxFood;
    @FXML private VBox VBoxPrice;
    @FXML private VBox VBoxDelete;
    @FXML private VBox VBoxButtons;
    @FXML private AnchorPane foodButtonPane;
    @FXML private AnchorPane orderPane;
    @FXML private Label totalCost;
    @FXML private Label subPrice;
    @FXML private Label taxAmout;
    @FXML private Label tableRequest;
    @FXML private ChoiceBox<String> tableChoiceBox;
    
    private SceneController newScene = new SceneController();   
    private DbUtilityCollection DatabaseConnecter = new DbUtilityCollection();
    private List<Item> itemsArray = new ArrayList<Item>();
    private List<Button> itemDeleteButtons = new ArrayList<Button>();
    private List<Label> itemNameList = new ArrayList<Label>();
    private List<Label> priceList = new ArrayList<Label>();
    private List<Label> quantityList = new ArrayList<Label>();
    private List<Item> orderArray = new ArrayList<Item>();
    private int buttonsPerRow = 6;
    private int buttonHeight = 100;
    private int widthOfScrollPane = 720;
    private BigDecimal taxRate = new BigDecimal("0.10");
    private BigDecimal totalOrderPrice = new BigDecimal("0.00");
    private boolean newOrder = false;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        requestWaitstaff.setVisible(false);
        tableRequest.setVisible(false);
        tableChoiceBox.setVisible(false);
        selectTable.setVisible(false);
        adminOptions.setVisible(false);
        itemsArray = DatabaseConnecter.getAllItems();///this is the connecter for the db
        foodButtonPane.setStyle("-fx-box-border: transparent;");
        orderPane.setStyle("-fx-box-border: transparent; -fx-background-color: #e0e0e0");
        
        
        //This is to temp fill a array of items
            BigDecimal bd = new BigDecimal("1.50");
            for(int i = 0; i < 38; i++){
                Item tempItems = new Item(i, "food" + i, i+1, bd );
                itemsArray.add(tempItems);
            }
        
        //end of temp area
        
        int row = (itemsArray.size() / buttonsPerRow) + 1;//gets the total rows
        int col = buttonsPerRow;
        int end = itemsArray.size() % buttonsPerRow;//gets the total for the last row
        
        int counter = 0;
        for(int i = 0; i < row; i++){
            int buttonsWidth = widthOfScrollPane / buttonsPerRow; //720 is the size of the VBox
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
                        addItemToOrder(tempItem);
                        orderArray.add(tempItem);
                        
                         
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
        if(CS4310FulfillmentProgram.currentUserRole == "Customer"){
            requestWaitstaff.setVisible(true);
            
        }
        else if(CS4310FulfillmentProgram.currentUserRole == "waitstaff"){
            tableChoiceBox.getItems().addAll("Table 1", "Table 2", "Table 3", "Table 4", "Table 5", "Table 6", "Table 7");
            tableRequest.setVisible(true);
            tableChoiceBox.setVisible(true);
            selectTable.setVisible(true);
            
        }
        else if(CS4310FulfillmentProgram.currentUserRole == "admin"){
            tableChoiceBox.getItems().addAll("Table 1", "Table 2", "Table 3", "Table 4", "Table 5", "Table 6", "Table 7");
            tableRequest.setVisible(true);
            tableChoiceBox.setVisible(true);
            selectTable.setVisible(true);
            adminOptions.setVisible(true);
        }
            
    }
    
    //checks the table number for initializing the scene. 
    //If it is a new order it will set a flag, and assign a new order number for the table. 
    //If not it will access the correct information for that order.
    private void checkTableNumber(){
        //DatabaseConnecter.
    }
    
    @FXML public void handleRequestWaitstaffButton(ActionEvent event) throws IOException{
        //adding DB call for waitstaff
        
        requestWaitstaff.setStyle("-fx-background-color: blue;");
    }
    
    @FXML public void handleSelectTableButton(ActionEvent event) throws IOException{
        tableChoiceBox.getValue().toString();
        //get order from the table. 
        //display everything from the order
    }
    
    @FXML public void handleAdminOptions(ActionEvent event) throws IOException{
        newScene.setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", (Button)event.getSource());
    }
    
    @FXML public void handleSubmitButton(ActionEvent event) throws IOException{
        for(int i = 0; i < orderArray.size(); i++){
           //DatabaseConnecter. = orderArray; //sets the items in the order to the DB
        
        }    
        if(CS4310FulfillmentProgram.currentUserRole == ""){
            newScene.setScene("/cs4310/fulfillment/program/View/EstimateTimeOfArrival1.fxml", submitOrder);
        }
        if(CS4310FulfillmentProgram.currentUserRole == "waitstaff"){
            
        }
        submitOrder.setText("Pay");
        newOrder = true;
        
    }
    
    @FXML private void handleCancelButton(ActionEvent e) throws IOException{
        newOrder = false;
        if(CS4310FulfillmentProgram.currentUserRole == "Customer" ){
            newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button)e.getSource());
        }
        else {
            //clear the order
        }
            
    }
    
    @FXML private void addItemToOrder(Item itemToAdd){
        Label newItemNameField = new Label(itemToAdd.getItemName());
        Label newPriceField = new Label("$" + itemToAdd.getItemPrice().toString());
        Label newQuantityField = new Label("1");
        Button deleteItemButton = new Button();
        totalOrderPrice = totalOrderPrice.add(itemToAdd.getItemPrice());
        String tempString = "";
        tempString = totalOrderPrice.toString();
        subPrice.setText("$" + tempString);  
        tempString = Double.toString(taxRate.multiply(totalOrderPrice).doubleValue());
        tempString = tempString.substring(0, Math.min(tempString.length(), 5));
        taxAmout.setText("$" + tempString);
        tempString = totalOrderPrice.add(taxRate.multiply(totalOrderPrice)).toString();
        tempString = tempString.substring(0, Math.min(tempString.length(), 5));
        totalCost.setText("$" + tempString);
                
        deleteItemButton.setText("X");
        deleteItemButton.setTextFill(Color.RED);
        deleteItemButton.setOnAction((ActionEvent event) -> {
            int index = itemDeleteButtons.indexOf(deleteItemButton);
            VBoxFood.getChildren().remove(index);
            VBoxPrice.getChildren().remove(index);
            VBoxQuantity.getChildren().remove(index);
            VBoxDelete.getChildren().remove(index);
            itemNameList.remove(index);
            priceList.remove(index);
            quantityList.remove(index);
            itemDeleteButtons.remove(index);
        });
        double height = 20;
        deleteItemButton.setMinHeight(height);
        deleteItemButton.setMaxHeight(height);
        newItemNameField.setMinHeight(height);
        newItemNameField.setMaxHeight(height);
        newPriceField.setMinHeight(height);
        newPriceField.setMaxHeight(height);
        newQuantityField.setMinHeight(height);
        newQuantityField.setMaxHeight(height);
        itemDeleteButtons.add(deleteItemButton);
        itemNameList.add(newItemNameField);
        priceList.add(newPriceField);
        quantityList.add(newQuantityField);
        
        
        VBoxFood.getChildren().add(newItemNameField);
        VBoxPrice.getChildren().add(newPriceField);
        VBoxQuantity.getChildren().add(newQuantityField);
        VBoxDelete.getChildren().add(deleteItemButton);
    }
    
      
}
