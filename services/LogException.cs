using System;


namespace MusicFestival.services
{
    class LogException : Exception
    {
        public LogException(string message): base(message)
        {
            
        }
    }
}
