/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Item;
import cs4310.fulfillment.program.Model.ItemJpaController;
import cs4310.fulfillment.program.Model.Subitem;
import cs4310.fulfillment.program.Model.SubitemJpaController;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class AddFoodItemController implements Initializable {

    @FXML
    private Button addItemButton;
    @FXML
    private TextField subitemNameField;
    @FXML
    private ChoiceBox<String> subitemTypeBox;
    @FXML
    private TextField subitemPriceField;
    @FXML
    private TextField subitemETAField;
    @FXML
    private Button cancelButton;
    @FXML
    private VBox VBoxSubitems;
    @FXML
    private Button addSubitemButton;
    @FXML
    private VBox VBoxRemove;

    private SceneController newScene;
    private DbUtilityCollection db;
    private Item newItem;
    private Subitem newSubitem;
    private Set<Subitem> setOfSubitems;
    @FXML
    private TextField itemNameField;
    @FXML
    private TextField itemPriceField;
    @FXML
    private TextField itemETAField;
    private int id =0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        subitemTypeBox.getItems().addAll("add-on", "attribute");
        newScene = new SceneController();
        setOfSubitems = new HashSet<Subitem>();
    }    

    @FXML
    private void handleAddItemButton(ActionEvent event){
        if (!checkPriceFormat(itemPriceField))
             return;
        List<String> emptyFields = emptyItemFields();
        boolean valid = (emptyFields.isEmpty());
        if(valid)
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
            ItemJpaController itemInstance = new ItemJpaController(emf);
            newItem = new Item();
            BigDecimal price = new BigDecimal(itemPriceField.getText());
            newItem.setItemName(itemNameField.getText());
            newItem.setItemPrice(price);
            newItem.setItemEta(Integer.parseInt(itemETAField.getText()));
            newItem.setSubitemCollection(setOfSubitems);
            //set id for item
            //disabled for now
            //itemInstance.create(newItem);
           
            newScene.setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", (Button)event.getSource());
        }
        else
        {
             createPopup(emptyFields);
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
         newScene.setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", (Button)event.getSource());
    }

    @FXML
    private void handleAddSubitemButton(ActionEvent event){
        if (!checkPriceFormat(subitemPriceField))
             return;
        List<String> emptyFields = emptySubitemFields();
        boolean valid = (emptyFields.isEmpty());
        if(valid)
        {
            newSubitem = new Subitem(id++);
            BigDecimal price = new BigDecimal(subitemPriceField.getText());
            newSubitem.setSubitemName(subitemNameField.getText());
            newSubitem.setSubitemPrice(price);
            newSubitem.setSubitemEta(Integer.parseInt(subitemETAField.getText()));
            newSubitem.setSubitemType(subitemTypeBox.getValue());
            if(setOfSubitems.add(newSubitem))
            {
                addSubitemtoList(newSubitem);
                
            }
        }
        else
        {
             createPopup(emptyFields);
        }
    }
    
    public List<String> emptyItemFields() 
    {
         List<String> emptyFields = new ArrayList<String>();
         if (itemNameField.getText() == null ||itemNameField.getText().isEmpty())
        {
            emptyFields.add("Item name");
        }
          if (itemPriceField.getText() == null ||itemPriceField.getText().isEmpty())
        {
            emptyFields.add("Item price");
        }
           if (itemETAField.getText() == null ||itemETAField.getText().isEmpty())
        {
            emptyFields.add("Item ETA");
        }
         return emptyFields;

    }
    
    public List<String> emptySubitemFields()
    {
        List<String> emptyFields = new ArrayList<String>();
         if (subitemNameField.getText() == null ||subitemNameField.getText().isEmpty())
        {
            emptyFields.add("Subitem name");
        }
          if (subitemPriceField.getText() == null ||subitemPriceField.getText().isEmpty())
        {
            emptyFields.add("Subitem price");
        }
           if (subitemETAField.getText() == null ||subitemETAField.getText().isEmpty())
        {
            emptyFields.add("Subitem ETA");
        }
           if (subitemTypeBox.getValue() == null ||subitemTypeBox.getValue().isEmpty())
        {
            emptyFields.add("Subitem Type");
        }
         return emptyFields;
    }

    private void createPopup(List<String> emptyFields) {
         try //create pop up window
            {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cs4310/fulfillment/program/View/EditUserPopupScene.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditUserPopupSceneController cont = fxmlLoader.getController();
                cont.setText(emptyFields);
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(addItemButton.getScene().getWindow());
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(EditUserSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private void addSubitemtoList(Subitem s) {
            Label nameLabel = new Label(s.getSubitemName() + "(" + s.getSubitemType() +  ")");
            Button removeButton = new Button("X");
             removeButton.setOnAction((ActionEvent event) -> {
                 removeSubitem(s,removeButton);
             });
             setHeight(30,nameLabel,removeButton);
             VBoxSubitems.getChildren().add(nameLabel);
             VBoxRemove.getChildren().add(removeButton);
    }

    private void removeSubitem(Subitem s, Button removeButton) {
        //doesn't actually remove subitem from database yet.
        int index = VBoxRemove.getChildren().indexOf(removeButton);
        VBoxSubitems.getChildren().remove(index);
        VBoxRemove.getChildren().remove(index);
        setOfSubitems.remove(s);
    }
    private void setHeight(double height, Control... nodes)
    {
        for (Control c : nodes)
        {
        c.setMinHeight(height);
        c.setMaxHeight(height);
        }
    }

    private boolean checkPriceFormat(TextField textfield) {
        try {
            DecimalFormat d = new DecimalFormat();
            d.setParseBigDecimal(true);
            d.parse(textfield.getText());
            return true;
        } catch (ParseException ex) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Price Format");
                alert.showAndWait();
                return false;
        }
    }
}
