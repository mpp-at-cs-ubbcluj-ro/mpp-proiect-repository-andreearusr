package client.gui;

import domain.OfficeEmployee;
import domain.Show;
import domain.Ticket;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.IFestivalObserver;
import services.IFestivalService;
import services.LogException;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EmployeeController implements Initializable, IFestivalObserver {

    private IFestivalService server;
    private OfficeEmployee officeEmployee;

    private final ObservableList<Show> model1 = FXCollections.observableArrayList();
    private final ObservableList<Show> model2 = FXCollections.observableArrayList();

    public void setServer(IFestivalService s) {
        this.server = s;
    }

    public void setEmployee(OfficeEmployee officeEmployee) throws LogException {
        this.officeEmployee = officeEmployee;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ShowNameColumn.setCellValueFactory(new PropertyValueFactory<Show, String>("showName"));
        ShowArtistColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getArtist().getFirstName() + " " + param.getValue().getArtist().getLastName()));
        ShowDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getDateTime().toString().substring(0, 10)));
        ShowLocationColumn.setCellValueFactory(new PropertyValueFactory<Show, String>("showLocation"));
        ShowAvailableSeatsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getSeatsTotal() - param.getValue().getSeatsSold()));
        ShowSoldSeatsColumn.setCellValueFactory(new PropertyValueFactory<Show, Integer>("seatsSold"));
        showsTable.setItems(model1);

        ArtistNameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getArtist().getFirstName() + " " + param.getValue().getArtist().getLastName()));
        StartHourColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getDateTime().toString().substring(10, 19)));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<Show, String>("showLocation"));
        SeatsAvailableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getSeatsTotal() - param.getValue().getSeatsSold()));

        date.setValue(LocalDate.from(LocalDateTime.now()));
        artistsTable.setItems(model2);
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
                        if (Objects.equals(item, 0))
                            currentRow.setStyle("-fx-background-color:red");
                    }
                }
            };
        });
    }

    void setShows() {
        try {
            showsTable.getItems().clear();
            List<Show> shows = server.getShows();

            for (Show show : shows) {
                showsTable.getItems().add(show);
            }
        } catch (LogException e) {
            e.printStackTrace();
        }

        customRow(ShowAvailableSeatsColumn);
    }

    void setArtists() throws LogException {

        artistsTable.getItems().clear();
        List<Show> shows = server.getArtistsByDate(Timestamp.valueOf(date.getValue().atStartOfDay()));

        for (Show show : shows) {
            artistsTable.getItems().add(show);
        }
        customRow(SeatsAvailableColumn);
    }

    @FXML
    private void handleSearch() throws LogException {
        setArtists();
    }

    private void clear() {
        buyerName.setText("");
        List<Integer> list = new ArrayList<>();
        list.add(0);
        numberTickets.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Integer>(FXCollections.observableArrayList(list)));
    }

    @FXML
    private void handleBuyTickets() throws LogException {
        if (artistsTable.getSelectionModel().isEmpty()) {
            MessageAlert.showErrorMessage(null, "Please select a show first!");
        } else if (artistsTable.getSelectionModel().getSelectedItem().getDateTime().toLocalDateTime().isBefore(LocalDateTime.now())) {
            MessageAlert.showErrorMessage(null, "You can't buy any tickets at this Show!");
        } else {
            Long showId = artistsTable.getSelectionModel().getSelectedItem().getId();
            Long officeEmployeeId = officeEmployee.getId();

            String buyerN = buyerName.getText();
            Integer nrTickets = numberTickets.getValue();

            if (Objects.equals(buyerN, "")) {
                MessageAlert.showErrorMessage(null, "Buyer name is empty");
                return;
            }

            Show newShow = server.findShowById(showId);
            if (newShow.getSeatsTotal() - newShow.getSeatsSold() != 0) {
                newShow.setSeatsSold(newShow.getSeatsSold() + nrTickets);

                Ticket ticket = new Ticket(showId, officeEmployeeId, buyerN);
                for (int i = 0; i < nrTickets; i++)
                    server.buyTicket(ticket);

                server.updateShow(showId, newShow);
                clear();
            } else
                MessageAlert.showErrorMessage(null, "There are no longer available tickets!");
        }
    }


    @FXML
    private void handleClick() {
        int seatsAvailable = artistsTable.getSelectionModel().getSelectedItem().getSeatsTotal() - artistsTable.getSelectionModel().getSelectedItem().getSeatsSold();
        List<Integer> listValues = new ArrayList<Integer>();
        for (int i = 0; i <= seatsAvailable; i++)
            listValues.add(i);

        numberTickets.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Integer>(FXCollections.observableArrayList(listValues)));
    }

    @Override
    public void showUpdated(Show show) throws LogException {
        System.out.println("Show updated Employee Controller");

        Platform.runLater(() -> {
            int i = 0;
            for (Show s : model1) {
                if (Objects.equals(s.getId(), show.getId())) {
                    model1.set(i, show);
                    break;
                }
                i++;
            }
            customRow(ShowAvailableSeatsColumn);

            int j = 0;
            for (Show ss : model2) {
                if (Objects.equals(ss.getId(), show.getId())) {
                    model2.set(j, show);
                    break;
                }
                j++;
            }
            customRow(SeatsAvailableColumn);

        });


    }


    @FXML
    private void handleLogout(javafx.event.ActionEvent actionEvent) throws IOException {
        try {
            server.logOut(officeEmployee, this);
        } catch (LogException e) {
            System.out.println("Logout error " + e);
        }
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

}
