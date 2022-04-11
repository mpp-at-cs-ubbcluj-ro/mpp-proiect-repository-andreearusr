using System;
using System.Windows.Forms;


namespace MusicFestival.client
{
    public partial class LoginWindow : Form
    {

        private FestivalClientCtrl ctrl;

        public LoginWindow(FestivalClientCtrl ctrl)
        {
            InitializeComponent();
            this.ctrl = ctrl;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                ctrl.tryLogin(usernameText.Text, passwordText.Text);

                EmployeeWindow employeeWin = new EmployeeWindow(ctrl);
                employeeWin.Text = "Festival window for " + usernameText.Text;
                employeeWin.Show();
                usernameText.Clear();
                passwordText.Clear();
                this.Hide();
            }
            catch (Exception ex)
            {
                MessageBox.Show(this, ex.Message);
            }

        }

    }
}
