package cs4310.fulfillment.program.Controller;

import java.net.URL;
import javafx.fxml.FXML;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 *
 * @author Jimmy
 */

public class AdjustOrderSceneController implements Initializable {

    @FXML private Button orderCompleteButton;
    @FXML private Button changeTimeButton;
    @FXML private Button cancelButton;
    @FXML private ChoiceBox<Integer> selectTime;
    
    @FXML
    public void handleOrderCompleteButton(ActionEvent e) {
        //return to list of orders, remove order from list of orders
        
        newScene.setScene("/cs4310/fulfillment/program/View/ListofOrdersScene.fxml", (Button)e.getSource());
    }
    @FXML
    public void handleChangeTimeButton(ActionEvent e) {
        //return to list of orders, change remaining wait time
        
        newScene.setScene("/cs4310/fulfillment/program/View/ListofOrdersScene.fxml", (Button)e.getSource());
    }
    
    @FXML
    public void handleCancelButton(ActionEvent e) {
        newScene.setScene("/cs4310/fulfillment/program/View/ListofOrdersScene.fxml", (Button)e.getSource());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newScene = new SceneController();
        selectTime.getItems().addAll(0,5,10,15,20,25,30,35,40,45,50,55,60);
    }    
    
}
