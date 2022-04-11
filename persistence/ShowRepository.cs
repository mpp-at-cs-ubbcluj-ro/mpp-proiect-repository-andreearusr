
using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Text;

namespace MusicFestival.persistence
{
    public interface ShowRepository: Repository<long, Show>
    {
        List<Show> getArtistsByDate(DateTime date);
    }
}
