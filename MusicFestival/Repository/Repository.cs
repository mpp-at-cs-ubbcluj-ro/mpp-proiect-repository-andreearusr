using MPP_MusicFestival.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Repository
{
    interface Repository<ID, E> where E : Entity<ID>
    {
        E findOne(ID id);
        IEnumerable<E> findAll();
        E save(E entity);
        E delete(ID id);
        E update(ID id, E entity);
    }
}
