package repository;

import domain.Artist;
import domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketDBRepository implements TicketRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    private ShowRepository showRepository;
    private OfficeEmployeeRepository officeEmployeeRepository;

    public TicketDBRepository(Properties props, ShowRepository showRepository, OfficeEmployeeRepository officeEmployeeRepository) {
        logger.info("Initializing TicketDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        this.showRepository = showRepository;
        this.officeEmployeeRepository = officeEmployeeRepository;
    }


    @Override
    public void save(Ticket entity) {
        logger.traceEntry("saving ticket {} ", entity);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("insert into Ticket(show_id, employee_id, buyer_name) values (?,?,?)")) {
            preStmt.setLong(1, entity.getShowId());
            preStmt.setLong(2, entity.getEmployeeId());
            preStmt.setString(3, entity.getBuyerName());
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting ticket with {}", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("delete from Ticket where id=?")) {
            preStmt.setLong(1, aLong);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, Ticket entity) {
    }

    @Override
    public Ticket findOne(Long aLong) {
        logger.traceEntry("finding ticket with id {} ", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from Ticket where id=?")) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("id");
                    long show_id = result.getLong("show_id");
                    long employee_id = result.getLong("employee_id");
                    String buyer_name = result.getString("buyer_name");

                    Ticket ticket = new Ticket(id, show_id, employee_id, buyer_name);
                    logger.traceExit(ticket);
                    return ticket;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No ticket found with id {}", aLong);
        return null;
    }

    @Override
    public List<Ticket> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Ticket")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("id");
                    long show_id = result.getLong("show_id");
                    long employee_id = result.getLong("employee_id");
                    String buyer_name = result.getString("buyer_name");

                    Ticket ticket = new Ticket(id, show_id, employee_id, buyer_name);
                    tickets.add(ticket);
                }
            }

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return tickets;
    }
}
