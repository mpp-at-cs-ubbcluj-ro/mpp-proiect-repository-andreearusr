
using MusicFestival.model;
using MusicFestival.services;
using System;
using System.Collections.Generic;

namespace MusicFestival.client
{
    public class FestivalClientCtrl : IFestivalObserver
    {
        public event EventHandler<FestivalEventArgs> updateEvent; //ctrl calls it when it has received an update
        private readonly IFestivalService server;
        private OfficeEmployee currentEmployee;

        public FestivalClientCtrl(IFestivalService server)
        {
            this.server = server;
            currentEmployee = null;
        }


        public void tryLogin(string username, string password)
        {
            OfficeEmployee officeEmployee = server.findEmployeeByUsername(username);
            if (officeEmployee.Equals(null))
            {
                throw new Exception("Login failed");
            }
            else if (officeEmployee.password.Equals(password))
            {
                server.LogIn(officeEmployee, this);
                currentEmployee = officeEmployee;
            }
            else
                throw new Exception("Login failed");
        }

        public void tryLogout()
        {
            server.LogOut(currentEmployee, this);
            currentEmployee = null;
        }

        public List<Show> getShows()
        {
            List<Show> shows = new List<Show>();
            shows = server.getShows();
            return shows;
        }


        public List<Show> getArtistsByDate(DateTime date)
        {
            List<Show> shows = new List<Show>();
            shows = server.getArtistsByDate(date);
            return shows;
        }

        public Show findShowById(long showId)
        {
            Show show = server.findShowById(showId);
            return show;
        }

        public void buyTicket(ulong showId, string buyerName)
        {
            Ticket ticket = new Ticket(showId, currentEmployee.Id, buyerName);
            server.buyTicket(ticket);
        }

        public void updateShow(long showId, Show newShow)
        {
            server.updateShow(showId, newShow);
        }


        protected virtual void OnFestivalEvent(FestivalEventArgs e)
        {
            if (updateEvent == null) return;
            updateEvent(this, e);
            Console.WriteLine("Update Event called");
        }

        public void showUpdated(Show show)
        {
            FestivalEventArgs festivalArgs = new FestivalEventArgs(FestivalEvent.ShowUpdated, show);
            Console.WriteLine("Show updated");
            OnFestivalEvent(festivalArgs);
        }
    }


}
