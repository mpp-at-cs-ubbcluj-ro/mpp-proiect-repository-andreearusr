package festival.repository.database;

import festival.domain.Artist;
import festival.domain.Show;
import festival.repository.ArtistRepository;
import festival.repository.ShowRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShowDBRepository implements ShowRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    private ArtistRepository artistRepository;

    public ShowDBRepository(Properties props, ArtistRepository artistRepository) {
        logger.info("Initializing ShowDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        this.artistRepository = artistRepository;
    }

    @Override
    public void save(Show entity) {
        logger.traceEntry("saving show {} ", entity);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("insert into Show(show_name, description, date_time, show_location, seats_total, seats_sold, artist_id) values (?,?,?,?,?,?,?)")) {
            preStmt.setString(1, entity.getShowName());
            preStmt.setString(2, entity.getDescription());
            preStmt.setString(3, entity.getDateTime().toString());
            preStmt.setString(4, entity.getShowLocation());
            preStmt.setInt(5, entity.getSeatsTotal());
            preStmt.setInt(6, entity.getSeatsSold());
            preStmt.setLong(7, entity.getArtist().getId());
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting show with {}", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("delete from Show where id=?")) {
            preStmt.setLong(1, aLong);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, Show entity) {
        logger.traceEntry("update show with id {} ", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("update Show SET show_name=?, description=?, date_time=?, show_location=?, seats_total=?, seats_sold=?, artist_id=? where id=?")){
            preStmt.setString(1, entity.getShowName());
            preStmt.setString(2, entity.getDescription());
            preStmt.setString(3, entity.getDateTime().toString());
            preStmt.setString(4, entity.getShowLocation());
            preStmt.setInt(5, entity.getSeatsTotal());
            preStmt.setInt(6, entity.getSeatsSold());
            preStmt.setLong(7, entity.getArtist().getId());
            preStmt.setLong(8, aLong);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Show findOne(Long aLong) {
        logger.traceEntry("finding show with id {} ", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from Show where id=?")) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("id");
                    String show_name = result.getString("show_name");
                    String description = result.getString("description");
                    Timestamp date_time = result.getTimestamp("date_time");
                    String show_location = result.getString("show_location");
                    int seats_total = result.getInt("seats_total");
                    int seats_sold = result.getInt("seats_sold");
                    long artist_id = result.getLong("artist_id");

                    Artist artist = artistRepository.findOne(artist_id);
                    Show show = new Show(id, show_name, description, artist, date_time, show_location, seats_total, seats_sold);
                    logger.traceExit(show);
                    return show;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No show found with id {}", aLong);
        return null;
    }

    @Override
    public List<Show> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Show")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("id");
                    String show_name = result.getString("show_name");
                    String description = result.getString("description");
                    Timestamp date_time = result.getTimestamp("date_time");
                    String show_location = result.getString("show_location");
                    int seats_total = result.getInt("seats_total");
                    int seats_sold = result.getInt("seats_sold");
                    long artist_id = result.getLong("artist_id");

                    Artist artist = artistRepository.findOne(artist_id);
                    Show show = new Show(id, show_name, description, artist, date_time, show_location, seats_total, seats_sold);
                    shows.add(show);
                }
            }

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return shows;
    }

    @Override
    public List<Show> getArtistsByDate(Timestamp dateTime) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from Show where date(Show.date_time) = ? order by Show.artist_id")) {
            preStmt.setString(1, dateTime.toString().substring(0, 10));
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("id");
                    String show_name = result.getString("show_name");
                    String description = result.getString("description");
                    Timestamp date_time = result.getTimestamp("date_time");
                    String show_location = result.getString("show_location");
                    int seats_total = result.getInt("seats_total");
                    int seats_sold = result.getInt("seats_sold");
                    long artist_id = result.getLong("artist_id");

                    Artist artist = artistRepository.findOne(artist_id);
                    Show show = new Show(id, show_name, description, artist, date_time, show_location, seats_total, seats_sold);
                    shows.add(show);
                }
            }

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return shows;
    }
}
