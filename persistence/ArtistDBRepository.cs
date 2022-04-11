
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
    public class ArtistDBRepository : ArtistRepository
    {

        private static readonly ILog log = LogManager.GetLogger("ArtistDBRepository");

        IDictionary<string, string> props;

        public ArtistDBRepository(IDictionary<string, string> props)
        {
            log.Info("Creating ArtistDBRepository ");
            this.props = props;
        }

        public void delete(long id)
        {
            IDbConnection con = DBUtils.getConnection(props);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "delete from Artist where id=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);
                var dataR = comm.ExecuteNonQuery();
                //if (dataR == 0)
                  //  throw new RepositoryException("No task deleted!");
            }
        }
    

        public List<Artist> findAll()
        {
            IDbConnection con = DBUtils.getConnection(props);
            List<Artist> artists = new List<Artist>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from Artist";

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long idA = dataR.GetInt32(0);
                        string first_name = dataR.GetString(1);
                        string last_name = dataR.GetString(2);
                        int age = dataR.GetInt32(3);
                        string origin_country = dataR.GetString(4);
                        Artist artist = new Artist(idA, first_name, last_name, age, origin_country);
                       
                        artists.Add(artist);
                    }
                }
            }

            return artists;
        }

        public Artist findOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);
            IDbConnection con = DBUtils.getConnection(props);

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from Artist where id=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idA = dataR.GetInt32(0);
                        string first_name = dataR.GetString(1);
                        string last_name = dataR.GetString(2);
                        int age = dataR.GetInt32(3);
                        string origin_country = dataR.GetString(4);
                        Artist artist = new Artist(idA, first_name, last_name, age, origin_country);

                        log.InfoFormat("Exiting findOne with value {0}", artist);
                        return artist;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);
            return null;
        }

        public void save(Artist entity)
        {
            var con = DBUtils.getConnection(props);

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "insert into Artist(first_name, last_name, age, origin_country) values (@first_name, @last_name, @age, @origin_country)";
                var paramfName = comm.CreateParameter();
                paramfName.ParameterName = "@first_name";
                paramfName.Value = entity.firstName;
                comm.Parameters.Add(paramfName);

                var paramlName = comm.CreateParameter();
                paramlName.ParameterName = "@last_name";
                paramlName.Value = entity.lastName;
                comm.Parameters.Add(paramlName);

                var paramAge = comm.CreateParameter();
                paramAge.ParameterName = "@age";
                paramAge.Value = entity.age;
                comm.Parameters.Add(paramAge);

                var paramOrigin = comm.CreateParameter();
                paramOrigin.ParameterName = "@origin_country";
                paramOrigin.Value = entity.originCountry;
                comm.Parameters.Add(paramOrigin);

                var result = comm.ExecuteNonQuery();
                //if (result == 0)
                  //  throw new RepositoryException("No task added !");
            }
        }

        public void update(long id, Artist entity)
        {
            throw new NotImplementedException();
        }
    }
}
