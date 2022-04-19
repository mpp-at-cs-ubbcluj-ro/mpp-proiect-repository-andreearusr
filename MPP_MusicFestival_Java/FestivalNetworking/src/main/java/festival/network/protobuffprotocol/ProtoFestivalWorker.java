package festival.network.protobuffprotocol;
/*
import domain.OfficeEmployee;
import domain.Show;
import domain.Ticket;
import festival.network.rpcprotocol.Request;
import festival.network.rpcprotocol.Response;
import festival.network.rpcprotocol.ResponseType;
import services.IFestivalObserver;
import services.IFestivalService;
import services.LogException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.List;

public class ProtoFestivalWorker  implements Runnable, IFestivalObserver{
    private IFestivalService server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public ProtoFestivalWorker(IFestivalService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            //output = new ObjectOutputStream(connection.getOutputStream());
            //output.flush();
            //input = new ObjectInputStream(connection.getInputStream());
            output=connection.getOutputStream();
            input=connection.getInputStream();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                //Object request = input.readObject();
                System.out.println("Waiting requests ...");
                FestivalProtobufs.FestivalRequest request=FestivalProtobufs.FestivalRequest.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                FestivalProtobufs.FestivalResponse response=handleRequest(request);

                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }


    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private FestivalProtobufs.FestivalResponse handleRequest(FestivalProtobufs.FestivalRequest request) {
        FestivalProtobufs.FestivalResponse response = null;
        switch (request.getType()){
            case Login:{
                System.out.println("Login request ...");
                OfficeEmployee officeEmployee = ProtoUtils.getOfficeEmployee(request);
                try {
                    server.logIn(officeEmployee, this);
                    return ProtoUtils.createOkResponse();
                } catch (LogException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

            case Logout:{
                System.out.println("Logout request...");
                OfficeEmployee officeEmployee = ProtoUtils.getOfficeEmployee(request);
                try {
                    server.logOut(officeEmployee, this);
                    connected = false;
                    return ProtoUtils.createOkResponse();
                } catch (LogException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

            case FindShowById:{
                System.out.println("FindShowById Request ...");
                Long id = ProtoUtils.getShowId(request);
                try {
                    Show show = server.findShowById(id);
                   // return ProtoUtils.(show);
                } catch (LogException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case FindEmployeeByUsername:{
                System.out.println("FindEmployeeByUsername Request ...");
                //String username = ProtoUtils
                try {
                    OfficeEmployee officeEmployee = server.findEmployeeByUsername(username);
                    //return ProtoUtils.createFind(officeEmployee.getUsername());
                } catch (LogException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

            case GetShows:{

            }

            case GetArtistsByDate:{

            }

            case BuyTicket:{

            }

            case UpdateShow:{

            }
        }



        return response;
    }

    private void sendResponse(FestivalProtobufs.FestivalResponse response) throws IOException {
        System.out.println("sending response " + response);
        //output.writeObject(response);
        response.writeDelimitedTo(output);
        output.flush();
    }


    @Override
    public void showUpdated(Show show) {
        Response resp = new Response.Builder().type(ResponseType.SHOW_UPDATED).data(show).build();
        System.out.println("Show updated reflexion ");
        try {
            sendResponse(ProtoUtils.create);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Response handleGET_SHOWS(Request request) {
        System.out.println("GetShows Request ...");
        try {
            List<Show> shows = server.getShows();
            return new Response.Builder().type(ResponseType.SHOWS).data(shows).build();
        } catch (LogException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ARTISTS_BY_DATE(Request request) {
        System.out.println("GetArtistsByDate Request ...");
        Timestamp date = (Timestamp) request.data();
        try {
            List<Show> shows = server.getArtistsByDate(date);
            return new Response.Builder().type(ResponseType.GET_ARTISTS_BY_DATE).data(shows).build();
        } catch (LogException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleBUY_TICKET(Request request) {
        System.out.println("BuyTicket Request ...");
        List<Object> list = (List<Object>) request.data();
        Long showId = (Long) list.get(0);
        Long officeEmployeeId = (Long) list.get(1);
        String buyerName = (String) list.get(2);
        Ticket ticket = new Ticket(showId, officeEmployeeId, buyerName);

        try {
            server.buyTicket(ticket);
            return okResponse;
        } catch (LogException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }

    }

    private Response handleUPDATE_SHOW(Request request) {
        System.out.println("UpdateShow Request ...");
        List<Object> list = (List<Object>) request.data();
        Long showId = (Long) list.get(0);
        Show newShow = (Show) list.get(1);
        try {
            server.updateShow(showId, newShow);
            return okResponse;
        } catch (LogException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }

    }


}

*/