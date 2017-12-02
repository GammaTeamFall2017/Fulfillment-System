/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Orders;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Udoka
 */
public class EstimatedTimeOfArrival implements Initializable {
    
    @FXML Button requestWaitstaff;
    @FXML Label minutesLeft;
    private Timeline timeline;
    private Integer timeLeft;
    private Orders newOrder = new Orders();
    private DbUtilityCollection DatabaseConnecter = new DbUtilityCollection();
    private EnjoyScene enjoy = new EnjoyScene();
    
    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
        minutesLeft.setText(timeLeft.toString());
    }
    
    public Orders getOrder() {
        return newOrder;
    }

    public void setOrder(Orders myOrder) {
        this.newOrder = myOrder;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.minutes(1),
                  new EventHandler() {
                    // KeyFrame event handler
                    @Override
                    public void handle(Event event) {
                       timeLeft--;
                       // update minutesLeft
                       minutesLeft.setText(timeLeft.toString());
                       if (timeLeft <= 0) {
                            timeline.stop();
                            enjoy.setNewOrder(newOrder);
                            new SceneController().setScene("/cs4310/fulfillment/program/View/EnjoyScene.fxml",requestWaitstaff);
                       }
                    }
                }));
        timeline.playFromStart();
    }

    
    public void handleRequestWaitstaffButton(ActionEvent event) throws IOException{
        newOrder.setRequestWaitstaff(true);
        System.out.println("newOrder" + newOrder.getTotalPrice());
        try{
            DatabaseConnecter.requestWaitstaffUpdate(newOrder);
        }
        catch(Exception e){
            System.out.println("Unable to request waitstaff " + e);
        }        
    }

}
