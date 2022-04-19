using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Chat.Protocol;
using proto = Chat.Protocol;

namespace protobuf3
{
    static class ProtoUtils
    {
        /*public static FestivalRequest createLoginRequest(MusicFestival.model.OfficeEmployee officeEmployee)
        {
            proto.OfficeEmployee officeEmployee1 = new proto.OfficeEmployee
            {
                Id = officeEmployee.Id,
                CNP = officeEmployee.CNP,
                FirstName = officeEmployee.firstName,
                LastName = officeEmployee.lastName,
                Username = officeEmployee.username,
                Password = officeEmployee.password
            };

            FestivalRequest request = new FestivalRequest { Type = FestivalRequest.Types.Type.Login , OfficeEmployee=officeEmployee1};
            return request;
        }

        public static FestivalRequest createLogoutRequest(MusicFestival.model.OfficeEmployee officeEmployee)
        {
            proto.OfficeEmployee officeEmployee1 = new proto.OfficeEmployee
            {
                Id = officeEmployee.Id,
                CNP = officeEmployee.CNP,
                FirstName = officeEmployee.firstName,
                LastName = officeEmployee.lastName,
                Username = officeEmployee.username,
                Password = officeEmployee.password
            };

            FestivalRequest request = new FestivalRequest { Type = FestivalRequest.Types.Type.Logout, OfficeEmployee = officeEmployee1 };
            return request;
        }*/


        public static FestivalResponse createOkResponse()
        {
            FestivalResponse response = new FestivalResponse { Type = FestivalResponse.Types.Type.Ok };
            return response;
        }


        public static FestivalResponse createErrorResponse(String text)
        {
            FestivalResponse response = new FestivalResponse
            {
                Type = FestivalResponse.Types.Type.Error,
                Error = text
            };
            return response;
        }

        public static FestivalResponse createFindShowByIdResponse(MusicFestival.model.Show show)
        {
            proto.Artist artist = new proto.Artist
            {
                Id = Convert.ToUInt64(show.artist.Id),
                FirstName = show.artist.firstName,
                LastName = show.artist.lastName,
                Age = Convert.ToUInt32(show.artist.age),
                OriginCountry = show.artist.originCountry

            };


            proto.Show show1 = new proto.Show
            {
                Id = Convert.ToUInt64(show.Id),
                ShowName = show.showName,
                Description = show.description,
                Artist = artist,
                DateTime = show.dateTime.ToString("yyyy-MM-dd HH:mm:ss"),
                ShowLocation = show.showLocation,
                SeatsSold = Convert.ToUInt32(show.seatsSold),
                SeatsTotal = Convert.ToUInt32(show.seatsAvailable)

            };

            FestivalResponse response = new FestivalResponse
            {
                Type = FestivalResponse.Types.Type.FindShowById , Show=show1
            };

            
            return response;
        }

        public static FestivalResponse createFindEmployeeByUsernameResponse(MusicFestival.model.OfficeEmployee officeEmployee)
        {
            proto.OfficeEmployee officeEmployee1 = new proto.OfficeEmployee 
            {
                Id = Convert.ToUInt64(officeEmployee.Id),
                CNP = Convert.ToUInt64(officeEmployee.CNP),
                FirstName = officeEmployee.firstName,
                LastName = officeEmployee.lastName,
                Username = officeEmployee.username,
                Password = officeEmployee.password
            };
           

            FestivalResponse response = new FestivalResponse
            {
                Type = FestivalResponse.Types.Type.FindEmployeeByUsername, OfficeEmployee=officeEmployee1
            };

            return response;
        }


        public static FestivalResponse createShowsResponse(List<MusicFestival.model.Show> shows)
        {
            FestivalResponse response = new FestivalResponse
            {
                Type = FestivalResponse.Types.Type.Shows
            };

            foreach (MusicFestival.model.Show show in shows)
            {
                proto.Artist artist = new proto.Artist
                {
                    Id = Convert.ToUInt64(show.artist.Id),
                    FirstName = show.artist.firstName,
                    LastName = show.artist.lastName,
                    Age = Convert.ToUInt32(show.artist.age),
                    OriginCountry = show.artist.originCountry

                };
                proto.Show show1 = new proto.Show
                {
                    Id = Convert.ToUInt64(show.Id),
                    ShowName = show.showName,
                    Description = show.description,
                    Artist = artist,
                    DateTime = show.dateTime.ToString("yyyy-MM-dd HH:mm:ss"),
                    ShowLocation = show.showLocation,
                    SeatsSold = Convert.ToUInt32(show.seatsSold),
                    SeatsTotal = Convert.ToUInt32(show.seatsAvailable)
                };
                response.Shows.Add(show1);
            }


           

            return response;
        }

