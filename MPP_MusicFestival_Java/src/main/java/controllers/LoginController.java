package controllers;

import exceptions.LogException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.LogService;
import service.Service;

import java.io.IOException;

public class LoginController {

    private Service service;
    private LogService logService;
    private Stage primaryStage;

    @FXML
    private TextField inputUsername;

    @FXML
    private PasswordField inputPassword;

    @FXML
    public void employeeLogin() {
        tryLogin(inputUsername.getText(), inputPassword.getText());
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setLogin(LogService logService) {
        this.logService = logService;
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void tryLogin(String username, String password) {
        try {
            logService.logIn(username, password);
            changeScene();
        } catch (LogException | IOException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void changeScene() throws IOException {
        FXMLLoader fxmlLoader  = new FXMLLoader(getClass().getResource("/views/employee.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Music Festival");
        primaryStage.setScene(scene);

        EmployeeController employeeController = fxmlLoader.getController();
        employeeController.setLogin(logService);
        employeeController.setService(service);
        employeeController.setStage(primaryStage);
    }
}
