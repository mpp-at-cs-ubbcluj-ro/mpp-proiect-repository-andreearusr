using MPP_MusicFestival.Domain;
using MusicFestival.Controllers;
using MusicFestival.Exceptions;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MusicFestival
{
    public partial class Employee : Form
    {
        private EmployeeController employeeController;
        private ArtistForm artistForm;
        private Login loginForm;
        public Employee(EmployeeController employeeController)
        {
            InitializeComponent();
            this.employeeController = employeeController;
        }

        internal void setArtistForm(ArtistForm artistForm)
        {
            this.artistForm = artistForm;
        }

        internal void setLoginForm(Login loginForm)
        {
            this.loginForm = loginForm;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                employeeController.tryLogout();
                loginForm.Show();
                this.Hide();
            }
            catch (LogException ex)
            {
                MessageBox.Show(this, ex.Message);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            try
            {
                artistForm.Show();
                this.Hide();
            }
            catch (Exception ex)
            {
                MessageBox.Show(this, ex.Message);
            }
        }

       private void Employee_Load(object sender, EventArgs e)
        {
            try
            {
                DataTable table = ConvertListToDataTableRezultat(employeeController.getShows());
                tableShows.DataSource = table;
            }
            catch(Exception ex)
            {
                MessageBox.Show(this, ex.Message);
            }
        }

        public static DataTable ConvertListToDataTableRezultat(List<Show> list)
        {
            DataTable table = new DataTable();

            table.Columns.Add("Nume Spectacol");
            table.Columns.Add("Nume Artist");
            table.Columns.Add("Locatie");
            table.Columns.Add("Bilete disponibile");
            table.Columns.Add("Bilete vandute");
            table.Columns.Add("Data");


            // Add rows.
            foreach (Show rez in list)
            {
                table.Rows.Add(rez.showName, rez.artist.firstName + " " + rez.artist.lastName, rez.showLocation, rez.seatsAvailable - rez.seatsSold, rez.seatsSold, rez.dateTime.ToString().Substring(0, 9));
            }

            return table;
        }

        private void tableShows_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            foreach (DataGridViewRow row in tableShows.Rows)
            {
                int seatsAvailable = Convert.ToInt32(row.Cells[3].Value);

                if (seatsAvailable == 0)
                {
                    row.DefaultCellStyle.BackColor = Color.Red;
                }
            }
        }
    }
}
