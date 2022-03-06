package domain;

import java.sql.Timestamp;

public class Show extends Entity<Long>{

    private String showName;
    private String description;
    private Artist artist;
    private Timestamp dateTime;
    private String showLocation;
    private int seatsAvailable;
    private int seatsSold;

    public Show(Long id, String showName, String description, Artist artist, Timestamp dateTime, String showLocation, int seatsAvailable, int seatsSold) {
        super(id);
        this.showName = showName;
        this.description = description;
        this.artist = artist;
        this.dateTime = dateTime;
        this.showLocation = showLocation;
        this.seatsAvailable = seatsAvailable;
        this.seatsSold = seatsSold;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getShowLocation() {
        return showLocation;
    }

    public void setShowLocation(String showLocation) {
        this.showLocation = showLocation;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public int getSeatsSold() {
        return seatsSold;
    }

    public void setSeatsSold(int seatsSold) {
        this.seatsSold = seatsSold;
    }

    @Override
    public String toString() {
        return "Spectacol- Nume:" + showName + " Descriere:" + description +
                "\nnume Artist:" + artist.getFirstName() + "\nData si ora:" + dateTime.toString() +
                " Locatia:" + showLocation;
    }
}
