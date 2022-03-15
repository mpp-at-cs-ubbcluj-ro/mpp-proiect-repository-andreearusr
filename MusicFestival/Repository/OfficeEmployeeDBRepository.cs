using MPP_MusicFestival.Domain;
using MPP_MusicFestival.Repository;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using log4net;

namespace MusicFestival.Repository
{
    class OfficeEmployeeDBRepository : OfficeEmployeeRepository
    {

        private static readonly ILog log = LogManager.GetLogger("OfficeEmployeeDBRepository");

        IDictionary<String, string> props;

        public OfficeEmployeeDBRepository(IDictionary<String, string> props)
        {
            log.Info("Creating OfficeEmployeeDBRepository ");
            this.props = props;
        }

        public void delete(long id)
        {
            IDbConnection con = DBUtils.getConnection(props);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "delete from OfficeEmployee where id=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);
                var dataR = comm.ExecuteNonQuery();
                //if (dataR == 0)
                //  throw new RepositoryException("No task deleted!");
            }
        }

        public IEnumerable<OfficeEmployee> findAll()
        {
            IDbConnection con = DBUtils.getConnection(props);
            IList<OfficeEmployee> officeEmployees = new List<OfficeEmployee>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from OfficeEmployee";

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        String first_name = dataR.GetString(1);
                        String last_name = dataR.GetString(2);
                        long CNP = dataR.GetInt32(3);
                        String username = dataR.GetString(4);
                        String password = dataR.GetString(5);
                        OfficeEmployee officeEmployee = new OfficeEmployee(id, first_name, last_name, CNP, username, password);

                        officeEmployees.Add(officeEmployee);
                    }
                }
            }

            return officeEmployees;
        }

        public OfficeEmployee findOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);
            IDbConnection con = DBUtils.getConnection(props);

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from OfficeEmployee where id=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idO = dataR.GetInt32(0);
                        String first_name = dataR.GetString(1);
                        String last_name = dataR.GetString(2);
                        long CNP = dataR.GetInt32(3);
                        String username = dataR.GetString(4);
                        String password = dataR.GetString(5);
                        OfficeEmployee officeEmployee = new OfficeEmployee(idO, first_name, last_name, CNP, username, password);

                        log.InfoFormat("Exiting findOne with value {0}", officeEmployee);
                        return officeEmployee;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);
            return null;
        }

        public long getId(string username)
        {
            IDbConnection con = DBUtils.getConnection(props);

            long N = 0;
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from OfficeEmployee where username=@username";
                var paramUsername = comm.CreateParameter();
                paramUsername.ParameterName = "@username";
                paramUsername.Value = username;
                comm.Parameters.Add(paramUsername);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idO = dataR.GetInt32(0);
                        
                        log.InfoFormat("Exiting getId with value {0}", idO);
                        return idO;
                    }
                }
            }
            return N;
        }

        public string getPassword(long id)
        {
            IDbConnection con = DBUtils.getConnection(props);

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from OfficeEmployee where id=@id";
                var paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        string password = dataR.GetString(5);

                        log.InfoFormat("Exiting getPassword with value {0}", password);
                        return password;
                    }
                }
            }
            return null;
        }
        public long getId(long CNP)
        {
            IDbConnection con = DBUtils.getConnection(props);

            long N = 0;
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from OfficeEmployee where CNP=@CNP";
                var paramCNP = comm.CreateParameter();
                paramCNP.ParameterName = "@CNP";
                paramCNP.Value = CNP;
                comm.Parameters.Add(paramCNP);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idO = dataR.GetInt32(0);

                        log.InfoFormat("Exiting getId with value {0}", idO);
                        return idO;
                    }
                }
            }
            return N;
        }

        public void save(OfficeEmployee entity)
        {
            var con = DBUtils.getConnection(props);

            if (getId(entity.CNP) != 0)
                log.InfoFormat("This entity already exists");
            else
            {

                using (var comm = con.CreateCommand())
                {
                    comm.CommandText = "insert into OfficeEmployee(first_name, last_name, CNP, username, password) values (@first_name, @last_name, @CNP, @username, @password)";
                    var paramfName = comm.CreateParameter();
                    paramfName.ParameterName = "@first_name";
                    paramfName.Value = entity.firstName;
                    comm.Parameters.Add(paramfName);

                    var paramlName = comm.CreateParameter();
                    paramlName.ParameterName = "@last_name";
                    paramlName.Value = entity.lastName;
                    comm.Parameters.Add(paramlName);

                    var paramCNP = comm.CreateParameter();
                    paramCNP.ParameterName = "@CNP";
                    paramCNP.Value = entity.CNP;
                    comm.Parameters.Add(paramCNP);

                    var paramUsername = comm.CreateParameter();
                    paramUsername.ParameterName = "@username";
                    paramUsername.Value = entity.username;
                    comm.Parameters.Add(paramUsername);

                    var paramPassw = comm.CreateParameter();
                    paramPassw.ParameterName = "@password";
                    paramPassw.Value = entity.password;
                    comm.Parameters.Add(paramPassw);

                    var result = comm.ExecuteNonQuery();
                    //if (result == 0)
                    //  throw new RepositoryException("No task added !");
                }
            }
        }

        public void update(long id, OfficeEmployee entity)
        {
            throw new NotImplementedException();
        }
    }
}
