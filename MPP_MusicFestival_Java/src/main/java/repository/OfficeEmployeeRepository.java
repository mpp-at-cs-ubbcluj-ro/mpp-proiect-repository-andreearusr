package repository;

public interface OfficeEmployeeRepository extends Repository<Long, domain.OfficeEmployee> {
    Long getId(String username);

    String getPassword(Long id);
}
