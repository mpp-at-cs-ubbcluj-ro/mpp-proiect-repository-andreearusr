package festival.services;

import festival.domain.OfficeEmployee;
import festival.domain.Show;
import festival.domain.Ticket;

import java.sql.Timestamp;
import java.util.List;

public interface IFestivalService {

    List<Show> getShows() throws LogException;

    List<Show> getArtistsByDate(Timestamp date) throws LogException;

    void buyTicket(Ticket ticket) throws LogException;

    void updateShow(Long showId, Show newShow) throws LogException;

    Show findShowById(Long showId) throws LogException;

    OfficeEmployee findEmployeeByUsername(String username) throws LogException;


    void logIn(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException;

    void logOut(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException;

}
