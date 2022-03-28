using MPP_MusicFestival.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Repository
{
    public interface ShowRepository: Repository<long, Show>
    {
        List<Show> getArtistsByDate(DateTime date);
    }
}
