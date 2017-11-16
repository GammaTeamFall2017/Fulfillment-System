package cs4310.fulfillment.program.Controller;


import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author Jimmy
 */
public class DoneController implements Initializable {

    @FXML private Button yesButton;
    @FXML private Button noButton;
    @FXML Label doneLabel;
    private SceneController newScene;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newScene = new SceneController();
    }    
    
    @FXML
    public void handleYesButton(ActionEvent e) {
        newScene.setScene("/cs4310/fulfillment/program/View/MenuOrderScene.fxml", (Button)e.getSource());
    }
    
    @FXML
    public void handleNoButton(ActionEvent e) {
        newScene.setScene("/cs4310/fulfillment/program/View/EnjoyScene.fxml", (Button)e.getSource());
    }
}
