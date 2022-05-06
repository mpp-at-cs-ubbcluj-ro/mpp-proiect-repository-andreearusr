package festival.network.rpcprotocol;

import festival.domain.OfficeEmployee;
import festival.domain.Show;
import festival.domain.Ticket;
import festival.services.IFestivalObserver;
import festival.services.IFestivalService;
import festival.services.LogException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.List;


public class FestivalClientRpcReflectionWorker implements Runnable, IFestivalObserver {
    private IFestivalService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public FestivalClientRpcReflectionWorker(IFestivalService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("HandlerName " + handlerName);
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }


    @Override
    public void showUpdated(Show show) {
        Response resp = new Response.Builder().type(ResponseType.SHOW_UPDATED).data(show).build();
        System.out.println("Show updated reflexion ");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Response handleLOGIN(Request request) {
        System.out.println("Login request ..." + request.type());
        OfficeEmployee officeEmployee = (OfficeEmployee) request.data();
        try {
            server.logIn(officeEmployee, this);
            return okResponse;
        } catch (LogException e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        System.out.println("Logout request...");
        OfficeEmployee officeEmployee = (OfficeEmployee) request.data();
        try {
            server.logOut(officeEmployee, this);
            connected = false;
            return okResponse;
        } catch (LogException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }

    }

    private Response handleFIND_SHOW_BY_ID(Request request) {
        System.out.println("FindShowById Request ...");
        Long id = (Long) request.data();
        try {
            Show show = server.findShowById(id);
            return new Response.Builder().type(ResponseType.FIND_SHOW_BY_ID).data(show).build();
        } catch (LogException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleFIND_EMPLOYEE_BY_USERNAME(Request request) {
        System.out.println("FindEmployeeByUsername Request ...");
        String username = (String) request.data();
        try {
            OfficeEmployee officeEmployee = server.findEmployeeByUsername(username);
            return new Response.Builder().type(ResponseType.FIND_EMPLOYEE_BY_USERNAME).data(officeEmployee).build();
        } catch (LogException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
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
        Ticket ticket = (Ticket) request.data();

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
