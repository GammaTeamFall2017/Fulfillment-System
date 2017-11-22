/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class UserLoginSceneController implements Initializable {

    @FXML private Button cancelButton;
    @FXML private Button loginButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label roleLabel;
    @FXML private Label authenticateLabel;
    @FXML private Label titleLabel;

    private SceneController newScene;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newScene = new SceneController();
    }    

    @FXML private void handleCancelButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button)event.getSource());
    }

    @FXML private void handleLoginButton(ActionEvent event) {
        DbUtilityCollection db = new DbUtilityCollection();
        
        if (db.authenticateEmployee(usernameField.getText(), passwordField.getText()))
        {
            System.out.println("user: " + db.getEmployeeByUsername(usernameField.getText()).getRole());
            CS4310FulfillmentProgram.setCurrentUserRole(db.getEmployeeByUsername(usernameField.getText()).getRole());// = db.getEmployeeByUsername(usernameField.getText()).getRole();
            System.out.println("user role: " + CS4310FulfillmentProgram.getCurrentUserRole());
            if (CS4310FulfillmentProgram.getCurrentUserRole().equals("admin"))
                newScene.setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", loginButton);
            else if(CS4310FulfillmentProgram.getCurrentUserRole().equals("waitstaff"))
                newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", loginButton);
            else if(CS4310FulfillmentProgram.getCurrentUserRole().equals("kitchen"))
                newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersScene.fxml", loginButton);
        }
        else
        {
            authenticateLabel.setVisible(true);
        }
    }
    
}