        public static FestivalResponse createGetArtistsByDateResponse(List<MusicFestival.model.Show> shows)
        {
            FestivalResponse response = new FestivalResponse
            {
                Type = FestivalResponse.Types.Type.GetArtistsByDate
            };

            foreach (MusicFestival.model.Show show in shows)
            {
                    proto.Artist artist = new proto.Artist
                    {
                        Id = Convert.ToUInt64(show.artist.Id),
                        FirstName = show.artist.firstName,
                        LastName = show.artist.lastName,
                        Age = Convert.ToUInt32(show.artist.age),
                        OriginCountry = show.artist.originCountry

                    };
                    proto.Show show1 = new proto.Show
                    {
                        Id = Convert.ToUInt64(show.Id),
                        ShowName = show.showName,
                        Description = show.description,
                        Artist = artist,
                        DateTime = show.dateTime.ToString("yyyy-MM-dd HH:mm:ss"),
                        ShowLocation = show.showLocation,
                        SeatsSold = Convert.ToUInt32(show.seatsSold),
                        SeatsTotal = Convert.ToUInt32(show.seatsAvailable)
                    };
                    response.Shows.Add(show1);
                }
            
            return response;
        }


        public static FestivalResponse createUpdatedShowResponse(MusicFestival.model.Show show)
        {
            proto.Artist artist = new proto.Artist
            {
                Id = Convert.ToUInt64(show.artist.Id),
                FirstName = show.artist.firstName,
                LastName = show.artist.lastName,
                Age = Convert.ToUInt32(show.artist.age),
                OriginCountry = show.artist.originCountry

            };


        proto.Show show1 = new proto.Show
            {
                Id = Convert.ToUInt64(show.Id),
                ShowName = show.showName,
                Description = show.description,
                Artist = artist,
                DateTime = show.dateTime.ToString("yyyy-MM-dd HH:mm:ss"),
                ShowLocation = show.showLocation,
                SeatsSold = Convert.ToUInt32(show.seatsSold),
                SeatsTotal = Convert.ToUInt32(show.seatsAvailable)

            };


            FestivalResponse response = new FestivalResponse
            {
                Type = FestivalResponse.Types.Type.ShowUpdated, Show=show1
            };

            return response;
        }


        public static string getError(FestivalResponse response)
        {
            string errorMessage = response.Error;
            return errorMessage;
        }

        public static MusicFestival.model.OfficeEmployee getOfficeE(FestivalRequest request)
        {
            MusicFestival.model.OfficeEmployee officeEmployee = new MusicFestival.model.OfficeEmployee
            (
                Convert.ToInt64(request.OfficeEmployee.Id),
                request.OfficeEmployee.FirstName,
                request.OfficeEmployee.LastName,
                 Convert.ToInt64(request.OfficeEmployee.CNP),
                request.OfficeEmployee.Username,
                request.OfficeEmployee.Password
            );

            return officeEmployee;
        }

        public static DateTime getDate(FestivalRequest request)
        {
            DateTime date = Convert.ToDateTime(request.Data.Data);
            return date;
        }

        public static MusicFestival.model.Ticket getTicket(FestivalRequest request)
        {
            MusicFestival.model.Ticket ticket = new MusicFestival.model.Ticket
                (
                Convert.ToInt64(request.Ticket.Id),
                Convert.ToInt64(request.Ticket.EmployeeId),
                request.Ticket.BuyerName
                );

            return ticket;
        }

        public static long getShowId(FestivalRequest request)
        {
            long showId = Convert.ToInt64(request.Id.Id);
            return showId;
        }

        public static long getShowUpdateId(FestivalRequest request)
        {
            long showId = Convert.ToInt64(request.Show.Id);
            return showId;
        }


        public static MusicFestival.model.Show getShow(FestivalRequest request)
        {
            MusicFestival.model.Artist artist = new MusicFestival.model.Artist
                (
                    Convert.ToInt64(request.Show.Artist.Id),
                    request.Show.Artist.FirstName,
                    request.Show.Artist.LastName,
                     Convert.ToInt32(request.Show.Artist.Age),
                    request.Show.Artist.OriginCountry
                );

            MusicFestival.model.Show show = new MusicFestival.model.Show
            (
                Convert.ToInt64(request.Show.Id),
                request.Show.ShowName,
                request.Show.Description,
                artist,
                Convert.ToDateTime(request.Show.DateTime),
                request.Show.ShowLocation,
                Convert.ToInt32(request.Show.SeatsTotal),
                Convert.ToInt32(request.Show.SeatsSold)
            );

            return show;
        }

        public static string getUsername(FestivalRequest request)
        {

            string username = request.Data.Data;
            return username;
        }
    }
}
