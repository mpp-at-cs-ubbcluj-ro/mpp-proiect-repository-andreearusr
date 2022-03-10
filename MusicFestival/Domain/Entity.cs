using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Domain
{
    class Entity<T>
    {
        public T Id { get; set; }
        public Entity(T id)
        {
            this.Id = id;
        }

        public override string ToString()
        {
            return this.Id.ToString();
        }
    }
}

