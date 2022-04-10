package services;

import domain.Show;
import domain.Ticket;


public interface IFestivalObserver {
    void showUpdated(Show show) throws  LogException;
}
