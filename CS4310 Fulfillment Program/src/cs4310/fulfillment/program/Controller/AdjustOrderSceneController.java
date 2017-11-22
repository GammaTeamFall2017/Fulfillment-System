package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.ItemsOrdered;
import cs4310.fulfillment.program.Model.Orders;
import cs4310.fulfillment.program.Model.Subitem;
import cs4310.fulfillment.program.exceptions.IllegalOrphanException;
import cs4310.fulfillment.program.exceptions.NonexistentEntityException;
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
    @FXML private SceneController newScene;
    
    private Subitem item;
    private Orders order;
    DbUtilityCollection db = new DbUtilityCollection();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newScene = new SceneController();
        //disable change time button if no time is selected
        changeTimeButton.disableProperty().bind(selectTime.valueProperty().isNull());
        selectTime.getItems().addAll(5,10,15,20,25,30,35,40,45,50,55,60);
    }  
    @FXML
    public void handleOrderCompleteButton(ActionEvent e) {
        //return to list of orders, remove order from list of orders
        try{
            db.removeOrder(order);
        }
        catch(Exception exception){
            System.out.println("Unable to remove order " + exception);
        }
        newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersScene.fxml", (Button)e.getSource());
    }
    @FXML
    public void handleChangeTimeButton(ActionEvent e) {
        //return to list of orders, change remaining wait time
        int newTime = selectTime.getSelectionModel().getSelectedItem();
        item.setSubitemEta(newTime);
        newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersScene.fxml", (Button)e.getSource());
    }
    
    @FXML
    public void handleCancelButton(ActionEvent e) {
        newScene.setScene("/cs4310/fulfillment/program/View/ListOfOrdersScene.fxml", (Button)e.getSource());
    }
    
      
    
}
