/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class EditUserPopupSceneController implements Initializable {

    @FXML
    private Button okButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label bodyLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleOkButton(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
    public void setText(List<String> emptyFields)
    {
        String text = "";
        for (String s : emptyFields)
        {
            text += s + " is missing. Please add " + s + ".\n";
        }
        bodyLabel.setText(text);
    }
    
}
