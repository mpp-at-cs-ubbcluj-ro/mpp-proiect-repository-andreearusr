
using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Drawing;
using System.Windows.Forms;

namespace MusicFestival.client
{
    public partial class EmployeeWindow : Form
    {
        private FestivalClientCtrl ctrl;

        ulong showId = 0;
        DateTime data;

        public EmployeeWindow(FestivalClientCtrl ctrl)
        {
            InitializeComponent();
            this.ctrl = ctrl;

            ctrl.updateEvent += userUpdate;
        }

        private void clear_fields()
        {
            textBoxBuyerName.Clear();
            comboBoxBilets.Items.Clear();
            comboBoxBilets.Text = "";
        }
        private void button1_Click(object sender, EventArgs e)
        {
            ctrl.tryLogout();
            //ctrl.updateEvent -= userUpdate;
            Application.Exit();
        }

        public void userUpdate(object sender, FestivalEventArgs e)
        {
            if (e.FestivalEventType == FestivalEvent.ShowUpdated)
            {
                Show show = (Show)e.Data;

                tableShows.BeginInvoke(new UpdateDataGridViewCallback(this.updateTableShows), new Object[] { show });
                artistsTable.BeginInvoke(new UpdateDataGridViewCallback(this.updateArtistsTable), new Object[] { show });

            }
        }

        public delegate void UpdateDataGridViewCallback(Show show);
        private void updateTableShows(Show show)
        {
            foreach (DataGridViewRow dgvr in tableShows.Rows)
            {
                if (dgvr == tableShows.Rows[tableShows.RowCount-1])
                {
                    break;
                }
                else if((dgvr.Cells[6].Value.ToString().Equals(show.Id.ToString())))
                {
                    dgvr.Cells[3].Value = show.seatsAvailable - show.seatsSold;
                    dgvr.Cells[4].Value = show.seatsSold;
                }
                
            }
        }

        private void updateArtistsTable(Show show)
        {
            foreach (DataGridViewRow dgvr in artistsTable.Rows)
            {
                if (dgvr == artistsTable.Rows[artistsTable.RowCount - 1])
                {
                    break;
                }
                else if ((dgvr.Cells[4].Value.ToString().Equals(show.Id.ToString())))
                {
                    dgvr.Cells[3].Value = show.seatsAvailable - show.seatsSold;
                }
            }
        }

        private void Employee_Load(object sender, EventArgs e)
        {
            try
            {
                DataTable table = ConvertListToDataTableRezultat(ctrl.getShows());
                tableShows.DataSource = table;
                tableShows.Columns[6].Visible = false;

            }
            catch (Exception ex)
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
            table.Columns.Add("Cod Show");


            // Add rows.
            foreach (Show rez in list)
            {
                table.Rows.Add(rez.showName, rez.artist.firstName + " " + rez.artist.lastName, rez.showLocation, rez.seatsAvailable - rez.seatsSold, rez.seatsSold, rez.dateTime.ToString().Substring(0, 9), rez.Id);
            }

            return table;
        }

        public static DataTable ConvertListToDataTableRezultat2(List<Show> list)
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
                table.Rows.Add(rez.artist.firstName + " " + rez.artist.lastName, rez.showLocation, rez.dateTime.ToString().Substring(10, 8), rez.seatsAvailable - rez.seatsSold, rez.Id, rez.dateTime.ToString().Substring(0, 9));
            }

            return table;
        }

        private void tableShows_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            foreach (DataGridViewRow row in tableShows.Rows)
            {
                if (row == tableShows.Rows[tableShows.RowCount - 1])
                {
                    break;
                }
                else
                {
                    int seatsAvailable = Convert.ToInt32(row.Cells[3].Value);

                    if (seatsAvailable == 0)
                    {
                        row.DefaultCellStyle.BackColor = Color.Red;
                    }
                }
            }
        }

        private void artistsTable_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            foreach (DataGridViewRow row in artistsTable.Rows)
            {
                if (row == artistsTable.Rows[artistsTable.RowCount - 1])
                {
                    break;
                }
                else
                {
                    int seatsAvailable = Convert.ToInt32(row.Cells[3].Value);

                    if (seatsAvailable == 0)
                    {
                        row.DefaultCellStyle.BackColor = Color.Red;
                    }
                }
            }


        }
        private void button2_Click_1(object sender, EventArgs e)
        {
            DateTime date = dateTimePicker1.Value;

            DataTable table2 = ConvertListToDataTableRezultat2(ctrl.getArtistsByDate(date));
            artistsTable.DataSource = table2;
            artistsTable.Columns[4].Visible = false;
            artistsTable.Columns[5].Visible = false;
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
                    uint nrTickets = Convert.ToUInt32(comboBoxBilets.SelectedItem);

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
                    Show newShow = ctrl.findShowById(showId);
                    if (newShow.seatsAvailable - newShow.seatsSold != 0)
                    {
                        Console.WriteLine(nrTickets);
                        Console.WriteLine(newShow.seatsSold);
                        newShow.seatsSold = newShow.seatsSold + nrTickets;
                        Console.WriteLine(newShow.seatsSold);

                        for (int i = 0; i < nrTickets; i++)
                            ctrl.buyTicket(showId, buyerName);

                        ctrl.updateShow(showId, newShow);
                        clear_fields();

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
            for (int i = 1; i <= nrT; i++)
                comboBoxBilets.Items.Add(i);

            data = DateTime.Parse(artistsTable.Rows[e.RowIndex].Cells[5].Value.ToString());
        }
    }
}
