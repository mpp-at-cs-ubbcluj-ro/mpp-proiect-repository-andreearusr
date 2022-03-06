package repository;

import domain.Entity;

import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {

    E save(E entity);

    E delete(ID id);

    E update(ID id, E entity);

    E findOne(ID id);

    List<E> findAll();
}
