/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Item;
import cs4310.fulfillment.program.Model.ItemsOrdered;
import cs4310.fulfillment.program.Model.Orders;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    @FXML private Label tableLabel;
    @FXML private ChoiceBox<String> tableChoiceBox;
    
    private SceneController newScene = new SceneController();   
    private DbUtilityCollection DatabaseConnecter = new DbUtilityCollection();//this is the connecter for the db
    private List<Item> itemsArray = new ArrayList<Item>();
    private List<Button> itemDeleteButtons = new ArrayList<Button>();
    private List<Label> itemNameList = new ArrayList<Label>();
    private List<Label> priceList = new ArrayList<Label>();
    private List<Label> quantityList = new ArrayList<Label>();
    private List<String> usedTables = new ArrayList<String>(Collections.nCopies(8, "-1"));
    private List<ItemsOrdered> orderArray = new ArrayList<ItemsOrdered>();
    private int buttonsPerRow = 6;
    private int buttonHeight = 100;
    private int widthOfScrollPane = 720;
    private String tableNumber = "-1";
    private String orderNumber;
    private BigDecimal taxRate = new BigDecimal("0.10");
    private BigDecimal totalOrderPrice = new BigDecimal("0.00");
    private boolean receivedOrder = false;
    private Orders newOrder;
    List<Orders> allOrders;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        requestWaitstaff.setVisible(false);
        tableChoiceBox.setVisible(false);
        selectTable.setVisible(false);
        adminOptions.setVisible(false);
        tableLabel.setVisible(false);
        itemsArray = DatabaseConnecter.getAllItems();///this gets all of the food items in the database
        foodButtonPane.setStyle("-fx-box-border: transparent;");
        orderPane.setStyle("-fx-box-border: transparent; -fx-background-color: #e0e0e0");
        //itemsArray.get(0).setImgPath("/cs4310/fulfillment/program/View/Food/pizza.png");
        if(CS4310FulfillmentProgram.getCurrentUserRole().equals("Customer")){
            requestWaitstaff.setVisible(true);
            
            if(tableNumber == "-1"){
                tableNumber = getValidTableNumber();
                newOrder = DatabaseConnecter.createNewOrder();
            }
            else {
                allOrders = DatabaseConnecter.getAllOrders();
                for(Orders o : allOrders){
                    if(tableNumber == o.getTableNumber())//tableNumber needs to be set when returning from enjoy scene
                    {
                       orderArray.addAll(o.getItemsOrderedCollection());
                    }
                }
                for(ItemsOrdered itemsOrderedInDB : orderArray){
                    addItemToOrder(itemsOrderedInDB.getItemInOrder());
                }
            }
        }
        else if(CS4310FulfillmentProgram.getCurrentUserRole().equals("waitstaff")){
            tableChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7");
            tableNumber = getValidTableNumber();
            tableChoiceBox.getSelectionModel().selectFirst();
            tableChoiceBox.setVisible(true);
            selectTable.setVisible(true);
            tableLabel.setVisible(true);
            
            try{
                TimeUnit.SECONDS.sleep(1);
                checkRequestWaitstaff();
            }
            catch(Exception timer){
                System.out.println("Timer failed " + timer);
            }
                
            
        }
        else if(CS4310FulfillmentProgram.getCurrentUserRole().equals("admin")) {
            tableChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7");
            tableNumber = getValidTableNumber();
            tableChoiceBox.getSelectionModel().selectFirst();
            tableChoiceBox.setVisible(true);
            selectTable.setVisible(true);
            adminOptions.setVisible(true);
            tableLabel.setVisible(true);
            
            try{
                TimeUnit.SECONDS.sleep(1);
                checkRequestWaitstaff();
            }
            catch(Exception timer){
                System.out.println("Timer failed " + timer);
            }
        }
        
        //This is to temp fill a array of items
            BigDecimal bd = new BigDecimal("1.50");
            for(int i = 0; i < 9; i++){
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
                        ItemsOrdered itemsToOrder = new ItemsOrdered();
                        //add this scene in when it is created
                        //newScene.setScene("/cs4310/fulfillment/program/View/.fxml", (Button)e.getSource());
                        
                        newOrder.setTableNumber(tableNumber);
                        itemsToOrder.setItemInOrder(tempItem);
                        itemsToOrder.setOrderId(newOrder);
                        itemsToOrder.setItemQuantity(1);
                        
                        addItemToOrder(tempItem);
                        orderArray.add(itemsToOrder);
                    }
                });
                //sets the image of the button
                //BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource(tempItem.getImgPath()).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                //Background background = new Background(backgroundImage);
                newButton.setMinWidth(tempHBox.getPrefWidth());
                newButton.setMinHeight(tempHBox.getPrefHeight());
                //newButton.setBackground(background);
                newPane.getChildren().add(newButton);
                tempHBox.getChildren().add(newPane);
                tempHBox.setFillHeight(true);
            }
            VBoxButtons.getChildren().add(tempHBox);
        }
    }
    
    //Checks if the table is avalible or not.
    //It will return the first table avalible in the array.
    //If no tables are avalible it will return -1
    public String getValidTableNumber(){
        for(int i = 0; i < usedTables.size(); i++){
            if(usedTables.get(i) == "-1"){
                usedTables.set(i, Integer.toString(i));
                return usedTables.get(i);
            }
        }   
        return "-1";
    }
    
    //Request Waitstaff button
    @FXML public void handleRequestWaitstaffButton(ActionEvent event) throws IOException{
        newOrder.setRequestWaitstaff(true);
        try{
            DatabaseConnecter.requestWaitstaffUpdate(newOrder);
        }
        catch(Exception e){
            System.out.println("Unable to request waitstaff " + e);
        }
        requestWaitstaff.setStyle("-fx-background-color: blue;");
    }
    
    private void checkRequestWaitstaff(){
        
        try{
            TimeUnit.SECONDS.sleep(1);
            allOrders = DatabaseConnecter.getAllOrders();
            for(Orders o : allOrders){
                if(o.getRequestWaitstaff()){
                    Button newClearRequestWaitstaffButton = new Button();
                    newClearRequestWaitstaffButton.setLayoutX(15);
                    newClearRequestWaitstaffButton.setLayoutX(15);
                    newClearRequestWaitstaffButton.setText("Table" + o.getTableNumber());
                    newClearRequestWaitstaffButton.setOnAction(new EventHandler<ActionEvent>(){
                        @Override public void handle(ActionEvent e){
                            o.setRequestWaitstaff(false);
                            try{
                                DatabaseConnecter.finishRequestWaitstaffUpdate(o);
                            }
                            catch(Exception eventClearRequest){
                                System.out.println("Unable to clear request for waitstaff: " + eventClearRequest);
                            }
                        }
                    });
                }
            }
        }
        catch(Exception sleepTimer)
        {
            System.out.println("Sleep timer failes: " + sleepTimer);
        }
        
    }
    
    //select Table button
    @FXML public void handleSelectTableButton(ActionEvent event) throws IOException{
        tableNumber = tableChoiceBox.getValue().toString();
        //display everything from the order
        
        if(usedTables.get(Integer.valueOf(tableNumber)) == "-1"){
            tableNumber = getValidTableNumber();
            newOrder = DatabaseConnecter.createNewOrder();
        }
        else{
            allOrders = DatabaseConnecter.getAllOrders();
            for(Orders o : allOrders){
                if(tableNumber == o.getTableNumber())//tableNumber needs to be set when returning from enjoy scene
                {
                   orderArray.addAll(o.getItemsOrderedCollection());
                }
            }
            for(ItemsOrdered iO : orderArray){//needs better name than iO
                addItemToOrder(iO.getItemInOrder());
            }
        }
    }
    
    //back to admin options
    @FXML public void handleAdminOptions(ActionEvent event) throws IOException{
        newScene.setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", (Button)event.getSource());
    }
    
    //submit order button
    @FXML public void handleSubmitButton(ActionEvent event) throws IOException{
           
        if(CS4310FulfillmentProgram.getCurrentUserRole().equals("Customer")){
            newScene.setScene("/cs4310/fulfillment/program/View/EstimateTimeOfArrival1.fxml", submitOrder);
        }
        if(CS4310FulfillmentProgram.getCurrentUserRole().equals("waitstaff")){
            
        }
        submitOrder.setText("Received");
        receivedOrder = true;
        
        // Create line item entries in the database
        List<ItemsOrdered> tempOrderArray = new ArrayList<ItemsOrdered>(); // Store the ItemsOrdered object created in database to new list with their new id
        // If these are the items we want in the order, first add each line item as entries into the database to ItemsOrdered 
        for (Iterator<ItemsOrdered> i = orderArray.iterator(); i.hasNext();) {
            ItemsOrdered lineItem =  i.next();
            
            ItemsOrdered tempItemsOrdered = DatabaseConnecter.createNewItemsOrdered(lineItem);
            tempOrderArray.add(tempItemsOrdered);
        }
        
        // Attach the tempOrderArray to our order since it contains the ids needed to associated the new order with its line items in the database 
        newOrder.setItemsOrderedCollection(tempOrderArray);
        
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date(cal.getTimeInMillis());
        //DatabaseConnecter.updateNewOrder(newOrder, orderArray, newTable, totalCost.getText(), currentDate);
    }
    
    //cancel button
    @FXML private void handleCancelButton(ActionEvent e) throws IOException{
        
        try{
            DatabaseConnecter.removeOrder(newOrder);
        }
        catch(Exception execption){
            System.out.println("Unable to remove Order from database " + execption);
        }
        if(CS4310FulfillmentProgram.getCurrentUserRole().equals("Customer")){
            newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", submitOrder);
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
