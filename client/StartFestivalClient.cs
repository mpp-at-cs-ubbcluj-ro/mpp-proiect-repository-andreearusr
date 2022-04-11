using log4net.Config;
using MusicFestival.networking;
using MusicFestival.services;
using System;
using System.Windows.Forms;

namespace MusicFestival.client
{
    static class StartFestivalClient
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(string[] args)
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            XmlConfigurator.Configure(new System.IO.FileInfo("args[0]"));
            IFestivalService server = new FestivalServerProxy("127.0.0.1", 55556);
            FestivalClientCtrl ctrl = new FestivalClientCtrl(server);
            LoginWindow win = new LoginWindow(ctrl);
            Application.Run(win);

        }
    }
}
