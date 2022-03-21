package controllers;

import domain.Artist;
import domain.Show;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import service.LogService;
import service.Service;

import java.awt.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SearchArtistsController {
    private Service service;
    private LogService logService;
    private Stage primaryStage;

    private Long employeeId;

    private final ObservableList<Show> model = FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;
        employeeId = logService.getEmployeeId();
    }

    public void setLogin(LogService logService) {
        this.logService = logService;
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    private TableColumn<Show, String> ArtistNameColumn;

    @FXML
    private TableColumn<Show, String> LocationColumn;

    @FXML
    private TableColumn<Show, String> StartHourColumn;

    @FXML
    private TableColumn<Show, Integer> SeatsAvailableColumn;

    @FXML
    private TableView<Show> artistsTable;

    @FXML
    private DatePicker date;

    @FXML
    private TextField buyerName;

    @FXML
    private Spinner<Integer> numberTickets;


    @FXML
    private void initialize() {
        ArtistNameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getArtist().getFirstName() + " " + param.getValue().getArtist().getLastName()));
        StartHourColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getDateTime().toString().substring(10, 19)));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<Show, String>("showLocation"));
        SeatsAvailableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getSeatsTotal()-param.getValue().getSeatsSold()));

        date.setValue(LocalDate.from(LocalDateTime.now()));
        artistsTable.setItems(model);
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
        model.clear();
        List<Show> shows = service.getArtistsByDate(Timestamp.valueOf(date.getValue().atStartOfDay()));

        List<Show> showsRez = shows.stream()
                .sorted(Comparator.comparing(x -> x.getArtist().getFirstName()))
                .sorted(Comparator.comparing(x -> x.getArtist().getLastName()))
                        .toList();

        model.setAll(showsRez);
        customRow(SeatsAvailableColumn);
    }

    @FXML
    private void handleSearch(){
        initModel();
    }

    private void clear(){
        buyerName.setText("");
        List<Integer> list = new ArrayList<>();
        list.add(0);
        numberTickets.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Integer>(FXCollections.observableArrayList(list)));
    }

    @FXML
    private void handleBuyTickets(){
        if (artistsTable.getSelectionModel().isEmpty()) {
            MessageAlert.showErrorMessage(null, "Please select a show first!");
        }
        else if(artistsTable.getSelectionModel().getSelectedItem().getDateTime().toLocalDateTime().isBefore(LocalDateTime.now())){
            MessageAlert.showErrorMessage(null, "You can't buy any tickets at this Show!");
        }
        else {
            Long showId = artistsTable.getSelectionModel().getSelectedItem().getId();
            Long officeEmployeeId = employeeId;

            String buyerN = buyerName.getText();
            Integer nrTickets = numberTickets.getValue();

            if(Objects.equals(buyerN, "")){
                MessageAlert.showErrorMessage(null, "Buyer name is empty");
                return;
            }

            Show newShow = service.findShowById(showId);
            if (newShow.getSeatsTotal() - newShow.getSeatsSold() != 0) {
                newShow.setSeatsSold(newShow.getSeatsSold() + nrTickets);
                service.updateShow(showId, newShow);

                for (int i = 0; i < nrTickets; i++)
                    service.buyTicket(showId, officeEmployeeId, buyerN);
                clear();
                initModel();
            } else
                MessageAlert.showErrorMessage(null, "There are no longer available tickets!");
        }
    }

    @FXML
    private void handleClick(){
        int seatsAvailable = artistsTable.getSelectionModel().getSelectedItem().getSeatsTotal()-artistsTable.getSelectionModel().getSelectedItem().getSeatsSold();
        List<Integer> listValues = new ArrayList<Integer>();
        for(int i=0; i<=seatsAvailable; i++)
            listValues.add(i);

        numberTickets.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Integer>(FXCollections.observableArrayList(listValues)));
    }

    @FXML
    private void handleClose() throws IOException {
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
