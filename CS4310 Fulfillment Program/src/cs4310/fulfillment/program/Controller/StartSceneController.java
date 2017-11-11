/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Delegate.SceneController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
/**
 * FXML Controller class
 *
 * @author John
 */
public class StartSceneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private SceneController newScene;
    @FXML
    private Button loginButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private ChoiceBox<?> roleSelectBox;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label roleLabel;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        newScene = new SceneController();
    } 
    @FXML
    private void handleLoginButton(ActionEvent e){
        newScene.setScene("/cs4310/fulfillment/program/View/UserLoginScene.fxml", (Button)e.getSource());
    }
    @FXML
    private void handleCustomerButton(ActionEvent e){
        newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", (Button)e.getSource());
    }
}
