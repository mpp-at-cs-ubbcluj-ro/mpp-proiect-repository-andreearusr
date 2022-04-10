package repository.database;

import domain.OfficeEmployee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.OfficeEmployeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OfficeEmployeeDBRepository implements OfficeEmployeeRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public OfficeEmployeeDBRepository(Properties props) {
        logger.info("Initializing OfficeEmployeeDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }


    @Override
    public Long getId(String username) {
        logger.traceEntry("finding office employee id with username {} ", username);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from OfficeEmployee where username=?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("id");

                    logger.traceExit(id);
                    return id;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No office employee found with username {}", username);
        return null;
    }

    @Override
    public String getPassword(Long id) {
        logger.traceEntry("finding office employee password with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from OfficeEmployee where id=?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String username = result.getString("password");

                    logger.traceExit(username);
                    return username;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No office employee password found with id {}", id);
        return null;
    }

    @Override
    public Long getId(Long CNP){
        logger.traceEntry("finding office employee id with CNP {} ", CNP);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from OfficeEmployee where CNP=?")) {
            preStmt.setLong(1, CNP);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("id");

                    logger.traceExit(id);
                    return id;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No office employee found with CNP {}", CNP);
        return null;
    }


    @Override
    public void save(OfficeEmployee entity) {
        logger.traceEntry("saving office employee {} ", entity);
        Connection con = dbUtils.getConnection();

        if (getId(entity.getCNP()) != null)
            logger.error("This entity already exists");
        else {
            try (PreparedStatement preStmt = con.prepareStatement("insert into OfficeEmployee(first_name, last_name, CNP, username, password) values (?,?,?,?,?)")) {
                preStmt.setString(1, entity.getFirstName());
                preStmt.setString(2, entity.getLastName());
                preStmt.setLong(3, entity.getCNP());
                preStmt.setString(4, entity.getUsername());
                preStmt.setString(5, entity.getPassword());
                preStmt.executeUpdate();
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
            logger.traceExit();
        }
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting office employee with {}", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("delete from OfficeEmployee where id=?")) {
            preStmt.setLong(1, aLong);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, OfficeEmployee entity) {
    }

    @Override
    public OfficeEmployee findOne(Long aLong) {
        logger.traceEntry("finding office employee with id {} ", aLong);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from OfficeEmployee where id=?")) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("id");
                    String first_name = result.getString("first_name");
                    String last_name = result.getString("last_name");
                    long CNP = result.getLong("CNP");
                    String username = result.getString("username");
                    String password = result.getString("password");

                    OfficeEmployee officeEmployee = new OfficeEmployee(id, first_name, last_name, CNP, username, password);
                    logger.traceExit(officeEmployee);
                    return officeEmployee;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No office employee found with id {}", aLong);
        return null;
    }

    @Override
    public List<OfficeEmployee> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<OfficeEmployee> officeEmployees = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from OfficeEmployee")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("id");
                    String first_name = result.getString("first_name");
                    String last_name = result.getString("last_name");
                    long CNP = result.getLong("CNP");
                    String username = result.getString("username");
                    String password = result.getString("password");

                    OfficeEmployee officeEmployee = new OfficeEmployee(id, first_name, last_name, CNP, username, password);
                    officeEmployees.add(officeEmployee);
                }
            }

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return officeEmployees;
    }
}
