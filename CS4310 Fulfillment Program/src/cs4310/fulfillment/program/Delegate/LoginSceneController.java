/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Delegate;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
/**
 * FXML Controller class
 *
 * @author John
 */
public class LoginSceneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private SceneController StartSceneController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        StartSceneController = new SceneController();
      
       // System.out.println("here");
    }    
    @FXML
    private void handleLoginButton(ActionEvent e){
        StartSceneController.setScene("/cs4310/fulfillment/program/View/ListOfOrdersScene.fxml", (Button)e.getSource());
    }
    @FXML
    private void handleCancelButton(ActionEvent e){
        StartSceneController.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button)e.getSource());
    }
}
