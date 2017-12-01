/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program.Controller;

import cs4310.fulfillment.program.Model.DbUtilityCollection;
import cs4310.fulfillment.program.Model.Employee;
import cs4310.fulfillment.program.Model.EmployeeJpaController;
import cs4310.fulfillment.program.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class EditSelectUserSceneController implements Initializable {

    @FXML
    private VBox VBoxUser;
    @FXML
    private VBox VBoxRole;
    @FXML
    private VBox VBoxRemove;
    @FXML
    private VBox VBoxUpdate;
    @FXML
    private Button returnButton;

    private SceneController newScene;
    private Employee emp;
    private DbUtilityCollection db;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newScene = new SceneController();
        db = new DbUtilityCollection();
        displayEmployeeList();
    }    

    @FXML
    private void handleReturnButton(ActionEvent event) {
        newScene.setScene("/cs4310/fulfillment/program/View/AdminOptionScene.fxml", (Button)event.getSource());
    }
    
    private void displayEmployeeList()
    {
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
         EmployeeJpaController employeeInstance = new EmployeeJpaController(emf);
         List<Employee> employeeList = employeeInstance.findEmployeeEntities();
         for (Employee e :employeeList)
         {
             Label nameLabel = new Label(e.getFirstName() + " " + e.getLastName());
             Label roleLabel = new Label(e.getRole());
             Button updateButton = new Button("Update");
             updateButton.setOnAction((ActionEvent event) -> {
                 updateEmployee(e, updateButton);
             });
             Button removeButton = new Button("Remove");
             removeButton.setOnAction((ActionEvent event) -> {
                 removeEmployee(e,removeButton);
             });
             setHeight(50,nameLabel,roleLabel,updateButton,removeButton);
             VBoxUser.getChildren().add(nameLabel);
             VBoxRole.getChildren().add(roleLabel);
             VBoxUpdate.getChildren().add(updateButton);
             VBoxRemove.getChildren().add(removeButton);
         }
    }

    private void updateEmployee(Employee e, Button updateButton){
        try {
            Stage stage = (Stage) updateButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cs4310/fulfillment/program/View/EditUserScene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            EditUserSceneController cont = fxmlLoader.getController();
            cont.setFields(e.getUsername());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EditSelectUserSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void removeEmployee(Employee e, Button removeButton) {
        //check if e is only admin
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
        EmployeeJpaController employeeInstance = new EmployeeJpaController(emf);
        List<Employee> employeeList = employeeInstance.findEmployeeEntities();
        //confirm removal
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Remove Employee");
            alert.setHeaderText("Are you sure you want to remove this employee?");
            
            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeTwo = new ButtonType("No",ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){  //user chose Yes
                int index = VBoxRemove.getChildren().indexOf(removeButton);
                VBoxUser.getChildren().remove(index);
                VBoxRole.getChildren().remove(index);
                VBoxUpdate.getChildren().remove(index);
                VBoxRemove.getChildren().remove(index);
                try {
                    db.removeEmployee(e);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(EditSelectUserSceneController.class.getName()).log(Level.SEVERE, null, ex);
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
}
