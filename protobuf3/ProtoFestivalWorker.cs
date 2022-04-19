using MusicFestival.model;
using MusicFestival.services;
using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Threading;

using Chat.Protocol;
using Google.Protobuf;


namespace protobuf3
{
	public class ProtoFestivalWorker : IFestivalObserver
	{

		private IFestivalService server;
		private TcpClient connection;

		private NetworkStream stream;
		private volatile bool connected;
		public ProtoFestivalWorker(IFestivalService server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{

				stream = connection.GetStream();
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
					FestivalRequest request = FestivalRequest.Parser.ParseDelimitedFrom(stream);
					FestivalResponse response = handleRequest(request);
					if (response != null)
					{
						sendResponse(response);
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

		public void showUpdated(MusicFestival.model.Show show)
		{
			Console.WriteLine("Show updated  " + show);
			try
			{
				sendResponse(ProtoUtils.createUpdatedShowResponse(show));
			}
			catch (Exception e)
			{
				throw new LogException("Sending error: " + e);
			}


		}

		private FestivalResponse handleRequest(FestivalRequest request)
		{
			FestivalResponse response = null;
			FestivalRequest.Types.Type reqType = request.Type;
			switch (reqType)
			{
				case FestivalRequest.Types.Type.Login:
					{
						Console.WriteLine("Login request ...");
						MusicFestival.model.OfficeEmployee officeEmployee = ProtoUtils.getOfficeE(request);

						try
						{
							lock (server)
							{
								server.LogIn(officeEmployee, this);
							}
							return ProtoUtils.createOkResponse();
						}
						catch (LogException e)
						{
							connected = false;
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case FestivalRequest.Types.Type.Logout:
                    {
						Console.WriteLine("Logout request ...");
						MusicFestival.model.OfficeEmployee officeEmployee = ProtoUtils.getOfficeE(request);

						try
						{
							lock (server)
							{
								server.LogOut(officeEmployee, this);
							}
							return ProtoUtils.createOkResponse();
						}
						catch (LogException e)
						{
							connected = false;
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}

				case FestivalRequest.Types.Type.GetShows:
                    {
						Console.WriteLine("GetShows Request ...");
						
						try
						{
							List<MusicFestival.model.Show> shows;
							lock (server)
							{
								shows = server.getShows();
							}

							return ProtoUtils.createShowsResponse(shows);
						}
						catch (LogException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}

				case FestivalRequest.Types.Type.GetArtistsByDate:
                    {
						Console.WriteLine("GetArtistsByDate Request ...");
						DateTime date = ProtoUtils.getDate(request);
						Console.WriteLine("data e:" + date);
						try
						{
							List<MusicFestival.model.Show> shows;
							lock (server)
							{
								shows = server.getArtistsByDate(date);
							}

							return ProtoUtils.createGetArtistsByDateResponse(shows);
						}
						catch (LogException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case FestivalRequest.Types.Type.BuyTicket:
                    {
						Console.WriteLine("BuyTicket Request ...");
						MusicFestival.model.Ticket ticket = ProtoUtils.getTicket(request);

						try
						{
							lock (server)
							{
								server.buyTicket(ticket);
							}

							return ProtoUtils.createOkResponse();
						}
						catch (LogException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}

				case FestivalRequest.Types.Type.UpdateShow:
					{ 
						Console.WriteLine("BuyTicket Request ...");
						long showId = ProtoUtils.getShowUpdateId(request);
						MusicFestival.model.Show newShow = ProtoUtils.getShow(request);

						try
						{
							lock (server)
							{
								server.updateShow(showId, newShow);
							}
							return ProtoUtils.createOkResponse();
						}
						catch (LogException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}

				case FestivalRequest.Types.Type.FindShowById:
                    {
						Console.WriteLine("FindShowById Request ...");
						long showId = ProtoUtils.getShowId(request);

						try
						{
							MusicFestival.model.Show show;
							lock (server)
							{
								show = server.findShowById(showId);
							}

							return ProtoUtils.createFindShowByIdResponse(show);
						}
						catch (LogException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
				case FestivalRequest.Types.Type.FindEmployeeByUsername:
                    {

						Console.WriteLine("FindEmployeeByUsername Request ...");
						string username = ProtoUtils.getUsername(request);

						try
						{
							MusicFestival.model.OfficeEmployee officeEmployee;
							lock (server)
							{
								officeEmployee = server.findEmployeeByUsername(username);
							}

							return ProtoUtils.createFindEmployeeByUsernameResponse(officeEmployee);
						}
						catch (LogException e)
						{
							return ProtoUtils.createErrorResponse(e.Message);
						}
					}
			}


			return response;
		}

		private void sendResponse(FestivalResponse response)
		{
			Console.WriteLine("sending response " + response);
			lock (stream)
			{
				response.WriteDelimitedTo(stream);
				stream.Flush();
			}

		}


	}
}
