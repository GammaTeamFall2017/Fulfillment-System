/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Delegate;

import cs4310.fulfillment.program.Delegate.SceneController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author jacobh
 */
public class MenuOrderScene  implements Initializable {
    
    @FXML private Button submitOrder;
    @FXML private Label totalPrice;
    @FXML private VBox VBoxQuantity;
    @FXML private VBox VBoxFood;
    @FXML private VBox VBoxPrice;
    
    @FXML private AnchorPane foodButtonPane;
    @FXML SceneController newScene;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //can put top food items here
        newScene = new SceneController();
    }
    
    @FXML public void handleSubmitButton(ActionEvent e) throws IOException{
        newScene.setScene("/cs4310/fulfillment/program/View/EstimateTimeOfArrival1.fxml", (Button)e.getSource());
    }
    @FXML private void handleCancelButton(ActionEvent e){
        newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button)e.getSource());
    }
    @FXML public void addRowToButtons(){
        
    }
    
    @FXML public void addColToButton(){
        
    }
    
    
    
}
