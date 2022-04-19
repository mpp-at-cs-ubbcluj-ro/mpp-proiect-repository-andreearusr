
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
    public class FestivalClientWorker : IFestivalObserver
    {

		private IFestivalService server;
		private TcpClient connection;

		private NetworkStream stream;
		private IFormatter formatter;
		private volatile bool connected;
		public FestivalClientWorker(IFestivalService server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{

				stream = connection.GetStream();
				formatter = new BinaryFormatter();
				connected = true;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
		}

		public virtual void run()
		{
			while (connected)
			{
				try
				{
					object request = formatter.Deserialize(stream);
					object response = handleRequest((Request)request);
					if (response != null)
					{
						sendResponse((Response)response);
					}
				}
				catch (Exception e)
				{
					Console.WriteLine(e.StackTrace);
				}

				try
				{
					Thread.Sleep(1000);
				}
				catch (Exception e)
				{
					Console.WriteLine(e.StackTrace);
				}
			}
			try
			{
				stream.Close();
				connection.Close();
			}
			catch (Exception e)
			{
				Console.WriteLine("Error " + e);
			}
		}

		public void showUpdated(Show show)
		{
			Console.WriteLine("Show updated  " + show);
			try
			{
				sendResponse(new UpdatedShowResponse(show));
			}
			catch (Exception e)
			{
				throw new LogException("Sending error: " + e);
			}

			
		}

		private Response handleRequest(Request request)
		{
			Response response = null;
			if (request is LoginRequest)
			{
				Console.WriteLine("Login request ...");
				LoginRequest logReq = (LoginRequest)request;
				OfficeEmployee officeEmployee = logReq.User;
				
				try
				{
					lock (server)
					{
						server.LogIn(officeEmployee, this);
					}
					return new OkResponse();
				}
				catch (LogException e)
				{
					connected = false;
					return new ErrorResponse(e.Message);
				}
			}

			if (request is LogoutRequest)
			{
				Console.WriteLine("Logout request");
				LogoutRequest logReq = (LogoutRequest)request;
				OfficeEmployee officeEmployee = logReq.User;

				try
				{
					lock (server)
					{

						server.LogOut(officeEmployee, this);
					}
					connected = false;
					return new OkResponse();

				}
				catch (LogException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is GetShowsRequest)
			{
				Console.WriteLine("GetShows Request ...");
				GetShowsRequest getReq = (GetShowsRequest)request;
				
				try
				{
					List<Show> shows;
					lock (server)
					{
						shows = server.getShows();
					}
					
					return new GetShowsResponse(shows);
				}
				catch (LogException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is GetArtistsByDateRequest)
			{
				Console.WriteLine("GetArtistsByDate Request ...");
				GetArtistsByDateRequest getReq = (GetArtistsByDateRequest)request;
				DateTime date = getReq.Date;

				try
				{
					List<Show> shows;
					lock (server)
					{
						shows = server.getArtistsByDate(date);
					}

					return new GetArtistsByDateResponse(shows);
				}
				catch (LogException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is BuyTicketRequest)
			{
				Console.WriteLine("BuyTicket Request ...");
				BuyTicketRequest getReq = (BuyTicketRequest)request;

				long showId = getReq.ShowId;
				long officeEmployeeId = getReq.OfficeEmployeeId;
				string buyerName = getReq.BuyerName;

				Ticket ticket = new Ticket(showId, officeEmployeeId, buyerName);
				try
				{
					lock (server)
					{
						server.buyTicket(ticket);
					}

					return new OkResponse();
				}
				catch (LogException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is UpdateShowRequest)
			{
				Console.WriteLine("BuyTicket Request ...");
				UpdateShowRequest getReq = (UpdateShowRequest)request;

				long showId = getReq.ShowId;
				Show newShow = getReq.NewShow;

				try
				{
					lock (server)
					{
						server.updateShow(showId, newShow);
					}
					return new OkResponse();
				}
				catch (LogException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is FindShowByIdRequest)
			{
				Console.WriteLine("FindShowById Request ...");
				FindShowByIdRequest getReq = (FindShowByIdRequest)request;
				long showId = getReq.ShowId;

				try
				{
					Show show;
					lock (server)
					{
						show = server.findShowById(showId);
					}

					return new FindShowByIdResponse(show);
				}
				catch (LogException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is FindEmployeeByUsernameRequest)
			{
				Console.WriteLine("FindEmployeeByUsername Request ...");
				FindEmployeeByUsernameRequest getReq = (FindEmployeeByUsernameRequest)request;
				string username = getReq.Username;

				try
				{
					OfficeEmployee officeEmployee;
					lock (server)
					{
						officeEmployee = server.findEmployeeByUsername(username);
					}

					return new FindEmployeeByUsernameResponse(officeEmployee);
				}
				catch (LogException e)
				{
					return new ErrorResponse(e.Message);
				}
			}


			return response;
		}

			private void sendResponse(Response response)
		{
			Console.WriteLine("sending response " + response);
			lock (stream)
			{
				formatter.Serialize(stream, response);
				stream.Flush();
			}

		}


	}
}
