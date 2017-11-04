/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Delegate;

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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Udoka
 */
public class EstimatedTimeOfArrival implements Initializable {
    
    @FXML Button requestWaitstaff;
    @FXML Label minutesLeft;
    String timeLeft;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //get time left for the order
    }
    
    public void handleRequestWaitstaffButton(ActionEvent event) throws IOException{
        //send info to the menuorderscene for the waitstaff
        
    }

}
