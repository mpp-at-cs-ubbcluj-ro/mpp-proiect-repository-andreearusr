﻿using System;
using System.Collections.Generic;
using System.Text;

namespace MPP_MusicFestival.Domain
{
    public class Artist : Entity<long>
    {
        public string firstName { get; set; }
        public string lastName { get; set; }
        public int age { get; set; }
        public string originCountry { get; set; }

        public Artist(long id, string firstName, string lastName, int age, string originCountry) : base(id)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.originCountry = originCountry;
        }
        public Artist(string firstName, string lastName, int age, string originCountry) : base(0)
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
