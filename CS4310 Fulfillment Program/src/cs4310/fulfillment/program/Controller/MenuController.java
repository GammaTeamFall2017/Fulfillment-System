/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author John
 */
public class MenuController implements Initializable {
    private SceneController newScene = new SceneController();
    @FXML
    ImageView menuImage;
    private int imageCount = 1;
    private int maxImages = 5;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    public void handleBackButton(ActionEvent e) throws IOException {
        newScene.setScene("/cs4310/fulfillment/program/View/StartScene.fxml", (Button) e.getSource());
    }

    @FXML
    public void handleOrderButton(ActionEvent e) throws IOException {
        newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", (Button) e.getSource());
    }
    @FXML
    public void handleNextButton(ActionEvent e) throws IOException {
        imageCount++;
        if(imageCount>maxImages){
            imageCount = 1;
        }
        Image pic = new Image("/cs4310/fulfillment/program/View/Food/pic" +imageCount +".jpg");
        menuImage.setImage(pic);
    }
    @FXML
    public void handlePreviousButton(ActionEvent e) throws IOException {
        imageCount--;
        System.out.println(imageCount);
        if(imageCount<1){
            imageCount = maxImages;
        }
        Image pic = new Image("/cs4310/fulfillment/program/View/Food/pic" +imageCount +".jpg");
        menuImage.setImage(pic);
    }
}
