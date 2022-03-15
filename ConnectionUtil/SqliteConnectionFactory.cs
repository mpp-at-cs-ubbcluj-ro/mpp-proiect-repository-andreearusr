using Microsoft.Data.Sqlite;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.SQLite;

namespace ConnectionUtil
{
	public class SqliteConnectionFactory : ConnectionFactory
	{
		public override IDbConnection createConnection(IDictionary<string, string> props)
		{
			//Mono Sqlite Connection

			//String connectionString = "URI=file:C/databases/festival.db";
			/*String connectionString = props["ConnectionString"];
			Console.WriteLine("SQLite ---Se deschide o conexiune la  ... {0}", connectionString);
			return new SQLiteConnection(connectionString);*/

			// Windows SQLite Connection, fisierul .db ar trebuie sa fie in directorul debug/bin
			String connectionString = "Data Source=festival.db;Version=3";
			return new SQLiteConnection(connectionString);
			
			//return new SQLiteConnection(props["ConnectionString"]);
		}
	}
}
