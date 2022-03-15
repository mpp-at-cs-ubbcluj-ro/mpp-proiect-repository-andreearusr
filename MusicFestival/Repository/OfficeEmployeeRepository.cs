﻿using MPP_MusicFestival.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Repository
{
    interface OfficeEmployeeRepository : Repository<long, OfficeEmployee>
    {
        long getId(string username);

        string getPassword(long id);

        long getId(long CNP);
    }
}
