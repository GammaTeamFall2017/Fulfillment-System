/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.Employee;
import cs4310.fulfillment.program.Model.EmployeeJpaController;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static java.lang.Integer.parseInt;
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
        setFields(originalUsername);
    }    

    @FXML
    private void handleSaveButton(ActionEvent event) {
        String [] arr = validateFields();
        boolean valid = true;
        for (int i = 0; i < arr.length; i++)
        {
            System.out.println(arr[i]);
            if (arr[i] != null)
                valid = false;
        }
        System.out.println(valid);
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
                //disabled for now.
                //employeeInstance.edit(emp); 
            } catch (Exception ex) {
                Logger.getLogger(EditUserSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else         {
            createPopup(arr);
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/EditSelectUserScene.fxml", (Button)event.getSource());
    }
    
    public String[] validateFields()
    {
        String [] arr;
        arr = new String[5];
        if (firstNameField.getText() == null ||firstNameField.getText().isEmpty())
        {
            arr[0] = "First name";
        }
        if (lastNameField.getText()== null ||lastNameField.getText().isEmpty())
        {
            arr[1] = "Last name";
        }
        if (usernameField.getText()== null ||usernameField.getText().isEmpty())
        {
            arr[2] = "Username";
        }
        if (passwordField.getText()== null ||passwordField.getText().isEmpty())
        {
            arr[3] = "Password";
        }
        if (roleSelectBox.getValue()== null ||roleSelectBox.getValue().isEmpty())
        {
            arr[4] = "Job title";
        }
        return arr;
    }
    public void setFields(String username)
    {
        try
        {
        //DbUtilityCollection db = new DbUtilityCollection();
        //emp = db.getEmployeeByUsername(username);
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
    public void createPopup(String [] arr)
    {
        try //create pop up window
            {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cs4310/fulfillment/program/View/EditUserPopupScene.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditUserPopupSceneController cont = fxmlLoader.getController();
                cont.setText(arr);
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(saveButton.getScene().getWindow());
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(EditUserSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
