package repository;

import domain.Artist;

import java.util.List;

public interface ShowRepository extends Repository<Long, domain.Show> {
    List<Artist> getArtistsByDate(String date);
}
