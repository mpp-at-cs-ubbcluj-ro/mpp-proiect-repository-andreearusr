using MusicFestival.model;
using MusicFestival.networking;
using MusicFestival.services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace MusicFestival.networking
{
    public class FestivalServerProxy : IFestivalService
    {
		private string host;
		private int port;

		private IFestivalObserver client;

		private NetworkStream stream;

		private IFormatter formatter;
		private TcpClient connection;

		private Queue<Response> responses;
		private volatile bool finished;
		private EventWaitHandle _waitHandle;
		public FestivalServerProxy(string host, int port)
		{
			this.host = host;
			this.port = port;
			responses = new Queue<Response>();
		}

		private void closeConnection()
		{
			finished = true;
			try
			{
				stream.Close();

				connection.Close();
				_waitHandle.Close();
				client = null;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}

		}

		private void sendRequest(Request request)
		{
			try
			{
				formatter.Serialize(stream, request);
				stream.Flush();
			}
			catch (Exception e)
			{
				throw new LogException("Error sending object " + e);
			}

		}

		private Response readResponse()
		{
			Response response = null;
			try
			{
				_waitHandle.WaitOne();
				lock (responses)
				{
					//Monitor.Wait(responses); 
					response = responses.Dequeue();

				}


			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
			return response;
		}
		private void initializeConnection()
		{
			try
			{
				connection = new TcpClient(host, port);
				stream = connection.GetStream();
				formatter = new BinaryFormatter();
				finished = false;
				_waitHandle = new AutoResetEvent(false);
				startReader();
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
		}
		private void startReader()
		{
			Thread tw = new Thread(run);
			tw.Start();
		}

		private void handleUpdate(UpdateResponse update)
		{
			if (update is UpdatedShowResponse)
			{

				UpdatedShowResponse updatedShowRsp = (UpdatedShowResponse)update;
				Show show = updatedShowRsp.Showw;
				Console.WriteLine("Show updated " + show);
				try
				{
					client.showUpdated(show);
				}
				catch (LogException e)
				{
					Console.WriteLine(e.StackTrace);
				}
			}
		}

		public void buyTicket(Ticket ticket)
        {
			sendRequest(new BuyTicketRequest(ticket.showId, ticket.employeeId, ticket.buyerName));
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new LogException(err.Message);
			}
			
		}

        public OfficeEmployee findEmployeeByUsername(string username)
        {
			initializeConnection();
			sendRequest(new FindEmployeeByUsernameRequest(username));
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new LogException(err.Message);
			}
			FindEmployeeByUsernameResponse resp = (FindEmployeeByUsernameResponse)response;
			OfficeEmployee officeEmployee = resp.OfficeEmployee;
			return officeEmployee;
		}

        public Show findShowById(long showId)
        {
			sendRequest(new FindShowByIdRequest(showId));
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new LogException(err.Message);
			}
			FindShowByIdResponse resp = (FindShowByIdResponse)response;
			Show show = resp.Showw;
			return show;
		}

        public List<Show> getArtistsByDate(DateTime date)
        {
			sendRequest(new GetArtistsByDateRequest(date));
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new LogException(err.Message);
			}
			GetArtistsByDateResponse resp = (GetArtistsByDateResponse)response;
			List<Show> shows = resp.Shows;
			return shows;
		}

        public List<Show> getShows()
        {
			sendRequest(new GetShowsRequest());
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new LogException(err.Message);
			}
			GetShowsResponse resp = (GetShowsResponse)response;
			List<Show> shows = resp.Shows;
			return shows;
		}

        public void LogIn(OfficeEmployee officeEmployee, IFestivalObserver client)
        {
			initializeConnection();
			
			sendRequest(new LoginRequest(officeEmployee));
			Response response = readResponse();
			if (response is OkResponse)
			{
				this.client = client;
				return;
			}
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				closeConnection();
				throw new LogException(err.Message);
			}
		}

        public void LogOut(OfficeEmployee officeEmployee, IFestivalObserver client)
        {
			sendRequest(new LogoutRequest(officeEmployee));
			Response response = readResponse();
			closeConnection();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new LogException(err.Message);
			}
		}

        public void updateShow(long showId, Show newShow)
        {
			sendRequest(new UpdateShowRequest(showId, newShow));
			Response response = readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err = (ErrorResponse)response;
				throw new LogException(err.Message);
			}
		}

	public virtual void run()
	{
		while (!finished)
		{
			try
			{
				object response = formatter.Deserialize(stream);
				Console.WriteLine("response received " + response);
				if (response is UpdateResponse)
				{
					handleUpdate((UpdateResponse)response);
				}
				else
				{

					lock (responses)
					{


						responses.Enqueue((Response)response);

					}
					_waitHandle.Set();
				}
			}
			catch (Exception e)
			{
				Console.WriteLine("Reading error " + e);
			}

		}
	}
	
}
}
