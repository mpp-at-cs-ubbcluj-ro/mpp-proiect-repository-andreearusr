using MPP_MusicFestival.Domain;
using MusicFestival.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.Controllers
{
    public class EmployeeController
    {
        private Services service;
        private LogService logService;

        public void setService(Services service)
        {
            this.service = service;
        }

        public void setLogin(LogService logService)
        {
            this.logService = logService;
        }

        public void tryLogout()
        {
            logService.logOut();
        }

        public List<Show> getShows()
        {
            return service.getShows();
        }
        
    }
}
