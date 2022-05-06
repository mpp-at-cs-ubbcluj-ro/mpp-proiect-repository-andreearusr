package festival.client;

import festival.client.gui.EmployeeController;
import festival.client.gui.LoginController;
import festival.network.protobuffprotocol.ProtoFestivalProxy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import festival.services.IFestivalService;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffClient extends Application {

    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/festivalclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find festivalclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("festival.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("festival.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IFestivalService server = new ProtoFestivalProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("Login.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.<LoginController>getController();
        loginController.setServer(server);


        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("Employee.fxml"));
        Parent croot = cloader.load();


        EmployeeController employeeController = cloader.<EmployeeController>getController();
        employeeController.setServer(server);

        loginController.setEmployeeController(employeeController);
        loginController.setParent(croot);

        primaryStage.setTitle("Music Festival");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
