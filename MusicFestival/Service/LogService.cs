using MPP_MusicFestival.Repository;
using MusicFestival.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.Service
{
    public class LogService
    {
        private OfficeEmployeeRepository officeEmployeeRepository;

        public LogService(OfficeEmployeeRepository officeEmployeeRepository)
        {
            this.officeEmployeeRepository = officeEmployeeRepository;
        }

        public long employeeId = 0;

        public long getEmployeeId()
        {
            return employeeId;
        }

        public long logIn(string username, string password)
        {

        if (employeeId != 0)
                throw new LogException("Already logged in!");
    
        long id = officeEmployeeRepository.getId(username);

        if (id==0)
                throw new LogException("Wrong username or password!");

        if (officeEmployeeRepository.getPassword(id) == password)
                employeeId = id;
        else
                throw new LogException("Wrong username or password!");
        

        return id;
    }

    public void logOut()
    {
        if (employeeId == 0)
                throw new LogException("You are not logged in!");
        
        employeeId = 0;
    }

    }
}
