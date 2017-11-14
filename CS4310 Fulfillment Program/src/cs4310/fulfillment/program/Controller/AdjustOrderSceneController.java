package cs4310.fulfillment.program.Controller;

import java.net.URL;
import javafx.fxml.FXML;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class AdjustOrderSceneController implements Initializable {

    @FXML private Button orderCompleteButton;
    @FXML private Button changeTimeButton;
    @FXML private Button cancelButton;
    @FXML private ChoiceBox selectTime;
    
    @FXML
    public void handleOrderCompleteButton(ActionEvent e) {
        
    }
    @FXML
    public void handleChangeTimeButton(ActionEvent e) {
        
    }
    
    @FXML
    public void handleCancelButton(ActionEvent e) {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectTime.getItems().addAll(0,5,10,15,20,25,30,35,40,45,50,55,60);
    }    
    
}
