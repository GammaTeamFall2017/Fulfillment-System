/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 *
 * @author Udoka
 */
public class EnjoyScene implements Initializable {
    
    @FXML Button done;
    @FXML Button requestWaitstaff;
    
   @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void handleRequestWaitstaffButton(ActionEvent event) throws IOException{
        //send info to the menuorderscene for the waitstaff
        
    }
    
    public void handleDoneButton(ActionEvent event) throws IOException{

        new SceneController().setScene("/cs4310/fulfillment/program/View/EnjoyScene.fxml", (Button)event.getSource());
    }
    
}
