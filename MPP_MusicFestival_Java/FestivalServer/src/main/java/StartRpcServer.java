
import network.utils.AbstractServer;
import network.utils.FestivalRpcConcurrentServer;
import network.utils.ServerException;
import repository.ArtistRepository;
import repository.OfficeEmployeeRepository;
import repository.ShowRepository;
import repository.TicketRepository;
import repository.database.ArtistDBRepository;
import repository.database.OfficeEmployeeDBRepository;
import repository.database.ShowDBRepository;
import repository.database.TicketDBRepository;
import server.ServiceImpl;
import services.IFestivalService;
import services.LogException;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {

        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/festivalserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }

        ArtistRepository artistRepository = new ArtistDBRepository(serverProps);
        OfficeEmployeeRepository officeEmployeeRepository = new OfficeEmployeeDBRepository(serverProps);
        ShowRepository showRepository = new ShowDBRepository(serverProps, artistRepository);
        TicketRepository ticketRepository = new TicketDBRepository(serverProps,showRepository, officeEmployeeRepository);

        IFestivalService festivalServiceImpl = new ServiceImpl(artistRepository, officeEmployeeRepository, showRepository, ticketRepository);

        try {
            System.out.println(festivalServiceImpl.findShowById(4L));
        } catch (LogException e) {
            e.printStackTrace();
        }
        int festivalServerPort = defaultPort;
        try {
            festivalServerPort = Integer.parseInt(serverProps.getProperty("festival.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + festivalServerPort);
        AbstractServer server = new FestivalRpcConcurrentServer(festivalServerPort, festivalServiceImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
