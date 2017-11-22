/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Employee;
import cs4310.fulfillment.program.Model.EmployeeJpaController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class EditUserSceneController implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private ChoiceBox<String> roleSelectBox;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    
    private SceneController newScene;
    private Employee emp;
    private String originalUsername;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newScene = new SceneController();
        roleSelectBox.getItems().addAll("waitstaff","kitchen","admin");
        originalUsername = "uname";
        emp = new Employee();
    }    

    @FXML
    private void handleSaveButton(ActionEvent event) {
        List<String> emptyFields = validateFields();
        boolean valid = true;
        for (int i = 0; i < emptyFields.size(); i++)
        {
            //System.out.println(emptyFields.get(i));
            if (emptyFields.get(i) != null)
                valid = false;
        }
        //System.out.println(valid);
        if (valid)
        {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
            EmployeeJpaController employeeInstance = new EmployeeJpaController(emf);
            emp.setUsername(usernameField.getText());
            emp.setFirstName(firstNameField.getText());
            emp.setLastName(lastNameField.getText());
            emp.setPassword(passwordField.getText());
            emp.setRole(roleSelectBox.getValue());
            try {
                employeeInstance.edit(emp); 
                newScene.setScene("/cs4310/fulfillment/program/View/EditSelectUserScene.fxml", (Button)event.getSource());
            } catch (Exception ex) {
                Logger.getLogger(EditUserSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else         {
            createPopup(emptyFields);
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/EditSelectUserScene.fxml", (Button)event.getSource());
    }
    
    public List<String> validateFields()
    {
        List<String> emptyFields = new ArrayList<String>();

        if (firstNameField.getText() == null ||firstNameField.getText().isEmpty())
        {
            emptyFields.add("First name");
        }
        if (lastNameField.getText()== null ||lastNameField.getText().isEmpty())
        {
            emptyFields.add("Last name");
        }
        if (usernameField.getText()== null ||usernameField.getText().isEmpty())
        {
            emptyFields.add("Username");
        }
        if (passwordField.getText()== null ||passwordField.getText().isEmpty())
        {
            emptyFields.add("Password");
        }
        if (roleSelectBox.getValue()== null ||roleSelectBox.getValue().isEmpty())
        {
            emptyFields.add("Job title");
        }
        return emptyFields;
    }
    public void setFields(String username)
    {
        try
        {
        DbUtilityCollection db = new DbUtilityCollection();
        emp = db.getEmployeeByUsername(username);
        nameLabel.setText(emp.getFirstName() + " " + emp.getLastName());
        firstNameField.setText(emp.getFirstName());
        lastNameField.setText(emp.getLastName());
        usernameField.setText(emp.getUsername());
        passwordField.setText(emp.getPassword());
        roleSelectBox.setValue(emp.getRole());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public void createPopup(List<String> emptyFields)
    {
        try //create pop up window
            {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cs4310/fulfillment/program/View/EditUserPopupScene.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditUserPopupSceneController cont = fxmlLoader.getController();
                cont.setText(emptyFields);
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(saveButton.getScene().getWindow());
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(EditUserSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
