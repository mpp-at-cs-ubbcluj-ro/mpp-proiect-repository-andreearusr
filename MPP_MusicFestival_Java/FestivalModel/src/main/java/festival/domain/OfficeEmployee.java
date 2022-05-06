package festival.domain;

import java.io.Serializable;

public class OfficeEmployee extends Entity<Long> implements Serializable {

    private String firstName;
    private String lastName;
    private Long CNP;
    private String username;
    private String password;


    public OfficeEmployee(Long id, String firstName, String lastName, Long CNP, String username, String password) {
        setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.CNP = CNP;
        this.username = username;
        this.password = password;
    }

    public OfficeEmployee(String firstName, String lastName, Long CNP, String username, String password) {
        setId(0L);
        this.firstName = firstName;
        this.lastName = lastName;
        this.CNP = CNP;
        this.username = username;
        this.password = password;
    }

    public Long getId(){
        return  super.getId();
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

    public Long getCNP() {
        return CNP;
    }

    public void setCNP(Long CNP) {
        this.CNP = CNP;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Cod angajat:" + super.toString() + " Nume:" + lastName +
                " Prenume:" + firstName + " CNP:" + CNP;
    }
}
