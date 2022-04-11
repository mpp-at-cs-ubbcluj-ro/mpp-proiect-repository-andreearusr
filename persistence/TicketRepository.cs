
using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Text;

namespace MusicFestival.persistence
{
    public interface TicketRepository: Repository<long, Ticket>
    { 
    }
}
