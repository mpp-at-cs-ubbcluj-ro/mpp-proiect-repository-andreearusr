using System;

namespace MusicFestival.client
{
    public enum FestivalEvent
    {
        ShowUpdated
    };
    public class FestivalEventArgs : EventArgs
    {
        private readonly FestivalEvent festivalEvent;
        private readonly Object data;

        public FestivalEventArgs(FestivalEvent festivalEvent, object data)
        {
            this.festivalEvent = festivalEvent;
            this.data = data;
        }

        public FestivalEvent FestivalEventType
        {
            get { return festivalEvent; }
        }

        public object Data
        {
            get { return data; }
        }
    }
}
