package client.gui;

import domain.OfficeEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.IFestivalService;
import services.LogException;


public class LoginController {

    private IFestivalService server;
    private EmployeeController employeeController;
    Parent mainEmployeeParent;

    @FXML
    private TextField inputUsername;

    @FXML
    private PasswordField inputPassword;


    public void setParent(Parent p) {
        mainEmployeeParent = p;
    }

    public void setServer(IFestivalService s) {
        this.server = s;
    }


    public void setEmployeeController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }


    @FXML
    public void employeeLogin(ActionEvent actionEvent) {
        tryLogin(actionEvent, inputUsername.getText(), inputPassword.getText());
    }


    private void tryLogin(ActionEvent actionEvent, String username, String password) {
        try {
            OfficeEmployee officeEmployee = server.findEmployeeByUsername(username);
            if (officeEmployee.getPassword().equals(password)) {
                server.logIn(officeEmployee, this.employeeController);

                Stage stage = new Stage();
                stage.setTitle("Chat Window for " + officeEmployee.getFirstName() + officeEmployee.getLastName());
                stage.setScene(new Scene(mainEmployeeParent));

                stage.show();
                employeeController.setEmployee(officeEmployee);
                employeeController.setShows();
                employeeController.setArtists();
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            } else
                throw new LogException("Login failed");
        } catch (LogException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

}
