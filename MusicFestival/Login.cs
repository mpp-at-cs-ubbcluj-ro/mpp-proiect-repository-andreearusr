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
    public partial class Login : Form
    {

        private LoginController loginController;
        private Employee employeeForm;
        public Login(LoginController loginController)
        {
            InitializeComponent();
            this.loginController = loginController;
        }

        internal void setEmployeeForm(Employee employeeForm)
        {
            this.employeeForm = employeeForm;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                loginController.tryLogin(usernameText.Text, passwordText.Text);
                usernameText.Text = "";
                passwordText.Text = "";
                employeeForm.Show();
                this.Hide();
            }
            catch (LogException ex)
            {
                MessageBox.Show(this, ex.Message);
            }
            catch (Exception exx)
            {
                MessageBox.Show(this, exx.Message);
            }

        }

    }
}
