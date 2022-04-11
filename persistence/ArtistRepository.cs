using MusicFestival.model;
using System;
using System.Collections.Generic;
using System.Text;

namespace MusicFestival.persistence
{
    public interface ArtistRepository: Repository<long, Artist>
    {
    }
}
