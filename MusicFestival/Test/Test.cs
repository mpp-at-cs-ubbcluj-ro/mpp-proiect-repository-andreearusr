
using MPP_MusicFestival.Domain;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;

namespace MPP_MusicFestival.Testing
{
    class Test
    {
        public void test_entity()
        {
            Entity<int> e = new Entity<int>(3);
            Debug.Assert(e.Id == 3);
            Debug.Assert(e.ToString() == "3");
            e.Id = 4;
            Debug.Assert(e.Id == 4);
            Debug.Assert(e.ToString() == "4");
        }

        public void test_artist()
        {
            Artist artist = new Artist(1, "Alejandro", "Speitzer", 25, "Mexic");
            Debug.Assert(artist.Id == 1);
            Debug.Assert(artist.firstName == "Alejandro");
            Debug.Assert(artist.lastName == "Speitzer");
            Debug.Assert(artist.age == 25);
            Debug.Assert(artist.originCountry == "Mexic");
            Debug.Assert(artist.ToString() == "Artist- Nume:Speitzer Prenume:Alejandro Varsta:25 Tara origine:Mexic");



            artist.Id = 5;
            artist.lastName = "Rauw";
            artist.age = 29;
            artist.originCountry = "Puerto Rico";

            Debug.Assert(artist.Id == 5);
            Debug.Assert(artist.firstName == "Alejandro");
            Debug.Assert(artist.lastName == "Rauw");
            Debug.Assert(artist.age == 29);
            Debug.Assert(artist.originCountry == "Puerto Rico");
            Debug.Assert(artist.ToString() == "Artist- Nume:Rauw Prenume:Alejandro Varsta:29 Tara origine:Puerto Rico");
        }

        public void test_office_employee()
        {
            OfficeEmployee officeEmployee = new OfficeEmployee(7, "Raluca", "Rus", 6041778234556, "raluca_33", "Pisica1234");
            Debug.Assert(officeEmployee.Id == 7);
            Debug.Assert(officeEmployee.firstName == "Raluca");
            Debug.Assert(officeEmployee.lastName == "Rus");
            Debug.Assert(officeEmployee.CNP == 6041778234556);
            Debug.Assert(officeEmployee.username == "raluca_33");
            Debug.Assert(officeEmployee.password == "Pisica1234");
            Debug.Assert(officeEmployee.ToString() == "Cod angajat:7 Nume:Rus Prenume:Raluca CNP:6041778234556");
      
            officeEmployee.Id = 123;
            officeEmployee.lastName = "Sava";
            officeEmployee.username = "raluca_31";
            officeEmployee.password = "PisicaMea1234";

            Debug.Assert(officeEmployee.Id == 123);
            Debug.Assert(officeEmployee.firstName == "Raluca");
            Debug.Assert(officeEmployee.lastName == "Sava");
            Debug.Assert(officeEmployee.CNP == 6041778234556);
            Debug.Assert(officeEmployee.username == "raluca_31");
            Debug.Assert(officeEmployee.password == "PisicaMea1234");
            Debug.Assert(officeEmployee.ToString() == "Cod angajat:123 Nume:Sava Prenume:Raluca CNP:6041778234556");
        }

        public void test_show()
        {
            Artist artist = new Artist(1, "Anuel", "Santiago", 29, "Puerto Rico");
            DateTime dateTime = DateTime.Parse("05/29/2022 20:30");

            Show show = new Show(456, "LatinoParty", "Spectacol cu artisti latino din toate regiunile, nu ratati!",
                artist, dateTime, "Ibitza", 20000, 1500);
            Debug.Assert(show.Id == 456);
            Debug.Assert(show.showName == "LatinoParty");
            Debug.Assert(show.description == "Spectacol cu artisti latino din toate regiunile, nu ratati!");
            Debug.Assert(show.artist.Id == 1);
            Debug.Assert(show.dateTime == DateTime.Parse("05/29/2022 20:30"));
            Debug.Assert(show.showLocation == "Ibitza");
            Debug.Assert(show.seatsAvailable == 20000);
            Debug.Assert(show.seatsSold == 1500);
            Debug.Assert(show.ToString() == "Spectacol- Nume:LatinoParty Descriere:Spectacol cu artisti latino din toate regiunile, nu ratati!" +
                "\nnume Artist:Anuel" +
                "\nData si ora:5/29/2022 8:30:00 PM Locatia:Ibitza");


            show.dateTime = DateTime.Parse("07/15/2022 21:00");


            Debug.Assert(show.Id == 456);
            Debug.Assert(show.showName == "LatinoParty");
            Debug.Assert(show.description == "Spectacol cu artisti latino din toate regiunile, nu ratati!");
            Debug.Assert(show.artist.Id == 1);
            Debug.Assert(show.dateTime == DateTime.Parse("07/15/2022 21:00"));
            Debug.Assert(show.showLocation == "Ibitza");
            Debug.Assert(show.seatsAvailable == 20000);
            Debug.Assert(show.seatsSold == 1500);
            Debug.Assert(show.ToString() == "Spectacol- Nume:LatinoParty Descriere:Spectacol cu artisti latino din toate regiunile, nu ratati!" +
                "\nnume Artist:Anuel" +
                "\nData si ora:7/15/2022 9:00:00 PM Locatia:Ibitza");
        }


        public void test_ticket()
        {

            Artist artist = new Artist(1, "Anuel", "Santiago", 29, "Puerto Rico");
            DateTime dateTime = DateTime.Parse("05/29/2022 20:30");

            Show show = new Show(43, "LatinoParty", "Spectacol cu artisti latino din toate regiunile, nu ratati!",
                artist, dateTime, "Ibitza", 20000, 1500);
            OfficeEmployee officeEmployee = new OfficeEmployee(78, "Raluca", "Rus", 6041778234556,"raluca_33" ,"Pisica1234");
            Ticket ticket = new Ticket(67, 43, 78, "Andreea Rus");

            Debug.Assert(ticket.Id == 67);
            Debug.Assert(ticket.showId == 43);
            Debug.Assert(ticket.employeeId == 78);
            Debug.Assert(ticket.buyerName == "Andreea Rus");
            Debug.Assert(ticket.ToString() == "Bilet- Cod:67 Nume cumparator:Andreea Rus" +
                "\nSpectacolId:43" +
                "\nAngajatId:78");


            Show show1 = new Show(56, "VivaReggaeton", "Reggaeton partyy",
                artist, DateTime.Parse("08/11/2019 17:00"), "Bucuresti", 3000, 150);
            OfficeEmployee officeEmployee1 = new OfficeEmployee(99, "Andrei", "Rusu", 6111799234553, "rusua", "masina");

            ticket.Id = 123;
            ticket.showId = 56;
            ticket.employeeId = 99;
            ticket.buyerName = "Maria Runcan";

            Debug.Assert(ticket.Id == 123);
            Debug.Assert(ticket.showId == 56);
            Debug.Assert(ticket.employeeId == 99);
            Debug.Assert(ticket.buyerName == "Maria Runcan");
            Debug.Assert(ticket.ToString() == "Bilet- Cod:123 Nume cumparator:Maria Runcan" +
                "\nSpectacolId:56" +
                "\nAngajatId:99");
        }


        public void test()
        {
            test_entity();
            test_artist();
            test_office_employee();
            test_show();
            test_ticket();
        }
    }
}
