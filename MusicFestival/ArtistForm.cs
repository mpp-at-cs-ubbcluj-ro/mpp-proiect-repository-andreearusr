using MPP_MusicFestival.Domain;
using MusicFestival.Controllers;
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
    public partial class ArtistForm : Form
    {
        private ArtistController artistController;
        private Employee employeeForm;
        long showId = 0;
        DateTime data;
        public ArtistForm(ArtistController artistController)
        {
            InitializeComponent();
            this.artistController = artistController;
        }

        internal void setEmployeeForm(Employee employeeForm)
        {
            this.employeeForm = employeeForm;
        }

        private void refresh_table()
        {
            DateTime date = dateTimePicker1.Value;

            DataTable table = ConvertListToDataTableRezultat(artistController.getArtistsByDate(date));
            artistsTable.DataSource = table;
            artistsTable.Columns[4].Visible = false;
            artistsTable.Columns[5].Visible = false;

        }

        private void clear_fields()
        {
            textBoxBuyerName.Text = "";
            comboBoxBilets.Items.Clear();
            comboBoxBilets.Text = "";
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                artistsTable.Columns.Clear();
                clear_fields();
                employeeForm.Show();
                this.Hide();
            }
            catch(Exception ex)
            {
                MessageBox.Show(this, ex.Message);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            try
            {
                DateTime date = dateTimePicker1.Value;

                DataTable table = ConvertListToDataTableRezultat(artistController.getArtistsByDate(date));
                artistsTable.DataSource = table;
                artistsTable.Columns[4].Visible = false;
                artistsTable.Columns[5].Visible = false;
            }
            catch(Exception ex)
            {
                MessageBox.Show(this, ex.Message);
            }
        }

       public static DataTable ConvertListToDataTableRezultat(List<Show> list)
        {
            DataTable table = new DataTable();

            //int columns = 4;

            table.Columns.Add("Nume Artist");
            table.Columns.Add("Locatie");
            table.Columns.Add("Ora inceperii");
            table.Columns.Add("Bilete disponibile");
            table.Columns.Add("Cod Show");
            table.Columns.Add("Data");

            // Add rows.
            foreach (Show rez in list)
            { 
                table.Rows.Add(rez.artist.firstName + " " + rez.artist.lastName, rez.showLocation, rez.dateTime.ToString().Substring(10,8), rez.seatsAvailable - rez.seatsSold, rez.Id, rez.dateTime.ToString().Substring(0, 9));
            }

            return table;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            try
            {
                if (data < DateTime.Now)
                {
                    MessageBox.Show(this, "You can't buy any tickets at this Show!");
                }
                else
                {
                    string buyerName = textBoxBuyerName.Text;
                    int nrTickets = Convert.ToInt32(comboBoxBilets.SelectedItem);

                    if (buyerName.Equals(""))
                    {
                        MessageBox.Show(this, "Buyer name is empty");
                        return;
                    }
                    if (nrTickets.Equals(0))
                    {
                        MessageBox.Show(this, "Tickets number is null");
                        return;
                    }
                    Show newShow = artistController.findShowById(showId);
                    if (newShow.seatsAvailable - newShow.seatsSold != 0)
                    {
                        Console.WriteLine(nrTickets);
                        Console.WriteLine(newShow.seatsSold);
                        newShow.seatsSold = newShow.seatsSold + nrTickets;
                        Console.WriteLine(newShow.seatsSold);
                        artistController.updateShow(showId, newShow);

                        for (int i = 0; i < nrTickets; i++)
                            artistController.buyTicket(showId, buyerName);

                        clear_fields();
                        refresh_table();
                    }
                    else
                        MessageBox.Show(this, "There are no longer available tickets!");
                }


            }
            catch (Exception ex)
            {
                MessageBox.Show(this, ex.Message);
            }
        }

        private void artistsTable_RowHeaderMouseClick(object sender, DataGridViewCellMouseEventArgs e)
        {
            showId = Convert.ToInt64(artistsTable.Rows[e.RowIndex].Cells[4].Value);

            int nrT = Convert.ToInt32(artistsTable.Rows[e.RowIndex].Cells[3].Value);
            for (int i = 1; i <=nrT; i++)
                comboBoxBilets.Items.Add(i);

            data = DateTime.Parse(artistsTable.Rows[e.RowIndex].Cells[5].Value.ToString());
        }

        private void artistsTable_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            foreach( DataGridViewRow row in artistsTable.Rows)
            {
                int seatsAvailable = Convert.ToInt32(row.Cells[3].Value);

                if( seatsAvailable == 0)
                {
                    row.DefaultCellStyle.BackColor = Color.Red;
                }
            }
        }
    }
}
