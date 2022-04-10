package network.rpcprotocol;

import domain.OfficeEmployee;
import domain.Show;
import domain.Ticket;
import services.IFestivalObserver;
import services.IFestivalService;
import services.LogException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class FestivalServicesRpcProxy implements IFestivalService {
    private String host;
    private int port;

    private IFestivalObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public FestivalServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws LogException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new LogException("Error sending object " + e);
        }

    }

    private Response readResponse() throws LogException {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws LogException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.SHOW_UPDATED) {
            Show show = (Show) response.data();
            System.out.println("Show updated");
            try {
                client.showUpdated(show);
            } catch (LogException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.SHOW_UPDATED;
    }

    @Override
    public List<Show> getShows() throws LogException {
        Request req = new Request.Builder().type(RequestType.GET_SHOWS).data(null).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new LogException(err);
        }

        List<Show> shows = (List<Show>) response.data();
        return shows;
    }

    @Override
    public List<Show> getArtistsByDate(Timestamp date) throws LogException {
        Request req = new Request.Builder().type(RequestType.GET_ARTISTS_BY_DATE).data(date).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new LogException(err);
        }

        List<Show> shows = (List<Show>) response.data();
        return shows;
    }

    @Override
    public void buyTicket(Long showId, Long officeEmployeeId, String buyerName) throws LogException {
        List<Object> list = new ArrayList<>();
        list.add(showId);
        list.add(officeEmployeeId);
        list.add(buyerName);
        Request req = new Request.Builder().type(RequestType.BUY_TICKET).data(list).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new LogException(err);
        }
    }

    @Override
    public void updateShow(Long showId, Show newShow) throws LogException {
        List<Object> list = new ArrayList<>();
        list.add(showId);
        list.add(newShow);
        Request req = new Request.Builder().type(RequestType.UPDATE_SHOW).data(list).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new LogException(err);
        }

    }

    @Override
    public Show findShowById(Long showId) throws LogException {
        Request req = new Request.Builder().type(RequestType.FIND_SHOW_BY_ID).data(showId).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new LogException(err);
        }

        Show show = (Show) response.data();
        return show;
    }

    @Override
    public OfficeEmployee findEmployeeByUsername(String username) throws LogException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.FIND_EMPLOYEE_BY_USERNAME).data(username).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new LogException(err);
        }

        OfficeEmployee officeEmployee = (OfficeEmployee) response.data();
        return officeEmployee;
    }

    @Override
    public void logIn(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(officeEmployee).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new LogException(err);
        }
    }

    @Override
    public void logOut(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException {

        Request req = new Request.Builder().type(RequestType.LOGOUT).data(officeEmployee).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new LogException(err);
        }
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }


}
