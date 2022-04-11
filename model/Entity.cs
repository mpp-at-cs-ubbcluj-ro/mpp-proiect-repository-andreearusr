

using System;

namespace MusicFestival.model
{
    [Serializable]
    public class Entity<T>
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

