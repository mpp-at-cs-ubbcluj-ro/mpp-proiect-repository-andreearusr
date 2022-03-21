package controllers;

import domain.Show;
import exceptions.LogException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.LogService;
import service.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EmployeeController {

    private Service service;
    private LogService logService;
    private Stage primaryStage;

    //private Long employeeId;

    private final ObservableList<Show> model = FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;
        //employeeId = logService.getEmployeeId();
        initModel();
    }

    public void setLogin(LogService logService) {
        this.logService = logService;
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    private TableColumn<Show, String> ShowNameColumn;

    @FXML
    private TableColumn<Show, String> ShowArtistColumn;

    @FXML
    private TableColumn<Show, String> ShowDateColumn;

    @FXML
    private TableColumn<Show, String> ShowLocationColumn;

    @FXML
    private TableColumn<Show, Integer> ShowAvailableSeatsColumn;

    @FXML
    private TableColumn<Show, Integer> ShowSoldSeatsColumn;

    @FXML
    private TableView<Show> showsTable;

    @FXML
    private void initialize() {
        ShowNameColumn.setCellValueFactory(new PropertyValueFactory<Show, String>("showName"));
        ShowArtistColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getArtist().getFirstName() + " " + param.getValue().getArtist().getLastName()));
        ShowDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getDateTime().toString().substring(0, 10)));
        ShowLocationColumn.setCellValueFactory(new PropertyValueFactory<Show, String>("showLocation"));
        ShowAvailableSeatsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getSeatsTotal()-param.getValue().getSeatsSold()));
        ShowSoldSeatsColumn.setCellValueFactory(new PropertyValueFactory<Show, Integer>("seatsSold"));
        showsTable.setItems(model);
    }

    private void customRow(TableColumn<Show, Integer> col) {
        col.setCellFactory(column -> {
            return new TableCell<Show, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<Show> currentRow = getTableRow();

                    if (!isEmpty()) {
                        if(Objects.equals(item, 0))
                            currentRow.setStyle("-fx-background-color:red");
                    }
                }
            };
        });
    }

    private void initModel() {
        List<Show> shows = service.getShows();
        model.setAll(shows);
        customRow(ShowAvailableSeatsColumn);
    }

    @FXML
    private void handleSearchArtists() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/searchArtists.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Search Artists");
        primaryStage.setScene(scene);

        /*Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Search Artists");
        stage.show();
        stage.setResizable(false);*/

        SearchArtistsController searchArtistsController = fxmlLoader.getController();
        searchArtistsController.setLogin(logService);
        searchArtistsController.setService(service);
        searchArtistsController.setStage(primaryStage);
    }

    @FXML
    private void handleLogout() throws IOException, LogException {
        logService.logOut();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Music Festival");
        primaryStage.setScene(scene);

        LoginController loginController = fxmlLoader.getController();
        loginController.setLogin(logService);
        loginController.setService(service);
        loginController.setStage(primaryStage);
    }



}
