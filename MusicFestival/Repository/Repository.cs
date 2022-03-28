using MPP_MusicFestival.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Repository
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
