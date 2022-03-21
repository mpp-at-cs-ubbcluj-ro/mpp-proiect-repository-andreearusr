package service;

import domain.Show;
import domain.Ticket;
import repository.ArtistRepository;
import repository.OfficeEmployeeRepository;
import repository.ShowRepository;
import repository.TicketRepository;

import java.sql.Timestamp;
import java.util.List;

public class Service {
    private ArtistRepository artistRepository;
    private OfficeEmployeeRepository officeEmployeeRepository;
    private ShowRepository showRepository;
    private TicketRepository ticketRepository;

    public Service(ArtistRepository artistRepository, OfficeEmployeeRepository officeEmployeeRepository, ShowRepository showRepository, TicketRepository ticketRepository) {
        this.artistRepository = artistRepository;
        this.officeEmployeeRepository = officeEmployeeRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Show> getShows(){
        return showRepository.findAll();
    }

    public List<Show> getArtistsByDate(Timestamp date){
        return showRepository.getArtistsByDate(date);
    }

    public void buyTicket(Long showId, Long officeEmployeeId, String buyerName){
        Ticket ticket = new Ticket(showId, officeEmployeeId, buyerName);
        ticketRepository.save(ticket);
    }

    public void updateShow(Long showId, Show newShow){
        showRepository.update(showId, newShow);
    }

    public Show findShowById(Long showId){
        return showRepository.findOne(showId);
    }

}
