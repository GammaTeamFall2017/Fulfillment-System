/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class AdminOptionSceneController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Button userRegistrationButton;
    @FXML
    private Button editUserButton;
    @FXML
    private Button editItemButton;
    @FXML
    private Button addItemButton;
    @FXML
    private Button viewMenuOrderButton;
    @FXML
    private Button viewOrderListButton;
    @FXML
    private Button viewStatisticsButton;
    @FXML
    private Button logoutButton;

    private SceneController newScene;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        newScene = new SceneController();
    }    

    @FXML
    private void handleUserRegistrationButton(ActionEvent event) {
               newScene.setScene("/cs4310/fulfillment/program/View/UserRegistrationScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleEditUserButton(ActionEvent event) {
               newScene.setScene("/cs4310/fulfillment/program/View/EditSelectUserScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleEditItemButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/EditAddMenuSelection.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleAddItemButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/AddFoodItem.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleViewMenuOrderButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleViewOrderListButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/OrderListScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleViewStatisticsButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/ViewStatisticsScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        CS4310FulfillmentProgram.currentUserRole = "";
        newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button)event.getSource());
    }
    
}
