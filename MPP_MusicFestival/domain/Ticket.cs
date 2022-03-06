using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Domain
{
    class Ticket : Entity<long>
    {

        public long showId { get; set; }
        public long employeeId { get; set; }
        public string buyerName { get; set; }

        public Ticket(long id, long showId, long employeeId, string buyerName) : base(id)
        {
            this.showId = showId;
            this.employeeId = employeeId;
            this.buyerName = buyerName;
        }

        public override string ToString()
        {
            return "Bilet- Cod:" + base.ToString() + " Nume cumparator:" + buyerName +
                "\nSpectacolId:" + showId + 
                "\nAngajatId:" + employeeId;
        }
    }
}
