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
import java.math.MathContext;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    @FXML private AnchorPane anchorPane;
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
    private List<Integer> etaOfItem = new ArrayList<Integer>();
    private int buttonsPerRow = 5;
    private int buttonHeight = 120;
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
                System.out.println("tableNumber = " + tableNumber + " Size of usedTables = " + usedTables.size());
                //usedTables.add(0,"1");//add in table here
                //usedTables.add(Integer.getInteger(tableNumber), "1");
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
        
        int row = (itemsArray.size() / buttonsPerRow) + 1;//gets the total rows
        int col = buttonsPerRow;
        int end = itemsArray.size() % buttonsPerRow;//gets the total for the last row
        
        int counter = 0;
        for(int i = 0; i < row; i++){
            int buttonsWidth = widthOfScrollPane / buttonsPerRow; //720 is the size of the VBox
            System.out.println("buttonwidth: " + buttonsWidth);
            HBox tempHBox = new HBox(buttonsWidth);
            tempHBox.setPrefWidth(buttonsWidth); 
            tempHBox.setPrefHeight(buttonHeight);
            if(i == (row - 1)){
               col = end;
            }            
            for(int j = 0; j < col; j++) {
                Item tempItem = itemsArray.get(counter++);
                Pane newPane = new Pane();
                Button newButton = new Button();
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
                if(tempItem.getImgPath() != null){
                    try{
                    //pic size = 168x147  change the size in the image
                    BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource(tempItem.getImgPath()).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                    Background background = new Background(backgroundImage);
                    newButton.setBackground(background);
                    }
                    catch (Exception e)
                    {
                        newButton.setText(tempItem.getItemName());
                        System.out.println(e.getMessage());
                    }
                }
                
                newButton.setMinWidth(tempHBox.getPrefWidth());
                newButton.setMinHeight(tempHBox.getPrefHeight());
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
                    System.out.println("waitstaff requested");
                    Pane newPane = new Pane();
                    Button newClearRequestWaitstaffButton = new Button();
                    newClearRequestWaitstaffButton.setLayoutX(15);
                    newClearRequestWaitstaffButton.setLayoutX(15);
                    newClearRequestWaitstaffButton.setText("Table " + o.getTableNumber());
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
                    newPane.getChildren().add(newClearRequestWaitstaffButton);
                    anchorPane.getChildren().add(newPane);
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
                //if(o.)//check if the order has been submited to the kitchen 
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
    @FXML public void handleMenuButton(ActionEvent event) throws IOException{
        newScene.setScene("/cs4310/fulfillment/program/View/MenuScene.fxml", (Button)event.getSource());
    }
    
    //submit order button
    @FXML public void handleSubmitButton(ActionEvent event) throws IOException{
        System.out.println("1order paided: " + newOrder.getOrderPaid());
        int totalETA = 0;
        for(int i = 0; i < etaOfItem.size(); i++){
            totalETA += etaOfItem.get(i);
        }
        
        if(CS4310FulfillmentProgram.getCurrentUserRole().equals("Customer")){
            Stage stage = (Stage) submitOrder.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cs4310/fulfillment/program/View/EstimateTimeOfArrival1.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            EstimatedTimeOfArrival cont = fxmlLoader.getController();
            cont.setTimeLeft(totalETA);
            stage.setScene(new Scene(root));
            stage.show();
        }
        else if(!CS4310FulfillmentProgram.getCurrentUserRole().equals("Customer")){
            System.out.println("not customer");
            if(newOrder.getOrderPaid() == true){
                submitOrder.setText("Submit Order");
                VBoxFood.getChildren().clear();
                VBoxPrice.getChildren().clear();
                VBoxQuantity.getChildren().clear();
                VBoxDelete.getChildren().clear();
                itemNameList.clear();
                priceList.clear();
                quantityList.clear();
                itemDeleteButtons.clear();
                etaOfItem.clear();
                orderArray.clear();
                newOrder = DatabaseConnecter.createNewOrder();
                totalOrderPrice = BigDecimal.ZERO;
                setPrice();
                System.out.println("received");
            }
            else if(newOrder.getOrderPaid() == false){
                submitOrder.setText("Received");
                newOrder.setOrderPaid(true);
                System.out.println("submit");
            }
        }
        
        System.out.println("2order paided: " + newOrder.getOrderPaid());
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
            if(newOrder != null){
                DatabaseConnecter.removeOrder(newOrder);
            }
        }
        catch(Exception execption){
            System.out.println("Unable to remove Order from database " + execption);
        }
        System.out.println("tableNumber" + tableNumber);
        //usedTables.add(Integer.getInteger(tableNumber), "-1");
        if(CS4310FulfillmentProgram.getCurrentUserRole().equals("Customer")){
            newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", submitOrder);
        }else{
            // Remove all line items and reset prices
            VBoxFood.getChildren().clear();
            VBoxPrice.getChildren().clear();
            VBoxQuantity.getChildren().clear();
            VBoxDelete.getChildren().clear();
            itemNameList.clear();
            priceList.clear();
            quantityList.clear();
            itemDeleteButtons.clear();
            etaOfItem.clear();
            orderArray.clear();
            
            totalOrderPrice = BigDecimal.ZERO;
            setPrice();
        }
    }
    
    @FXML private void addItemToOrder(Item itemToAdd){
        Label newItemNameField = new Label(itemToAdd.getItemName());
        Label newPriceField = new Label("$" + itemToAdd.getItemPrice().toString());
        Label newQuantityField = new Label("1");
        Button deleteItemButton = new Button();
        totalOrderPrice = totalOrderPrice.add(itemToAdd.getItemPrice());
        setPrice();
        etaOfItem.add(itemToAdd.getItemEta());
                
        
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
            //change the order when it is removed
            etaOfItem.remove(index);
            orderArray.remove(index);
            
            totalOrderPrice = totalOrderPrice.subtract(itemToAdd.getItemPrice());
            setPrice();
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
    
    public void setPrice(){
        String tempString = "";
        tempString = totalOrderPrice.toString();
        subPrice.setText("$" + tempString);  
        tempString = Double.toString(taxRate.multiply(totalOrderPrice).doubleValue());
        tempString = tempString.substring(0, Math.min(tempString.length(), 5));
        taxAmout.setText("$" + tempString);
        tempString = totalOrderPrice.add(taxRate.multiply(totalOrderPrice)).toString();
        tempString = tempString.substring(0, Math.min(tempString.length(), 5));
        totalCost.setText("$" + tempString);
    }
      
}
