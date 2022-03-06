using MPP_MusicFestival.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Repository
{
    interface ArtistRepository: Repository<long, Artist>
    {
    }
}
