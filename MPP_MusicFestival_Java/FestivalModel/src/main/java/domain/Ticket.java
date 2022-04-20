package domain;

import java.io.Serializable;

public class Ticket extends Entity<Long> implements Serializable {

    public Long showId;
    public Long employeeId;
    public String buyerName;

    public Ticket(Long id, Long showId, Long employeeId, String buyerName) {
        setId(id);
        this.showId = showId;
        this.employeeId = employeeId;
        this.buyerName = buyerName;
    }

    public Ticket(Long showId, Long employeeId, String buyerName) {
        setId(0L);
        this.showId = showId;
        this.employeeId = employeeId;
        this.buyerName = buyerName;
    }


    public Long getId(){
        return  super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    @Override
    public String toString() {
        return "Bilet- Cod:" + super.toString() + " Nume cumparator:" + buyerName;
    }
}
