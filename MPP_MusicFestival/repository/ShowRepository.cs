using MPP_MusicFestival.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Repository
{
    interface ShowRepository: Repository<long, Show>
    {
        IEnumerable<Artist> findArtistsByDate(string date);
    }
}
