/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Controller.SceneController;
import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Subitem;
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
    
    DbUtilityCollection db = new DbUtilityCollection();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        newScene = new SceneController();
        sItem = new Subitem();
        String sInstruction = specialInstructionsField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        
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
        boolean isSelected = cb.isSelected();
    }    

    @FXML
    private void handleCancelButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleSubmitButton(ActionEvent event) {
        
        newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleUpdateButton(ActionEvent event) {
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
    }
    
}
