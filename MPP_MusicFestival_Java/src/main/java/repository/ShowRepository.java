package repository;

import domain.Show;

import java.sql.Timestamp;
import java.util.List;

public interface ShowRepository extends Repository<Long, Show> {
    List<Show> getArtistsByDate(Timestamp date);
}
