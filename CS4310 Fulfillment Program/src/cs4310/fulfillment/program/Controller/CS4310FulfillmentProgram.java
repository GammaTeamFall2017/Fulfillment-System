/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jacobh
 */
public class CS4310FulfillmentProgram extends Application {
    private static String currentUserRole = "";
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/cs4310/fulfillment/program/View/StartScene.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("EasyOrder Fulfillment System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    public static void setCurrentUserRole(String currentUserRole) {
        CS4310FulfillmentProgram.currentUserRole = currentUserRole;
    }
    
    
    
    
}