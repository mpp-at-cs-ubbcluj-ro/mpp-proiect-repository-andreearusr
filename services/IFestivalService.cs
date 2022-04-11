using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.services
{
    public interface IFestivalService
    {
        List<Show> getShows();

        List<Show> getArtistsByDate(DateTime date);

        void buyTicket(long showId, long officeEmployeeId, string buyerName);

        void updateShow(long showId, Show newShow);

        Show findShowById(long showId);

        OfficeEmployee findEmployeeByUsername(string username);


        void LogIn(OfficeEmployee officeEmployee, IFestivalObserver client);

        void LogOut(OfficeEmployee officeEmployee, IFestivalObserver client);
    }
}
