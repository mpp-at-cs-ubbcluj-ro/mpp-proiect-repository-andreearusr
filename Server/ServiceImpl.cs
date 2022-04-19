
using MusicFestival.model;
using MusicFestival.persistence;
using MusicFestival.services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.Server
{
    public class ServiceImpl : IFestivalService
    {
        private ArtistRepository artistRepository;
        private OfficeEmployeeRepository officeEmployeeRepository;
        private ShowRepository showRepository;
        private TicketRepository ticketRepository;
        private readonly IDictionary<long, IFestivalObserver> loggedEmployees;




        public ServiceImpl(ArtistRepository artistRepository, OfficeEmployeeRepository officeEmployeeRepository, ShowRepository showRepository, TicketRepository ticketRepository)
        {
            this.artistRepository = artistRepository;
            this.officeEmployeeRepository = officeEmployeeRepository;
            this.showRepository = showRepository;
            this.ticketRepository = ticketRepository;
            loggedEmployees = new Dictionary<long, IFestivalObserver>();
        }

        public List<Show> getShows()
        {
            return showRepository.findAll();
        }

        public List<Show> getArtistsByDate(DateTime date)
        {
            return showRepository.getArtistsByDate(date);
        }

        public void buyTicket(Ticket ticket)
        {
            ticketRepository.save(ticket);
        }

        public void updateShow(long showId, Show newShow)
        {
            showRepository.update(showId, newShow);

            foreach (KeyValuePair<long, IFestivalObserver> entry in loggedEmployees)
            {
                Task.Run(() => entry.Value.showUpdated(newShow));
            }

        }

        public Show findShowById(long showId)
        {
            return showRepository.findOne(showId);
        }

        public OfficeEmployee findEmployeeByUsername(string username)
        {
            long id = officeEmployeeRepository.getId(username);
            return officeEmployeeRepository.findOne(id);
        }

        public void LogIn(OfficeEmployee officeEmployee, IFestivalObserver client)
        {

            long employeeId = officeEmployeeRepository.getId(officeEmployee.username);
            if (employeeId != null)
            {
                if (loggedEmployees.ContainsKey(employeeId))
                    throw new LogException("OfficeEmployee already logged in.");
                loggedEmployees[employeeId] = client;
            }
            else
                throw new LogException("Authentication failed.");
        }

        public void LogOut(OfficeEmployee officeEmployee, IFestivalObserver client)
        {
            IFestivalObserver localClient = loggedEmployees[officeEmployee.Id];
            if (localClient == null)
                throw new LogException("OfficeEmployee " + officeEmployee.Id + "is not logged in.");

            loggedEmployees.Remove(officeEmployee.Id);

        }
}
}
