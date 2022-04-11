using log4net;
using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.persistence
{
    public class ShowDBRepository : ShowRepository
    {

        private static readonly ILog log = LogManager.GetLogger("ShowDBRepository");

        IDictionary<string, string> props;

        private ArtistRepository artistRepository;

        public ShowDBRepository(IDictionary<string, string> props, ArtistRepository artistRepository)
        {
            log.Info("Creating ShowDBRepository ");
            this.props = props;
            this.artistRepository = artistRepository;
        }

        public void delete(long id)
        {
            IDbConnection con = DBUtils.getConnection(props);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "delete from Show where id=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);
                var dataR = comm.ExecuteNonQuery();
                //if (dataR == 0)
                //  throw new RepositoryException("No task deleted!");
            }
        }

        public List<Show> findAll()
        {
            IDbConnection con = DBUtils.getConnection(props);
            List<Show> shows = new List<Show>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from Show";

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        string show_name = dataR.GetString(1);
                        string description = dataR.GetString(2);
                        DateTime date_time = DateTime.Parse(dataR.GetString(3));
                        string show_location = dataR.GetString(4);
                        int seats_available = dataR.GetInt32(5);
                        int seats_sold = dataR.GetInt32(6);
                        long artistId = dataR.GetInt32(7);

                        Artist artist = artistRepository.findOne(artistId);
                        Show show = new Show(id, show_name, description, artist, date_time, show_location, seats_available, seats_sold);
                        shows.Add(show);
                    }
                }
            }

            return shows;
        }

        public List<Show> getArtistsByDate(DateTime date)
        {
            IDbConnection con = DBUtils.getConnection(props);
            List<Show> shows = new List<Show>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from Show where substr(Show.date_time, 1, 9)=@date order by Show.artist_id";

                var paramDate = comm.CreateParameter();
                paramDate.ParameterName = "@date";
                paramDate.Value = date.ToString().Substring(0,9);
                comm.Parameters.Add(paramDate);

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        string show_name = dataR.GetString(1);
                        string description = dataR.GetString(2);
                        DateTime date_time = DateTime.Parse(dataR.GetString(3));
                        string show_location = dataR.GetString(4);
                        int seats_available = dataR.GetInt32(5);
                        int seats_sold = dataR.GetInt32(6);
                        long artistId = dataR.GetInt32(7);

                        Artist artist = artistRepository.findOne(artistId);
                        Show show = new Show(id, show_name, description, artist, date_time, show_location, seats_available, seats_sold);
                        shows.Add(show);
                    }
                }
            }

            return shows;
        }

        public Show findOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);
            IDbConnection con = DBUtils.getConnection(props);

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from Show where id=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idS = dataR.GetInt32(0);
                        string show_name = dataR.GetString(1);
                        string description = dataR.GetString(2);
                        DateTime date_time = DateTime.Parse(dataR.GetString(3));
                        string show_location = dataR.GetString(4);
                        int seats_available = dataR.GetInt32(5);
                        int seats_sold = dataR.GetInt32(6);
                        long artistId = dataR.GetInt32(7);

                        Artist artist = artistRepository.findOne(artistId);
                        Show show = new Show(idS, show_name, description, artist, date_time, show_location, seats_available, seats_sold);

                        log.InfoFormat("Exiting findOne with value {0}", show);
                        return show;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);
            return null;
        }

        public void save(Show entity)
        {
            var con = DBUtils.getConnection(props);

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "insert into Show(show_name, description, date_time, show_location, seats_available, seats_sold, artist_id) values (@show_name, @descr, @date_time, @show_location, @seats_available, @seats_sold, @artist_id)";
                var paramSName = comm.CreateParameter();
                paramSName.ParameterName = "@show_name";
                paramSName.Value = entity.showName;
                comm.Parameters.Add(paramSName);

                var paramDesc = comm.CreateParameter();
                paramDesc.ParameterName = "@descr";
                paramDesc.Value = entity.description;
                comm.Parameters.Add(paramDesc);

                var paramDate = comm.CreateParameter();
                paramDate.ParameterName = "@date_time";
                paramDate.Value = entity.dateTime;
                comm.Parameters.Add(paramDate);

                var paramLoc = comm.CreateParameter();
                paramLoc.ParameterName = "@show_location";
                paramLoc.Value = entity.showLocation;
                comm.Parameters.Add(paramLoc);

                var paramAvail = comm.CreateParameter();
                paramAvail.ParameterName = "@seats_available";
                paramAvail.Value = entity.seatsAvailable;
                comm.Parameters.Add(paramAvail);

                var paramSold = comm.CreateParameter();
                paramSold.ParameterName = "@seats_sold";
                paramSold.Value = entity.seatsSold;
                comm.Parameters.Add(paramSold);

                var paramIdA = comm.CreateParameter();
                paramIdA.ParameterName = "@artist_id";
                paramIdA.Value = entity.artist.Id;
                comm.Parameters.Add(paramIdA);

                var result = comm.ExecuteNonQuery();
                //if (result == 0)
                //  throw new RepositoryException("No task added !");
            }
        }

        public void update(long id, Show entity)
        {
            IDbConnection con = DBUtils.getConnection(props);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "update Show set seats_sold=@seats_sold where id=@id";

                var paramSold = comm.CreateParameter();
                paramSold.ParameterName = "@seats_sold";
                paramSold.Value = entity.seatsSold;
                comm.Parameters.Add(paramSold);

                var paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);


                var dataR = comm.ExecuteNonQuery();
                //if (dataR == 0)
                //  throw new RepositoryException("No task deleted!");
            }
        }
    }
}
