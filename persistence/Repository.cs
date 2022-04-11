
using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Text;

namespace MusicFestival.persistence
{
    public interface Repository<ID, E> where E : Entity<ID>
    {
        E findOne(ID id);
        List<E> findAll();
        void save(E entity);
        void delete(ID id);
        void update(ID id, E entity);
    }
}
