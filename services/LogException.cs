using System;


namespace MusicFestival.services
{
    public class LogException : Exception
    {
        public LogException(string message): base(message)
        {
            
        }
    }
}
