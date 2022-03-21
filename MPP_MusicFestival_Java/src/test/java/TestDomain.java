
import domain.*;
import festival.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDomain {
    @Test
    @DisplayName("Test Entity")
    public void testEntity() {
        Entity<Integer> entity = new Entity<Integer>(3);
        assertEquals(entity.getId(), 3);
        assertEquals(entity.toString(), "3");
    }

    @Test
    @DisplayName("Test Artist")
    public void testArtist() {
        Artist artist = new Artist(1L, "Alejandro", "Speitzer", 25, "Mexic");

        assertEquals(artist.getId(), 1);
        assertEquals(artist.getFirstName(), "Alejandro");
        assertEquals(artist.getLastName(), "Speitzer");
        assertEquals(artist.getAge(), 25);
        assertEquals(artist.getOriginCountry(), "Mexic");
        assertEquals(artist.toString(), "Artist- Nume:Speitzer Prenume:Alejandro Varsta:25 Tara origine:Mexic");

        artist.setId(5L);
        artist.setFirstName("Ale");
        artist.setLastName("Rauw");
        artist.setAge(29);
        artist.setOriginCountry("Puerto Rico");

        assertEquals(artist.getId(), 5);
        assertEquals(artist.getFirstName(), "Ale");
        assertEquals(artist.getLastName(), "Rauw");
        assertEquals(artist.getAge(), 29);
        assertEquals(artist.getOriginCountry(), "Puerto Rico");
        assertEquals(artist.toString(), "Artist- Nume:Rauw Prenume:Ale Varsta:29 Tara origine:Puerto Rico");
    }

    @Test
    @DisplayName("Test Office Employee")
    public void testOfficeEmployee() {

        OfficeEmployee officeEmployee = new OfficeEmployee(7L, "Raluca", "Rus", 6041778234556L, "raluca_33", "Pisica1234");

        assertEquals(officeEmployee.getId(), 7);
        assertEquals(officeEmployee.getFirstName(), "Raluca");
        assertEquals(officeEmployee.getLastName(), "Rus");
        assertEquals(officeEmployee.getCNP(), 6041778234556L);
        assertEquals(officeEmployee.getUsername(), "raluca_33");
        assertEquals(officeEmployee.getPassword(), "Pisica1234");
        assertEquals(officeEmployee.toString(), "Cod angajat:7 Nume:Rus Prenume:Raluca CNP:6041778234556");

        officeEmployee.setId(123L);
        officeEmployee.setLastName("Sava");
        officeEmployee.setFirstName("Alexandra");
        officeEmployee.setUsername("raluca_31");
        officeEmployee.setPassword("PisicaMea1234");
        officeEmployee.setCNP(6112278234556L);

        assertEquals(officeEmployee.getId(), 123);
        assertEquals(officeEmployee.getFirstName(), "Alexandra");
        assertEquals(officeEmployee.getLastName(), "Sava");
        assertEquals(officeEmployee.getCNP(), 6112278234556L);
        assertEquals(officeEmployee.getUsername(), "raluca_31");
        assertEquals(officeEmployee.getPassword(), "PisicaMea1234");
        assertEquals(officeEmployee.toString(), "Cod angajat:123 Nume:Sava Prenume:Alexandra CNP:6112278234556");
    }

    @Test
    @DisplayName("Test Ticket")
    public void testTicket() {
        Artist artist = new Artist(1L, "Anuel", "Santiago", 29, "Puerto Rico");
        Timestamp dateTime = Timestamp.valueOf("2022-05-29 20:30:00");

        Show show = new Show(43L, "LatinoParty", "Spectacol cu artisti latino din toate regiunile, nu ratati!",
                artist, dateTime, "Ibitza", 20000, 1500);
        OfficeEmployee officeEmployee = new OfficeEmployee(78L, "Raluca", "Rus", 6041778234556L,"raluca_33", "Pisica1234");

        Ticket ticket = new Ticket(67L, 43L, 78L, "Andreea Rus");

        assertEquals(ticket.getId(), 67L);
        assertEquals(ticket.getShowId(), 43L);
        assertEquals(ticket.getEmployeeId(), 78L);
        assertEquals(ticket.getBuyerName(), "Andreea Rus");
        assertEquals(ticket.toString(), "Bilet- Cod:67 Nume cumparator:Andreea Rus");

        Show show1 = new Show(56L, "VivaReggaeton", "Reggaeton partyy",
                artist, Timestamp.valueOf("2019-08-11 17:00:00"), "Bucuresti", 3000, 150);
        OfficeEmployee officeEmployee1 = new OfficeEmployee(99L, "Andrei", "Rusu", 6111799234553L,"rusua", "masina");

        ticket.setId(123L);
        ticket.setShowId(56L);
        ticket.setEmployeeId(99L);
        ticket.setBuyerName("Maria Runcan");


        assertEquals(ticket.getId(), 123L);
        assertEquals(ticket.getShowId(), 56L);
        assertEquals(ticket.getEmployeeId(), 99L);
        assertEquals(ticket.getBuyerName(), "Maria Runcan");
        assertEquals(ticket.toString(), "Bilet- Cod:123 Nume cumparator:Maria Runcan");


    }

    @Test
    @DisplayName("Test Show")
    public void testShow() {
        Artist artist = new Artist(1L, "Anuel", "Santiago", 29, "Puerto Rico");
        Timestamp dateTime = Timestamp.valueOf("2022-05-29 20:30:00");

        Show show = new Show(456L, "LatinoParty", "Spectacol cu artisti latino din toate regiunile, nu ratati!",
                artist, dateTime, "Ibitza", 20000, 1500);

        assertEquals(show.getId(), 456L);
        assertEquals(show.getShowName(), "LatinoParty");
        assertEquals(show.getDescription(), "Spectacol cu artisti latino din toate regiunile, nu ratati!");
        assertEquals(show.getArtist().getId(), 1);
        assertEquals(show.getDateTime(), Timestamp.valueOf("2022-05-29 20:30:00"));
        assertEquals(show.getShowLocation(), "Ibitza");
        assertEquals(show.getSeatsAvailable(), 20000);
        assertEquals(show.getSeatsSold(), 1500);
        assertEquals(show.toString(), "Spectacol- Nume:LatinoParty Descriere:Spectacol cu artisti latino din toate regiunile, nu ratati!" +
                "\nnume Artist:Anuel" +
                "\nData si ora:2022-05-29 20:30:00.0 Locatia:Ibitza");

        show.setDateTime(Timestamp.valueOf("2022-07-15 21:00:00"));
        show.setId(500L);
        show.setShowName("LatinoBaby");
        show.setSeatsAvailable(20200);
        show.setSeatsSold(1900);
        show.setShowLocation("Ibitza Beach Club");
        show.setDescription("Spectacol cu artisti latino din toate regiunile");
        artist.setLastName("AA");
        show.setArtist(artist);

        assertEquals(show.getId(), 500L);
        assertEquals(show.getShowName(), "LatinoBaby");
        assertEquals(show.getDescription(), "Spectacol cu artisti latino din toate regiunile");
        assertEquals(show.getArtist().getId(), 1);
        assertEquals(show.getDateTime(), Timestamp.valueOf("2022-07-15 21:00:00"));
        assertEquals(show.getShowLocation(), "Ibitza Beach Club");
        assertEquals(show.getSeatsAvailable(), 20200);
        assertEquals(show.getSeatsSold(), 1900);
        assertEquals(show.toString(), "Spectacol- Nume:LatinoBaby Descriere:Spectacol cu artisti latino din toate regiunile" +
                "\nnume Artist:Anuel" +
                "\nData si ora:2022-07-15 21:00:00.0 Locatia:Ibitza Beach Club");

    }

}
