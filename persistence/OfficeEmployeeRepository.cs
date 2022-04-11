
using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Text;

namespace MusicFestival.persistence
{
    public interface OfficeEmployeeRepository : Repository<long, OfficeEmployee>
    {
        long getId(string username);

        string getPassword(long id);

        long getId(long CNP);
    }
}
