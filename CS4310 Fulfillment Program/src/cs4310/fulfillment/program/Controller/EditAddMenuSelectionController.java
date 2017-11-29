/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Employee;
import cs4310.fulfillment.program.Model.Item;
import cs4310.fulfillment.program.exceptions.IllegalOrphanException;
import cs4310.fulfillment.program.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class EditAddMenuSelectionController implements Initializable {

    @FXML
    private Button addItemButton;
    @FXML
    private Button adminButton;
    @FXML
    private Button logoutButton;
    @FXML
    private VBox VBoxItem;
    @FXML
    private VBox VBoxPrice;
    @FXML
    private VBox VBoxETA;
    @FXML
    private VBox VBoxUpdate;
    @FXML
    private VBox VBoxRemove;

      private SceneController newScene;
    private Employee emp;
    private DbUtilityCollection db;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         newScene = new SceneController();
        db = new DbUtilityCollection();
        displayItemList();
    }    

    @FXML
    private void handleAddItemButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/AddFoodItem.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleAdminButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        CS4310FulfillmentProgram.setCurrentUserRole("");
        newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button)event.getSource());
    }

    private void displayItemList() {
           List<Item> itemList = db.getAllItems();
           for (Item i: itemList)
           {
               Label nameLabel = new Label(i.getItemName());
               Label priceLabel = new Label("$" + i.getItemPrice());
               Label etaLabel = new Label(Integer.toString(i.getItemEta()));
               Button updateButton = new Button("Update");
             updateButton.setOnAction((ActionEvent event) -> {
                 updateItem(i, updateButton);
             });
             Button removeButton = new Button("Remove");
             removeButton.setOnAction((ActionEvent event) -> {
                 removeItem(i,removeButton);
             });
               setHeight(50, nameLabel,priceLabel,etaLabel,updateButton,removeButton);
               VBoxItem.getChildren().add(nameLabel);
               VBoxPrice.getChildren().add(priceLabel);
               VBoxETA.getChildren().add(etaLabel);
               VBoxUpdate.getChildren().add(updateButton);
               VBoxRemove.getChildren().add(removeButton);
           }
    }

    private void updateItem(Item i, Button updateButton) {
        try {
            Stage stage = (Stage) updateButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cs4310/fulfillment/program/View/UpdateItemScene.fxml"));
             Parent root = (Parent) fxmlLoader.load();
            UpdateItemSceneController cont = fxmlLoader.getController();
            cont.setFields(i);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(EditSelectUserSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void removeItem(Item i, Button removeButton) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Item");
            alert.setHeaderText("Are you sure you want to remove this item?");
            
            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeTwo = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                int index = VBoxRemove.getChildren().indexOf(removeButton);
                VBoxItem.getChildren().remove(index);
                VBoxPrice.getChildren().remove(index);
                VBoxETA.getChildren().remove(index);
                VBoxUpdate.getChildren().remove(index);
                VBoxRemove.getChildren().remove(index);
                try {
                    db.removeItem(i);
                } catch (IllegalOrphanException ex) {
                    Logger.getLogger(EditAddMenuSelectionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(EditAddMenuSelectionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
            // ... user chose No or closed the dialog
            }
    }
    private void setHeight(double height, Control... nodes)
    {
        for (Control c : nodes)
        {
        c.setMinHeight(height);
        c.setMaxHeight(height);
        }
    }
}
