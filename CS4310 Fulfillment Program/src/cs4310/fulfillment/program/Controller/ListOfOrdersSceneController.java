/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import cs4310.fulfillment.program.Model.DbUtilityCollection;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
/**
 * FXML Controller class
 *
 * @author John
 */

public class ListOfOrdersSceneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private SceneController newScene;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newScene = new SceneController();
        DbUtilityCollection db = new DbUtilityCollection();
       // db.displayKitchenOrders();
        System.out.println(db.getTotalOrder());
        //db.displayKitchenOrders();
        // TODO
    }    
    
}
