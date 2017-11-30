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
import cs4310.fulfillment.program.exceptions.IllegalOrphanException;
import cs4310.fulfillment.program.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
public class UpdateItemSceneController implements Initializable {

    @FXML
    private TextField itemNameField;
    @FXML
    private TextField itemPriceField;
    @FXML
    private TextField itemETAField;
    @FXML
    private Button updateItemButton;
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
    private Item updateItem;
    private Subitem newSubitem;
    private Collection<Subitem> setOfSubitems;
    private List<Subitem> newSubitems;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new DbUtilityCollection();
        subitemTypeBox.getItems().addAll("add-on", "attribute");
        newScene = new SceneController();
        setOfSubitems = new HashSet<>();
        newSubitems = new ArrayList<>();
    }    

    @FXML
    private void handleUpdateItemButton(ActionEvent event) {
        if (!checkPriceFormat(itemPriceField))
             return;
        List<String> emptyFields = emptyItemFields();
        boolean valid = (emptyFields.isEmpty());
        if(valid)
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
            ItemJpaController itemInstance = new ItemJpaController(emf);
            BigDecimal price = new BigDecimal(itemPriceField.getText());
            String imgPath = "/cs4310/fulfillment/program/View/Food/" + updateItem.getItemName() + ".png";
            updateItem.setItemName(itemNameField.getText());
            updateItem.setItemPrice(price);
            updateItem.setItemEta(Integer.parseInt(itemETAField.getText()));
            updateItem.setImgPath(imgPath);
            updateItem.setSubitemCollection(setOfSubitems);
            try {
                itemInstance.edit(updateItem);
            }
            catch (Exception ex) {
                Logger.getLogger(UpdateItemSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println(setOfSubitems.toString());
            newScene.setScene("/cs4310/fulfillment/program/View/EditAddMenuSelection.fxml", (Button)event.getSource());
        }
        else
        {
             createPopup(emptyFields);
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
         newScene.setScene("/cs4310/fulfillment/program/View/EditAddMenuSelection.fxml", (Button)event.getSource());
    }

    @FXML
    //Unlike in Add Item Scene, adding or removing subitem has immediate effect on database
    private void handleAddSubitemButton(ActionEvent event){
        if (!checkPriceFormat(subitemPriceField))
             return;
        List<String> emptyFields = emptySubitemFields();
        boolean valid = (emptyFields.isEmpty());
        if(valid)
        {
            newSubitem = new Subitem();
            BigDecimal price = new BigDecimal(subitemPriceField.getText());
            newSubitem.setSubitemName(subitemNameField.getText());
            newSubitem.setSubitemPrice(price);
            newSubitem.setSubitemEta(Integer.parseInt(subitemETAField.getText()));
            newSubitem.setSubitemType(subitemTypeBox.getValue());
            newSubitem.setItemId(updateItem);
            newSubitem = db.createSubitem(newSubitem);
            if(setOfSubitems.add(newSubitem))
            {
                addSubitemtoList(newSubitem);
                // in case user adds subitem but then hits "cancel"
                updateItem.setSubitemCollection(setOfSubitems); 
                try {
                    db.updateItem(updateItem);
                } catch (Exception ex) {
                    Logger.getLogger(UpdateItemSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                stage.initOwner(updateItemButton.getScene().getWindow());
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
             setHeight(50,nameLabel,removeButton);
             VBoxSubitems.getChildren().add(nameLabel);
             VBoxRemove.getChildren().add(removeButton);
    }
    //Unlike in Add Item Scene, adding or removing subitem has immediate effect on database
    private void removeSubitem(Subitem s, Button removeButton) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Subitem");
            alert.setHeaderText("Are you sure you want to remove this subitem?");
            
            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeTwo = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                int index = VBoxRemove.getChildren().indexOf(removeButton);
                VBoxSubitems.getChildren().remove(index);
                VBoxRemove.getChildren().remove(index);
                setOfSubitems.remove(s);
        try {
            db.removeSubitem(s);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UpdateItemSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
            } else {
            // ... user chose No or closed the dialog
            }
        
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Price Format");
                alert.showAndWait();
                return false;
        }
    }
    
    public void setFields(Item i)
    {
        updateItem = i;
        itemNameField.setText(updateItem.getItemName());
        itemPriceField.setText(updateItem.getItemPrice().toString());
        itemETAField.setText(Integer.toString(updateItem.getItemEta()));
        setOfSubitems = updateItem.getSubitemCollection();
        for (Subitem s : setOfSubitems)
        {
            System.out.println(s.getSubitemId());
            Label nameLabel = new Label(s.getSubitemName() + "(" + s.getSubitemType() +  ")");
            nameLabel.setOnMouseClicked((MouseEvent event) -> {
                subitemNameField.setText(s.getSubitemName());
                subitemPriceField.setText(s.getSubitemPrice().toString());
                subitemETAField.setText(Integer.toString(s.getSubitemEta()));
                subitemTypeBox.setValue(s.getSubitemType());
            });
            Button removeButton = new Button("X");
             removeButton.setOnAction((ActionEvent event) -> {
                 removeSubitem(s,removeButton);
             });
             setHeight(50,nameLabel,removeButton);
             VBoxSubitems.getChildren().add(nameLabel);
             VBoxRemove.getChildren().add(removeButton);
        }
    }
}
