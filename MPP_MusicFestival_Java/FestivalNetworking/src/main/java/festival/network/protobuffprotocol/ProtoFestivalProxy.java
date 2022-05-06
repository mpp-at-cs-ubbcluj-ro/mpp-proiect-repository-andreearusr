package festival.network.protobuffprotocol;

import festival.domain.OfficeEmployee;
import festival.domain.Show;
import festival.domain.Ticket;
import festival.services.IFestivalObserver;
import festival.services.IFestivalService;
import festival.services.LogException;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoFestivalProxy implements IFestivalService {
    private String host;
    private int port;

    private IFestivalObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<FestivalProtobufs.FestivalResponse> qresponses;
    private volatile boolean finished;

    public ProtoFestivalProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<FestivalProtobufs.FestivalResponse>();
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

    private void sendRequest(FestivalProtobufs.FestivalRequest request) throws LogException {
        try {
            System.out.println("Sending request ..."+request);
            //output.writeObject(request);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new LogException("Error sending object " + e);
        }

    }

    private FestivalProtobufs.FestivalResponse readResponse() throws LogException {
        FestivalProtobufs.FestivalResponse response = null;
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
            //output = new ObjectOutputStream(connection.getOutputStream());
            output=connection.getOutputStream();
            //output.flush();
            //input = new ObjectInputStream(connection.getInputStream());
            input=connection.getInputStream();
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


    private void handleUpdate(FestivalProtobufs.FestivalResponse updateResponse) {
        switch (updateResponse.getType()) {
            case ShowUpdated:{
                Show show = ProtoUtils.getShowUpdated(updateResponse);
                System.out.println("Show updated");
                try {
                    client.showUpdated(show);
                } catch (LogException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private boolean isUpdateResponse(FestivalProtobufs.FestivalResponse.Type type) {
        switch (type){
            case ShowUpdated:  return true;
        }
        return false;
    }

    @Override
    public List<Show> getShows() throws LogException {
        sendRequest(ProtoUtils.createGetShowsRequest());
        FestivalProtobufs.FestivalResponse response = readResponse();
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new LogException(err);
        }

        List<Show> shows = ProtoUtils.getShows(response);
        return shows;
    }

    @Override
    public List<Show> getArtistsByDate(Timestamp date) throws LogException {
        sendRequest(ProtoUtils.createGetArtistsByDateRequest(date));
        FestivalProtobufs.FestivalResponse response = readResponse();
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new LogException(err);
        }

        List<Show> shows = ProtoUtils.getShowsByDate(response);
        return shows;
    }

    @Override
    public void buyTicket(Ticket ticket) throws LogException {
        sendRequest(ProtoUtils.createBuyTicketRequest(ticket));
        FestivalProtobufs.FestivalResponse response = readResponse();
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new LogException(err);
        }
    }

    @Override
    public void updateShow(Long showId, Show newShow) throws LogException {
        sendRequest(ProtoUtils.createUpdateShowRequest(showId, newShow));
        FestivalProtobufs.FestivalResponse response = readResponse();
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new LogException(err);
        }

    }

    @Override
    public Show findShowById(Long showId) throws LogException {
        sendRequest(ProtoUtils.createFindShowByIdRequest(showId));
        FestivalProtobufs.FestivalResponse response = readResponse();
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new LogException(err);
        }

        Show show = ProtoUtils.getShowById(response);
        return show;
    }

    @Override
    public OfficeEmployee findEmployeeByUsername(String username) throws LogException {
        initializeConnection();
        sendRequest(ProtoUtils.createFindEmployeeByUsernameRequest(username));

        FestivalProtobufs.FestivalResponse response = readResponse();
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Error) {
            String err = ProtoUtils.getError(response);
            throw new LogException(err);
        }

        OfficeEmployee officeEmployee = ProtoUtils.getOfficeE(response);
        return officeEmployee;
    }

    @Override
    public void logIn(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException {
        initializeConnection();
        System.out.println("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(officeEmployee));
        FestivalProtobufs.FestivalResponse response = readResponse();
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Ok) {
            this.client = client;
            return;
        }
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Error) {
            String errorText = ProtoUtils.getError(response);
            closeConnection();
            throw new LogException(errorText);
        }
    }

    @Override
    public void logOut(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException {
        sendRequest(ProtoUtils.createLogoutRequest(officeEmployee));
        FestivalProtobufs.FestivalResponse response = readResponse();
        closeConnection();
        if (response.getType() == FestivalProtobufs.FestivalResponse.Type.Error) {
            String errorText = ProtoUtils.getError(response);
            throw new LogException(errorText);
        }
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    //Object response = input.readObject();
                    FestivalProtobufs.FestivalResponse response = FestivalProtobufs.FestivalResponse.parseDelimitedFrom(input);
                    System.out.println("response received " + response);
                    if (isUpdateResponse(response.getType())) {
                        handleUpdate(response);
                    } else {

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }


}
