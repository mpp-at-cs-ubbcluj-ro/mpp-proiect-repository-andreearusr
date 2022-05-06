
import festival.network.utils.AbstractServer;
import festival.network.utils.FestivalRpcConcurrentServer;
import festival.network.utils.ServerException;
import festival.repository.ArtistRepository;
import festival.repository.OfficeEmployeeRepository;
import festival.repository.ShowRepository;
import festival.repository.TicketRepository;
import festival.repository.database.ArtistDBRepository;
import festival.repository.database.OfficeEmployeeDBRepository;
import festival.repository.database.ShowDBRepository;
import festival.repository.database.TicketDBRepository;
import festival.repository.orm.ArtistORMRepository;
import festival.server.ServiceImpl;
import festival.services.IFestivalService;

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

        ArtistRepository artistRepository = new ArtistDBRepository();
       //ArtistRepository artistRepository = new ArtistORMRepository();
        OfficeEmployeeRepository officeEmployeeRepository = new OfficeEmployeeDBRepository(serverProps);
        ShowRepository showRepository = new ShowDBRepository(serverProps, artistRepository);
        TicketRepository ticketRepository = new TicketDBRepository(serverProps,showRepository, officeEmployeeRepository);

        IFestivalService festivalServiceImpl = new ServiceImpl(artistRepository, officeEmployeeRepository, showRepository, ticketRepository);


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
