using MPP_MusicFestival.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Repository
{
    interface TicketRepository: Repository<long, Ticket>
    { 
    }
}
