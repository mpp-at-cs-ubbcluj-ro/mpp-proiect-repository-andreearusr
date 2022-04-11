using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.networking
{
	public interface Response
	{
	}

	[Serializable]
	public class OkResponse : Response
	{

	}

	[Serializable]
	public class ErrorResponse : Response
	{
		private string message;

		public ErrorResponse(string message)
		{
			this.message = message;
		}

		public virtual string Message
		{
			get
			{
				return message;
			}
		}
	}

	[Serializable]
	public class GetShowsResponse : Response
	{
		private List<Show> shows;

		public GetShowsResponse(List<Show> shows)
		{
			this.shows = shows;
		}

		public virtual List<Show> Shows
		{
			get
			{
				return shows;
			}
		}
	}

	[Serializable]
	public class GetArtistsByDateResponse : Response
	{
		private List<Show> shows;

		public GetArtistsByDateResponse(List<Show> shows)
		{
			this.shows = shows;
		}

		public virtual List<Show> Shows
		{
			get
			{
				return shows;
			}
		}
	}

	[Serializable]
	public class FindShowByIdResponse : Response
	{
		private Show show;

		public FindShowByIdResponse(Show show)
		{
			this.show = show;
		}

		public virtual Show Showw
		{
			get
			{
				return show;
			}
		}
	}

	[Serializable]
	public class FindEmployeeByUsernameResponse : Response
	{
		private OfficeEmployee officeEmployee;

		public FindEmployeeByUsernameResponse(OfficeEmployee officeEmployee)
		{
			this.officeEmployee = officeEmployee;
		}

		public virtual OfficeEmployee OfficeEmployee
		{
			get
			{
				return officeEmployee;
			}
		}
	}

	public interface UpdateResponse : Response
	{
	}

	[Serializable]
	public class UpdatedShowResponse : UpdateResponse
	{
		private Show show;

		public UpdatedShowResponse(Show show)
		{
			this.show = show;
		}

		public virtual Show Showw
		{
			get
			{
				return show;
			}
		}
	}


}
