package domain;

import java.io.Serializable;

public class Artist extends Entity<Long> implements Serializable {
    private String firstName;
    private String lastName;
    private int age;
    private String originCountry;

    public Artist(Long id, String firstName, String lastName, int age, String originCountry) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.originCountry = originCountry;
    }

    public Artist(String firstName, String lastName, int age, String originCountry) {
        super(0L);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.originCountry = originCountry;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    @Override
    public String toString() {
        return "Artist- Nume:" + lastName + " Prenume:" + firstName +
                " Varsta:" + age + " Tara origine:" + originCountry;

    }
}

