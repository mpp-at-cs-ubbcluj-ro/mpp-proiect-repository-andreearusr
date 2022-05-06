package festival.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Show extends Entity<Long> implements Serializable {

    private String showName;
    private String description;
    private Artist artist;
    private Timestamp dateTime;
    private String showLocation;
    private int seatsTotal;
    private int seatsSold;


    public Show(Long id, String showName, String description, Artist artist, Timestamp dateTime, String showLocation, int seatsTotal, int seatsSold) {
        setId(id);
        this.showName = showName;
        this.description = description;
        this.artist = artist;
        this.dateTime = dateTime;
        this.showLocation = showLocation;
        this.seatsTotal = seatsTotal;
        this.seatsSold = seatsSold;
    }

    public Show(String showName, String description, Artist artist, Timestamp dateTime, String showLocation, int seatsTotal, int seatsSold) {
        setId(0L);
        this.showName = showName;
        this.description = description;
        this.artist = artist;
        this.dateTime = dateTime;
        this.showLocation = showLocation;
        this.seatsTotal = seatsTotal;
        this.seatsSold = seatsSold;
    }


    public Long getId(){
        return  super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
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

    public int getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(int seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    public int getSeatsSold() {
        return seatsSold;
    }

    public void setSeatsSold(int seatsSold) {
        this.seatsSold = seatsSold;
    }

    @Override
    public String toString() {
        return "Show{" +
                "showName='" + showName + '\'' +
                ", description='" + description + '\'' +
                ", artist=" + getArtist() +
                ", dateTime=" + dateTime +
                ", showLocation='" + showLocation + '\'' +
                ", seatsTotal=" + seatsTotal +
                ", seatsSold=" + seatsSold +
                '}';
    }
}
