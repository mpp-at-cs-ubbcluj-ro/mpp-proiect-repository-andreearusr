package festival.services;

import festival.domain.Show;


public interface IFestivalObserver {
    void showUpdated(Show show) throws  LogException;
}
