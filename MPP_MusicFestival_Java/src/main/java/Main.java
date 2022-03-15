import domain.Artist;
import domain.OfficeEmployee;
import domain.Show;
import domain.Ticket;
import repository.ArtistDBRepository;
import repository.OfficeEmployeeDBRepository;
import repository.ShowDBRepository;
import repository.TicketDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello!");

        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        ArtistDBRepository artistRepo = new ArtistDBRepository(props);
        System.out.println("Toti artistii din db");
        for (Artist artist : artistRepo.findAll())
            System.out.println(artist);

        //System.out.println(artistRepo.findOne(2L).toString());
        //artistRepo.save(new Artist("Lunay","Moreno",25,"Ibitza"));
        artistRepo.delete(2L);


        OfficeEmployeeDBRepository employeeRepo = new OfficeEmployeeDBRepository(props);
        employeeRepo.save(new OfficeEmployee("Andreea", "Rus", 6010109234332L, "andre_11","Pisica"));
        employeeRepo.save(new OfficeEmployee("Andreea", "Moldovan", 6050109234232L, "andre_10","Catel"));
        System.out.println(employeeRepo.findOne(4L));
        for (OfficeEmployee employee : employeeRepo.findAll())
            System.out.println(employee);
        System.out.println(employeeRepo.getId(6010109234332L));
        System.out.println(employeeRepo.getId("andre_10"));
        System.out.println(employeeRepo.getPassword(3L));
        employeeRepo.delete(3L);


        ShowDBRepository showRepo = new ShowDBRepository(props, artistRepo);
        //showRepo.save(new Show("FridayNightParty", "Distractie pana in zori", artistRepo.findOne(4L), Timestamp.valueOf("2022-03-22 23:00:00"),"Cluj-Napoca, Revolution Club", 120, 40));
        //showRepo.save(new Show("Latino", "Haide sa dansam", artistRepo.findOne(1L), Timestamp.valueOf("2022-04-12 20:00:00"),"Cluj-Napoca, Cluj Arena", 250, 50));
        System.out.println(showRepo.findOne(4L));
        for(Show show : showRepo.findAll()){
            System.out.println(show);
        }
        showRepo.delete(1L);
        for(Show show :showRepo.getArtistsByDate(Timestamp.valueOf("2022-04-12 00:00:00"))){
            System.out.println(show);
        }

        TicketDBRepository ticketRepo = new TicketDBRepository(props, showRepo, employeeRepo);
        //ticketRepo.save(new Ticket(4L, 5L, "Sava Tudor"));
        //ticketRepo.save(new Ticket(6L, 5L, "Pruteanu Robert"));

        System.out.println(ticketRepo.findOne(2L));
        for(Ticket ticket: ticketRepo.findAll())
            System.out.println(ticket);

        ticketRepo.delete(1L);

    }
}
