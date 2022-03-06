using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Domain
{
    class Show: Entity<long>
    {

        public string showName { get; set; }
        public string description { get; set; }
        public Artist artist { get; set; }
        public DateTime dateTime { get; set; }
        public string showLocation { get; set; }
        public int seatsAvailable { get; set; }
        public int seatsSold { get; set; }

        public Show(long id, string showName, string description, Artist artist, DateTime dateTime, string showLocation, int seatsAvailable, int seatsSold) : base(id)
        {
            this.showName = showName;
            this.description = description;
            this.artist = artist;
            this.dateTime = dateTime;
            this.showLocation = showLocation;
            this.seatsAvailable = seatsAvailable;
            this.seatsSold = seatsSold;
        }

        public override string ToString()
        {
            return "Spectacol- Nume:" + showName + " Descriere:" + description +
                "\nnume Artist:" + artist.firstName + "\nData si ora:" + dateTime.ToString() +
                " Locatia:" + showLocation;
        }
    }
}
