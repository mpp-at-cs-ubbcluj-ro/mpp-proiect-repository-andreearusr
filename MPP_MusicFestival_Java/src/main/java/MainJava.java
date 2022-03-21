import controllers.EmployeeController;
import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.ArtistDBRepository;
import repository.OfficeEmployeeDBRepository;
import repository.ShowDBRepository;
import repository.TicketDBRepository;
import service.LogService;
import service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainJava extends Application {

    private LogService logService;
    private Service service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        ArtistDBRepository artistDBRepository = new ArtistDBRepository(props);
        OfficeEmployeeDBRepository officeEmployeeDBRepository = new OfficeEmployeeDBRepository(props);
        ShowDBRepository showDBRepository = new ShowDBRepository(props, artistDBRepository);
        TicketDBRepository ticketDBRepository = new TicketDBRepository(props, showDBRepository, officeEmployeeDBRepository);

        logService = new LogService(officeEmployeeDBRepository);
        service = new Service(artistDBRepository, officeEmployeeDBRepository, showDBRepository, ticketDBRepository);

        initView(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Music Festival");
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {

        //Parent root = FXMLLoader.load(getClass().getResource("employee.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //primaryStage.setTitle("Music Festival");
        primaryStage.setScene(scene);

        LoginController loginController = fxmlLoader.getController();
        loginController.setLogin(logService);
        loginController.setService(service);
        loginController.setStage(primaryStage);
    }
}
