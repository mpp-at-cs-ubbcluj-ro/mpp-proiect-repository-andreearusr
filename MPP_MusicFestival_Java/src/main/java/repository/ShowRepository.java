package repository;

import domain.Artist;
import domain.Show;

import java.sql.Timestamp;
import java.util.List;

public interface ShowRepository extends Repository<Long, domain.Show> {
    List<Show> getArtistsByDate(Timestamp date);
}
