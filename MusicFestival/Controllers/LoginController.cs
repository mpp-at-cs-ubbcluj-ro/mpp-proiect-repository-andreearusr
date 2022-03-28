using MusicFestival.Exceptions;
using MusicFestival.Service;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MusicFestival.Controllers
{
    public class LoginController
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


        public void tryLogin(string username, string password)
        {
            logService.logIn(username, password);
        }
    }
}
