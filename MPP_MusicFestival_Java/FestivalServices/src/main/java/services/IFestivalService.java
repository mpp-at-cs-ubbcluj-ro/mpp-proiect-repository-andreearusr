package services;

import domain.OfficeEmployee;
import domain.Show;

import java.sql.Timestamp;
import java.util.List;

public interface IFestivalService {

    List<Show> getShows() throws LogException;

    List<Show> getArtistsByDate(Timestamp date) throws LogException;

    void buyTicket(Long showId, Long officeEmployeeId, String buyerName) throws LogException;

    void updateShow(Long showId, Show newShow) throws LogException;

    Show findShowById(Long showId) throws LogException;

    OfficeEmployee findEmployeeByUsername(String username) throws LogException;


    void logIn(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException;

    void logOut(OfficeEmployee officeEmployee, IFestivalObserver client) throws LogException;

}
