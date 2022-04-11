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
    public class TicketDBRepository : TicketRepository
    {

        private static readonly ILog log = LogManager.GetLogger("ShowDBRepository");

        IDictionary<string, string> props;

        private ShowRepository showRepository;
        private OfficeEmployeeRepository officeEmployeeRepository;

        public TicketDBRepository(IDictionary<string, string> props, ShowRepository showRepository, OfficeEmployeeRepository officeEmployeeRepository)
        {
            log.Info("Creating TicketDBRepository ");
            this.props = props;
            this.showRepository = showRepository;
            this.officeEmployeeRepository = officeEmployeeRepository;
        }

        public void delete(long id)
        {
            IDbConnection con = DBUtils.getConnection(props);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "delete from Ticket where id=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);
                var dataR = comm.ExecuteNonQuery();
                //if (dataR == 0)
                //  throw new RepositoryException("No task deleted!");
            }
        }

        public List<Ticket> findAll()
        {
            IDbConnection con = DBUtils.getConnection(props);
            List<Ticket> tickets = new List<Ticket>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from Ticket";

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        long show_id = dataR.GetInt32(1);
                        long employee_id = dataR.GetInt32(2);
                        string buyer_name = dataR.GetString(3);

                        Ticket ticket = new Ticket(id, show_id, employee_id, buyer_name);
                        tickets.Add(ticket);
                    }
                }
            }

            return tickets;
        }

        public Ticket findOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);
            IDbConnection con = DBUtils.getConnection(props);

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select * from Ticket where id=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idT = dataR.GetInt32(0);
                        long show_id = dataR.GetInt32(1);
                        long employee_id = dataR.GetInt32(2);
                        string buyer_name = dataR.GetString(3);

                        Ticket ticket = new Ticket(idT, show_id, employee_id, buyer_name);

                        log.InfoFormat("Exiting findOne with value {0}", ticket);
                        return ticket;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);
            return null;
        }

        public void save(Ticket entity)
        {
            var con = DBUtils.getConnection(props);

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "insert into Ticket(show_id, employee_id, buyer_name) values (@show_id, @employee_id, @buyer_name)";
                var paramSId = comm.CreateParameter();
                paramSId.ParameterName = "@show_id";
                paramSId.Value = entity.showId;
                comm.Parameters.Add(paramSId);

                var paramEId = comm.CreateParameter();
                paramEId.ParameterName = "@employee_id";
                paramEId.Value = entity.employeeId;
                comm.Parameters.Add(paramEId);

                var paramBName = comm.CreateParameter();
                paramBName.ParameterName = "@buyer_name";
                paramBName.Value = entity.buyerName;
                comm.Parameters.Add(paramBName);


                var result = comm.ExecuteNonQuery();
                //if (result == 0)
                //  throw new RepositoryException("No task added !");
            }
        }

        public void update(long id, Ticket entity)
        {
            throw new NotImplementedException();
        }
    }
}
