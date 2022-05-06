package festival.repository;

import festival.domain.OfficeEmployee;

public interface OfficeEmployeeRepository extends Repository<Long, OfficeEmployee> {
    Long getId(String username);

    String getPassword(Long id);

    Long getId(Long CNP);
}
