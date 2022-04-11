using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MusicFestival.services
{
    public interface IFestivalObserver
    {
        void showUpdated(Show show);
    }
}
