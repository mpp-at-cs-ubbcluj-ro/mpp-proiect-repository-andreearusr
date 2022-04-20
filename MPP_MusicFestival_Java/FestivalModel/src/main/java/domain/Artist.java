package domain;

import java.io.Serializable;


public class Artist extends Entity<Long> implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String originCountry;


    public Artist() {

    }

    public Artist(Long id, String firstName, String lastName, int age, String originCountry) {
        setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.originCountry = originCountry;
    }

    public Artist(String firstName, String lastName, int age, String originCountry) {
        setId(0L);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.originCountry = originCountry;
    }


    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
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
        return "Artist{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

