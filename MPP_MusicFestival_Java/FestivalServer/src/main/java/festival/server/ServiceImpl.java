package festival.server;

import festival.domain.OfficeEmployee;
import festival.domain.Show;
import festival.domain.Ticket;
import festival.repository.ArtistRepository;
import festival.repository.OfficeEmployeeRepository;
import festival.repository.ShowRepository;
import festival.repository.TicketRepository;
import festival.services.IFestivalObserver;
import festival.services.IFestivalService;
import festival.services.LogException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImpl implements IFestivalService {

    private ArtistRepository artistRepository;
    private OfficeEmployeeRepository officeEmployeeRepository;
    private ShowRepository showRepository;
    private TicketRepository ticketRepository;
    private Map<Long, IFestivalObserver> loggedEmployees;

    public ServiceImpl(ArtistRepository artistRepository, OfficeEmployeeRepository officeEmployeeRepository, ShowRepository showRepository, TicketRepository ticketRepository) {
        this.artistRepository = artistRepository;
        this.officeEmployeeRepository = officeEmployeeRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        loggedEmployees = new ConcurrentHashMap<>();
    }

    public synchronized List<Show> getShows() {
        return showRepository.findAll();
    }

    public synchronized List<Show> getArtistsByDate(Timestamp date) {
        return showRepository.getArtistsByDate(date);
    }


    public synchronized void buyTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }


    private final int defaultThreadsNo = 5;
    public synchronized void updateShow(Long showId, Show newShow) {
        showRepository.update(showId, newShow);
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (Map.Entry<Long, IFestivalObserver> entry : loggedEmployees.entrySet()) {
            //IFestivalObserver festivalClient = entry.getValue();
            //if (festivalClient != null)
            executor.execute(() ->
            {
                try {
                    entry.getValue().showUpdated(newShow);
                } catch (LogException e) {
                    e.printStackTrace();
                }
            });

        }
        executor.shutdown();
    }



    //private void sendUpdate(Show show) {

    //}

    public synchronized Show findShowById(Long showId) {
        return showRepository.findOne(showId);
    }

    public synchronized OfficeEmployee findEmployeeByUsername(String username) {
        Long id = officeEmployeeRepository.getId(username);
        return officeEmployeeRepository.findOne(id);
    }

    @Override
    public synchronized void logIn(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException {
        Long employeeId = officeEmployeeRepository.getId(officeEmployee.getUsername());
        if (employeeId != null) {
            if (loggedEmployees.get(employeeId) != null)
                throw new LogException("OfficeEmployee already logged in.");
            loggedEmployees.put(employeeId, client);
        } else
            throw new LogException("Authentication failed.");
    }

    @Override
    public synchronized void logOut(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException {
        IFestivalObserver localClient = loggedEmployees.remove(officeEmployee.getId());
        if (localClient == null)
            throw new LogException("OfficeEmployee " + officeEmployee.getId() + "is not logged in.");
    }


}

