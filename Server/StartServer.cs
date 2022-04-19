using MusicFestival.networking;
using MusicFestival.persistence;
using MusicFestival.services;
using protobuf3;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Net.Sockets;
using System.Threading;

namespace MusicFestival.Server
{

    class StartServer
    {
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

        static void Main(string[] args)
        {

            IDictionary<string, string> props = new SortedList<string, string>();
            props.Add("ConnectionString", GetConnectionStringByName("festivalDB"));

            ArtistRepository artistDBRepository = new ArtistDBRepository(props);
            
            OfficeEmployeeRepository officeEmployeeDBRepository = new OfficeEmployeeDBRepository(props);
            ShowRepository showDBRepository = new ShowDBRepository(props, artistDBRepository);
            TicketRepository ticketDBRepository = new TicketDBRepository(props, showDBRepository, officeEmployeeDBRepository);
            IFestivalService festivalServiceImpl = new ServiceImpl(artistDBRepository, officeEmployeeDBRepository, showDBRepository, ticketDBRepository);

            /* List<Show> artists = festivalServiceImpl.getShows();
             foreach (Show a in artists)
             {
                 Console.WriteLine(a);
             }*/

            ProtoV3FestivalServer scs = new ProtoV3FestivalServer("127.0.0.1", 55556, festivalServiceImpl);
            scs.Start();
            Console.WriteLine("Server started ...");
            //Console.WriteLine("Press <enter> to exit...");
            
            Console.ReadLine();

        }
    }

    public class SerialFestivalServer : ConcurrentServer
    {
        private IFestivalService server;
        private FestivalClientWorker worker;
        public SerialFestivalServer(string host, int port, IFestivalService server) : base(host, port)
        {
            this.server = server;
            Console.WriteLine("SerialFestivalServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new FestivalClientWorker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }

}

public class ProtoV3FestivalServer : ConcurrentServer
{
    private IFestivalService server;
    private ProtoFestivalWorker worker;
    public ProtoV3FestivalServer(string host, int port, IFestivalService server)
        : base(host, port)
    {
        this.server = server;
        Console.WriteLine("ProtoFestivalServer...");
    }
    protected override Thread createWorker(TcpClient client)
    {
        worker = new ProtoFestivalWorker(server, client);
        return new Thread(new ThreadStart(worker.run));
    }
}