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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleCancelButton(ActionEvent event) {
    }

    @FXML
    private void handleSubmitButton(ActionEvent event) {
    }

    @FXML
    private void handleUpdateButton(ActionEvent event) {
    }
    
}
