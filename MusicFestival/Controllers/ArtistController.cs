using MPP_MusicFestival.Domain;
using MusicFestival.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.Controllers
{
    public class ArtistController
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

        public List<Show> getArtistsByDate(DateTime date)
        {
            return service.getArtistsByDate(date);
        }

        public List<Show> getAll()
        {
            return service.getShows();
        }


        public Show findShowById(long showId)
        {
            return service.findShowById(showId);
        }

        public void updateShow(long showId, Show newShow)
        {
            service.updateShow(showId, newShow);
        }

        public void buyTicket(long showId, string buyerN)
        {
            service.buyTicket(showId, logService.employeeId, buyerN);
        }

    }
}
