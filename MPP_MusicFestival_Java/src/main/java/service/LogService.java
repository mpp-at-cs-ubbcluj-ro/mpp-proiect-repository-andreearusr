package service;

import exceptions.LogException;
import repository.OfficeEmployeeRepository;

import java.util.Objects;

public class LogService {

    private final OfficeEmployeeRepository officeEmployeeRepository;

    public LogService(OfficeEmployeeRepository officeEmployeeRepository) {
        this.officeEmployeeRepository = officeEmployeeRepository;
    }

    private Long employeeId = null;

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long logIn(String username, String password) throws LogException {
        if (employeeId != null) {
            throw new LogException("Already logged in!");
        }
        Long id = officeEmployeeRepository.getId(username);

        if (id==null) {
            throw new LogException("Wrong username or password!");
        }

        if (Objects.equals(officeEmployeeRepository.getPassword(id), password)) {
            employeeId = id;
        } else {
            throw new LogException("Wrong username or password!");
        }

        return id;
    }

    public void logOut() throws LogException {
        if (employeeId == null) {
            throw new LogException("You are not logged in!");
        }
        employeeId = null;
    }
}
