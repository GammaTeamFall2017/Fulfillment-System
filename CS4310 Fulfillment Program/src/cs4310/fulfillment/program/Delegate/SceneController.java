/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Delegate;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SceneController{
   private Stage stage;
    public boolean setScene(String scene, Button button){
        boolean temp = false;
        try{
            this.setStage(button);
            Parent root = FXMLLoader.load(getClass().getResource(scene));;
            //get reference to scene1
            Scene myScene = new Scene(root);
            stage.setScene(myScene);
            stage.show();
            temp = true;
        }
        catch(Exception e){
            System.out.println("Unable to set the scene: " + e);
            temp = false;
        }
        return temp;
    }
    private void setStage(Button button){
        stage = (Stage) button.getScene().getWindow();
    }
}