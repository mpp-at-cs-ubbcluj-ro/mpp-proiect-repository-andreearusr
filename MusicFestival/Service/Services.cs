using MPP_MusicFestival.Domain;
using MPP_MusicFestival.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.Service
{
    public class Services
    {
        private ArtistRepository artistRepository;
        private OfficeEmployeeRepository officeEmployeeRepository;
        private ShowRepository showRepository;
        private TicketRepository ticketRepository;

        public Services(ArtistRepository artistRepository, OfficeEmployeeRepository officeEmployeeRepository, ShowRepository showRepository, TicketRepository ticketRepository)
        {
            this.artistRepository = artistRepository;
            this.officeEmployeeRepository = officeEmployeeRepository;
            this.showRepository = showRepository;
            this.ticketRepository = ticketRepository;
        }

        public List<Show> getShows()
        {
            return showRepository.findAll();
        }

        public List<Show> getArtistsByDate(DateTime date)
        {
            return showRepository.getArtistsByDate(date);
        }

        public void buyTicket(long showId, long officeEmployeeId, String buyerName)
        {
            Ticket ticket = new Ticket(showId, officeEmployeeId, buyerName);
            ticketRepository.save(ticket);
        }

        public void updateShow(long showId, Show newShow)
        {
            showRepository.update(showId, newShow);
        }

        public Show findShowById(long showId)
        {
            return showRepository.findOne(showId);
        }

    }
}
