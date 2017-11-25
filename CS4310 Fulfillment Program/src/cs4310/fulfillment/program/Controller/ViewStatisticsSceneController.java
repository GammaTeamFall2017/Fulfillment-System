/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class ViewStatisticsSceneController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Button adminOptionsButton;
    @FXML
    private Label totalRevenueLabel;
    @FXML
    private Label mostOrderedItemLabel;
    @FXML
    private Label mostRevenueItemLabel;
    @FXML
    private Label leastOrderedItemLabel;
    @FXML
    private Label leastRevenueItemLabel;

    private DbUtilityCollection db;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new DbUtilityCollection();
        setLabels();
    }    

    @FXML
    private void handleAdminOptionsButton(ActionEvent event) {
        new SceneController().setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", (Button)event.getSource());
    }

    private void setLabels() {
        //set titleLabel
        Date today = new Date();
        String dateString = DateFormat.getDateInstance().format(today);
        titleLabel.setText("Menu Statistics for " + dateString);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(titleLabel, 0.0);
        AnchorPane.setRightAnchor(titleLabel, 0.0);
        titleLabel.setAlignment(Pos.CENTER);
        //set rest of labels
    }
    
}
