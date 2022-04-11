using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.networking
{

	public interface Request
	{
	}

	[Serializable]
	public class LoginRequest : Request
	{
		private OfficeEmployee officeEmployee;

		public LoginRequest(OfficeEmployee officeEmployee)
		{
			this.officeEmployee = officeEmployee;
		}

		public virtual OfficeEmployee User
		{
			get
			{
				return officeEmployee;
			}
		}
	}

	[Serializable]
	public class LogoutRequest : Request
	{
		private OfficeEmployee officeEmployee;

		public LogoutRequest(OfficeEmployee officeEmployee)
		{
			this.officeEmployee = officeEmployee;
		}

		public virtual OfficeEmployee User
		{
			get
			{
				return officeEmployee;
			}
		}
	}

	[Serializable]
	public class GetShowsRequest : Request
	{
		public GetShowsRequest() { }

	}

	[Serializable]
	public class GetArtistsByDateRequest : Request
	{
		private DateTime date;

		public GetArtistsByDateRequest(DateTime date)
		{
			this.date = date;
		}

		public virtual DateTime Date
		{
			get
			{
				return date;
			}
		}

	}

	[Serializable]
	public class BuyTicketRequest : Request
	{
		private long showId;
		private long officeEmployeeId;
		private string buyerName;

		public BuyTicketRequest(long showId, long officeEmployeeId, string buyerName)
		{
			this.showId = showId;
			this.officeEmployeeId = officeEmployeeId;
			this.buyerName = buyerName;
		}

		public virtual long ShowId
		{
			get
			{
				return showId;
			}
		}

		public virtual long OfficeEmployeeId
		{
			get
			{
				return officeEmployeeId;
			}
		}
		public virtual string BuyerName
		{
			get
			{
				return buyerName;
			}
		}

	}

	[Serializable]
	public class UpdateShowRequest : Request
	{
		private long showId;
		private Show newShow;

		public UpdateShowRequest(long showId, Show newShow)
		{
			this.showId = showId;
			this.newShow = newShow;
		}

		public virtual long ShowId
		{
			get
			{
				return showId;
			}
		}

		public virtual Show NewShow
		{
			get
			{
				return newShow;
			}
		}

	}

	[Serializable]
	public class FindShowByIdRequest : Request
	{
		private long showId;

		public FindShowByIdRequest(long showId)
		{
			this.showId = showId;
		}

		public virtual long ShowId
		{
			get
			{
				return showId;
			}
		}

	}

	[Serializable]
	public class FindEmployeeByUsernameRequest : Request
	{
		private string username;

		public FindEmployeeByUsernameRequest(string username)
		{
			this.username = username;
		}

		public virtual string Username
		{
			get
			{
				return username;
			}
		}

	}
}
