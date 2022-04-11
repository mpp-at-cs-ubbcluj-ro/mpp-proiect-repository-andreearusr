﻿

using System;

namespace MusicFestival.model
{
    [Serializable]
    public class Ticket : Entity<long>
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

        public Ticket(long showId, long employeeId, string buyerName) : base(0)
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