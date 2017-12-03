/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Controller.SceneController;
import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Subitem;
import cs4310.fulfillment.program.Model.Item;
import java.math.BigDecimal;
import cs4310.fulfillment.program.Model.Orders;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.CheckBox;
import javax.persistence.criteria.Order;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class CustomizeItemSceneController implements Initializable {

    @FXML
    private VBox VBoxAddons;
    @FXML
    private TextField specialInstructionsField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private TextField quantityField;
    @FXML
    private Label subTotalLabel;
    @FXML
    private Button updateButton;
    @FXML
    private Label subTotalAmountLabel;
    @FXML
    private Button addButton;
    
    private Subitem sItem;
    private SceneController newScene;
    private BigDecimal subtotal;
    private BigDecimal itemPrice;
    private Orders order;
    private Item item;
    DbUtilityCollection db = new DbUtilityCollection();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        newScene = new SceneController();
        sItem = new Subitem();
        
        //disable submitButton if quantity field is empty; avoid empty orders
        submitButton.disableProperty().bind(quantityField.textProperty().isEmpty());
        
        CheckBox cb = new CheckBox();
        int totalSubitems = 5;
        VBox vb[] = new VBox[totalSubitems];
        for (int j = 0; j < totalSubitems; j++) {
            cb = new CheckBox("Item: " + db.getSubitemByID(j)); 
            vb[j] = new VBox();
            vb[j].setStyle("-fx-border-style: solid;"
                    + "-fx-border-width: 1;"
                    + "-fx-min-height: 500px;"
                    + "-fx-max-width: 200px;"
                    + "-fx-border-color: black");
            vb[j].getChildren().add(cb);
        }
        CheckBox cb1;
        CheckBox cb2;
        CheckBox cb3;
        CheckBox cb4;
        CheckBox cb5;
        
        //testing subitem checkboxes generated for item calling it
        //checkboxes for subitems
        cb1 = new CheckBox("Item: " + db.getSubitemByID(1)); 
        cb2 = new CheckBox("Item: " + db.getSubitemByID(2)); 
        cb3 = new CheckBox("Item: " + db.getSubitemByID(3)); 
        cb4 = new CheckBox("Item: " + db.getSubitemByID(4)); 
        cb5 = new CheckBox("Item: " + db.getSubitemByID(5)); 
        //check for selected subitems
        boolean isSelected = cb.isSelected();
        
        addButton.setOnAction(e -> 
{
  
        if (cb1.isSelected()) {
            //Subitem.getSubitemPrice();
            subtotal = subtotal.add(itemPrice);
        }
   
        if (cb2.isSelected())  {
           subtotal = subtotal.add(itemPrice);
        }
    
        if (cb3.isSelected()) {
            subtotal = subtotal.add(itemPrice);
        }
     
        if (cb4.isSelected()) {
            subtotal = subtotal.add(itemPrice);
        }    
     }
);
    }    

    @FXML
    private void handleCancelButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleSubmitButton(ActionEvent event) {
        String sInstruction = specialInstructionsField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        db.addItemsToOrder(order, item, sItem, quantity, sInstruction);
        newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleUpdateButton(ActionEvent event) {
         newScene.setScene("/cs4310/fulfillment/program/View/CustomizeItemScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        subTotalAmountLabel.setText(subtotal.toString());
    }
    
}
