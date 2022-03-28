using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Domain
{
    public class OfficeEmployee : Entity<long>
    {
        public string firstName { get; set; }
        public string lastName { get; set; }
        public long CNP { get; set; }
        public string username { get; set; }
        public string password { get; set; }
        


        public OfficeEmployee(long id, string firstName, string lastName, long CNP, string username, string password) : base(id)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.CNP = CNP;
            this.username = username;
            this.password = password;
        }

        public OfficeEmployee(string firstName, string lastName, long CNP, string username, string password) : base(0)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.CNP = CNP;
            this.username = username;
            this.password = password;
        }

        public override string ToString()
        {
            return "Cod angajat:" + base.ToString() + " Nume:" + lastName + 
                " Prenume:" + firstName + " CNP:" + CNP;
        }
    }
}
