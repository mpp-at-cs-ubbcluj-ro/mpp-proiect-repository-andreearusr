package repository;

import domain.Artist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ArtistDBRepository implements ArtistRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ArtistDBRepository(Properties props) {
        logger.info("Initializing ArtistDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }


    @Override
    public void save(Artist entity) {
        logger.traceEntry("saving artist {} ", entity);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("insert into Artist(first_name, last_name, age, origin_country) values (?,?,?,?)")) {
            preStmt.setString(1, entity.getFirstName());
            preStmt.setString(2, entity.getLastName());
            preStmt.setInt(3, entity.getAge());
            preStmt.setString(4, entity.getOriginCountry());
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting artist with {}", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("delete from Artist where id=?")) {
            preStmt.setLong(1, aLong);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, Artist entity) {
    }

    @Override
    public Artist findOne(Long aLong) {
        logger.traceEntry("finding artist with id {} ", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from Artist where id=?")) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("id");
                    String first_name = result.getString("first_name");
                    String last_name = result.getString("last_name");
                    int age = result.getInt("age");
                    String origin_country = result.getString("origin_country");

                    Artist artist = new Artist(id, first_name, last_name, age, origin_country);
                    logger.traceExit(artist);
                    return artist;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No artist found with id {}", aLong);
        return null;
    }

    @Override
    public List<Artist> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Artist> artists = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Artist")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("id");
                    String first_name = result.getString("first_name");
                    String last_name = result.getString("last_name");
                    int age = result.getInt("age");
                    String origin_country = result.getString("origin_country");

                    Artist artist = new Artist(id, first_name, last_name, age, origin_country);
                    artists.add(artist);
                }
            }

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return artists;
    }
}
