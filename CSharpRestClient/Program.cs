using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Text;
using System.Threading.Tasks;

namespace CSharpRestClient
{
    class Program
    {
        static HttpClient client = new HttpClient();

        public static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
            RunAsync().Wait();
        }

        static async Task RunAsync()
        {
           
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            /*long id = 1;
            Console.WriteLine("Get artist {0}", id);
            Artist artist = await GetArtistAsync("http://localhost:8090/festival/Artist/" + id);
            Console.WriteLine("Am primit {0}", artist);

            Console.WriteLine("Get all artists");
            List<Artist> artists = await GetAllArtistsAsync("http://localhost:8090/festival/Artist/");
            foreach (Artist a in artists)
            {
                Console.WriteLine(a);
            }

            long id2 = 8;
            Console.WriteLine("Delete artist {0}", id2);
            string response = await DeleteArtistAsync("http://localhost:8090/festival/Artist/" + id2);
            Console.WriteLine("Am primit {0}", response);
*/

           // Console.WriteLine("Add artist");
            //CreateArtistAsync("http://localhost:8090/festival/Artist/");

            Console.WriteLine("Update artist");
            long id3 = 33;
            UpdateArtistAsync("http://localhost:8090/festival/Artist/" + id3);

            Console.ReadLine();
        }

        static async void UpdateArtistAsync(string path)
        {

            var data = new
            {
                id = 33,
                firstName = "Andrei",
                lastName = "Solares",
                age = 19,
                originCountry = "Romania"

            };

            JsonContent content = JsonContent.Create(data);
            await client.PutAsync(path, content);
        }

        static async void CreateArtistAsync(string path)
        {
        
            var data = new
            {
                id = 0,
                firstName = "Andrei",
                lastName = "Soles",
                age = 18,
                originCountry = "Romania"

            };

            JsonContent content = JsonContent.Create(data);
            await client.PostAsync(path, content);
        }


        static async Task<string> DeleteArtistAsync(string path)
        {
            string msg = "";
            HttpResponseMessage response =  await client.DeleteAsync(path);
            if (response.IsSuccessStatusCode)
            {
                msg = "Artist deleted succesfully!";
            }
            else
            {
                msg = "Artist not found!";
            }
             return msg;
          
        }

        static async Task<Artist> GetArtistAsync(string path)
        {
            Artist artist = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                artist = await response.Content.ReadAsAsync<Artist>();
            }
            return artist;
        }

        static async Task<List<Artist>> GetAllArtistsAsync(string path)
        {
            List<Artist> artists = null;

            HttpResponseMessage response = await client.GetAsync(path);
            var jsonString = await response.Content.ReadAsStringAsync();
            if (response.IsSuccessStatusCode)
            {
                artists = JsonConvert.DeserializeObject<List<Artist>>(jsonString);
            }
            return artists;
        }


    }

    public class Artist
        {
            public long id { get; set; }
            public string firstName { get; set; }
            public string lastName { get; set; }
            public int age { get; set; }
            public string originCountry { get; set; }

            public Artist(long id, string firstName, string lastName, int age, string originCountry)
            {
                this.firstName = firstName;
                this.lastName = lastName;
                this.age = age;
                this.originCountry = originCountry;
            }

            public override string ToString()
            {
                return "Artist- Nume:" + lastName + " Prenume:" + firstName +
                    " Varsta:" + age + " Tara origine:" + originCountry;
            }
        }
}

