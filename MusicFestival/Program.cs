using MPP_MusicFestival.Testing;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using log4net;
using log4net.Config;
using MusicFestival.Repository;
using MPP_MusicFestival.Domain;
using System.Configuration;
using System.Data.SqlClient;
using System.Data.SQLite;
using MPP_MusicFestival.Repository;
using MusicFestival.Service;
using System.Windows.Forms;
using MusicFestival.Controllers;

namespace MusicFestival
{
    class Program
    {
		//private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

		public static void Main(string[] args)
        {
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);

			XmlConfigurator.Configure(new System.IO.FileInfo("args[0]"));
			Console.WriteLine("Configuration Settings for festivalDB {0}", GetConnectionStringByName("festivalDB"));
			IDictionary<String, string> props = new SortedList<String, String>();
			props.Add("ConnectionString", GetConnectionStringByName("festivalDB"));



			ArtistDBRepository artistDBRepository = new ArtistDBRepository(props);
			OfficeEmployeeDBRepository officeEmployeeDBRepository = new OfficeEmployeeDBRepository(props);
			ShowDBRepository showDBRepository = new ShowDBRepository(props, artistDBRepository);
			TicketDBRepository ticketDBRepository = new TicketDBRepository(props, showDBRepository, officeEmployeeDBRepository);

            LogService logService = new LogService(officeEmployeeDBRepository);
			Services service = new Services(artistDBRepository, officeEmployeeDBRepository, showDBRepository, ticketDBRepository);

			LoginController loginController = new LoginController();
			loginController.setLogin(logService);
			loginController.setService(service);
			Login loginForm = new Login(loginController);

			EmployeeController employeeController = new EmployeeController();
			employeeController.setLogin(logService);
			employeeController.setService(service);
			Employee employeeForm = new Employee(employeeController);

			ArtistController artistController = new ArtistController();
			artistController.setLogin(logService);
			artistController.setService(service);
			ArtistForm artistForm = new ArtistForm(artistController);

			loginForm.setEmployeeForm(employeeForm);
			employeeForm.setArtistForm(artistForm);
			employeeForm.setLoginForm(loginForm);
			artistForm.setEmployeeForm(employeeForm);

			Application.Run(loginForm);






			/* Test t = new Test();
			 t.test();
			 Console.WriteLine("Hello World!");

			 //configurare jurnalizare folosind log4net
			 XmlConfigurator.Configure(new System.IO.FileInfo("args[0]"));
			 Console.WriteLine("Configuration Settings for festivalDB {0}", GetConnectionStringByName("festivalDB"));
			 IDictionary<String, string> props = new SortedList<String, String>();
			 props.Add("ConnectionString", GetConnectionStringByName("festivalDB"));

			 ArtistDBRepository repoArtist = new ArtistDBRepository(props);
			 Artist artist = repoArtist.findOne(4);
			 Console.WriteLine(artist);
			 //repoArtist.save(new Artist("Andra","Maruta",35,"Romania"));
			 foreach (Artist a in repoArtist.findAll())
			 {
				 Console.WriteLine(a);
			 }
			 repoArtist.delete(6);
			 foreach (Artist a in repoArtist.findAll())
			 {
				 Console.WriteLine(a);
			 }


			 OfficeEmployeeDBRepository repoOffice = new OfficeEmployeeDBRepository(props);
			 OfficeEmployee officeEmployee = repoOffice.findOne(4);
			 Console.WriteLine(officeEmployee);
			 repoOffice.save(new OfficeEmployee("Raul","Marina",6120398551641,"raluca_98","Pisica15"));
			 Console.WriteLine(repoOffice.getPassword(6));
			 Console.WriteLine(repoOffice.getId("raluca_98"));
			 foreach (OfficeEmployee o in repoOffice.findAll())
			 {
				 Console.WriteLine(o);
			 }
			 repoOffice.delete(4);
			 foreach (OfficeEmployee o in repoOffice.findAll())
			 {
				 Console.WriteLine(o);
			 }


			 ShowDBRepository repoShow = new ShowDBRepository(props, repoArtist);
			 Show show = repoShow.findOne(6);
			 Console.WriteLine(show);
			 //repoShow.save(new Show("Retro", "We love retro", repoArtist.findOne(7), DateTime.Parse("4/12/2022 23:00"), "After Eight, Cluj-Napoca", 200, 170));
			 foreach (Show s in repoShow.findAll())
			 {
				 Console.WriteLine(s);
			 }
			 repoShow.delete(4);
			 foreach (Show s in repoShow.findAll())
			 {
				 Console.WriteLine(s);
			 }
			 foreach (Show s in repoShow.getArtistsByDate(DateTime.Parse("4/12/2022 23:00")))
			 {
				 Console.WriteLine(s);
			 }


			 TicketDBRepository repoTicket = new TicketDBRepository(props, repoShow, repoOffice);
			 Ticket ticket = repoTicket.findOne(5);
			 //repoTicket.save(new Ticket(7, 6, "Rauta Alexandra"));
			 foreach (Ticket tick in repoTicket.findAll())
			 {
				 Console.WriteLine(tick);
			 }
			 repoTicket.delete(2);
			 foreach (Ticket tick in repoTicket.findAll())
			 {
				 Console.WriteLine(tick);
			 }*/


		}

		static string GetConnectionStringByName(string name)
		{
			// Assume failure.
			string returnValue = null;

			// Look for the name in the connectionStrings section.
			ConnectionStringSettings settings = ConfigurationManager.ConnectionStrings[name];

			// If found, return the connection string.
			if (settings != null)
				returnValue = settings.ConnectionString;

			return returnValue;
		}
	}
}
