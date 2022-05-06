package festival.network.protobuffprotocol;

import festival.domain.OfficeEmployee;
import festival.domain.Ticket;
import festival.domain.Show;
import festival.domain.Artist;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {

    public static FestivalProtobufs.FestivalRequest createLoginRequest(OfficeEmployee officeEmployee) {
        FestivalProtobufs.OfficeEmployee officeEmployee1 = FestivalProtobufs.OfficeEmployee.newBuilder().setId(officeEmployee.getId()).setCNP(officeEmployee.getCNP()).setFirstName(officeEmployee.getFirstName())
                .setLastName(officeEmployee.getLastName()).setPassword(officeEmployee.getPassword()).setUsername(officeEmployee.getUsername()).build();
        FestivalProtobufs.FestivalRequest request = FestivalProtobufs.FestivalRequest.newBuilder().setType(FestivalProtobufs.FestivalRequest.Type.Login)
                .setOfficeEmployee(officeEmployee1).build();
        return request;
    }

    public static FestivalProtobufs.FestivalRequest createLogoutRequest(OfficeEmployee officeEmployee) {
        FestivalProtobufs.OfficeEmployee officeEmployee1 = FestivalProtobufs.OfficeEmployee.newBuilder().setId(officeEmployee.getId()).setCNP(officeEmployee.getCNP()).setFirstName(officeEmployee.getFirstName())
                .setLastName(officeEmployee.getLastName()).setPassword(officeEmployee.getPassword()).setUsername(officeEmployee.getUsername()).build();
        FestivalProtobufs.FestivalRequest request = FestivalProtobufs.FestivalRequest.newBuilder().setType(FestivalProtobufs.FestivalRequest.Type.Logout)
                .setOfficeEmployee(officeEmployee1).build();
        return request;
    }

    public static FestivalProtobufs.FestivalRequest createFindShowByIdRequest(Long id) {
        FestivalProtobufs.LongM idM = FestivalProtobufs.LongM.newBuilder().setId(id).build();
        FestivalProtobufs.FestivalRequest request = FestivalProtobufs.FestivalRequest.newBuilder()
                .setType(FestivalProtobufs.FestivalRequest.Type.FindShowById)
                .setId(idM)
                .build();
        return request;
    }

    public static FestivalProtobufs.FestivalRequest createFindEmployeeByUsernameRequest(String username) {
        FestivalProtobufs.StringM usernameM = FestivalProtobufs.StringM.newBuilder().setData(username).build();
        FestivalProtobufs.FestivalRequest request = FestivalProtobufs.FestivalRequest.newBuilder()
                .setType(FestivalProtobufs.FestivalRequest.Type.FindEmployeeByUsername)
                .setData(usernameM)
                .build();
        return request;
    }


    public static FestivalProtobufs.FestivalRequest createGetShowsRequest() {
        FestivalProtobufs.FestivalRequest request = FestivalProtobufs.FestivalRequest.newBuilder()
                .setType(FestivalProtobufs.FestivalRequest.Type.GetShows)
                .build();
        return request;
    }

    public static FestivalProtobufs.FestivalRequest createGetArtistsByDateRequest(Timestamp date) {
        FestivalProtobufs.StringM dateM = FestivalProtobufs.StringM.newBuilder().setData(date.toString()).build();
        FestivalProtobufs.FestivalRequest request = FestivalProtobufs.FestivalRequest.newBuilder()
                .setType(FestivalProtobufs.FestivalRequest.Type.GetArtistsByDate)
                .setData(dateM)
                .build();
        return request;
    }

    public static FestivalProtobufs.FestivalRequest createBuyTicketRequest(Ticket ticket) {
        FestivalProtobufs.Ticket ticketM = FestivalProtobufs.Ticket.newBuilder().setId(ticket.getId()).setBuyerName(ticket.getBuyerName()).setEmployeeId(ticket.getEmployeeId()).setShowId(ticket.getShowId()).build();
        FestivalProtobufs.FestivalRequest request = FestivalProtobufs.FestivalRequest.newBuilder()
                .setType(FestivalProtobufs.FestivalRequest.Type.BuyTicket)
                .setTicket(ticketM)
                .build();
        return request;
    }

    public static FestivalProtobufs.FestivalRequest createUpdateShowRequest(Long showId, Show newShow) {
        FestivalProtobufs.LongM showIdM = FestivalProtobufs.LongM.newBuilder().setId(showId).build();

        FestivalProtobufs.Artist srtistM = FestivalProtobufs.Artist.newBuilder().setId(newShow.getArtist().getId()).setAge(newShow.getArtist().getAge()).setFirstName(newShow.getArtist().getFirstName())
                .setLastName(newShow.getArtist().getFirstName()).setOriginCountry(newShow.getArtist().getOriginCountry()).build();
        FestivalProtobufs.Show newShowM = FestivalProtobufs.Show.newBuilder().setId(newShow.getId()).setShowName(newShow.getShowName()).setArtist(srtistM).setDateTime(newShow.getDateTime().toString())
                .setShowLocation(newShow.getShowLocation()).setDescription(newShow.getDescription()).setSeatsSold(newShow.getSeatsSold()).setSeatsTotal(newShow.getSeatsTotal()).build();
        FestivalProtobufs.FestivalRequest request = FestivalProtobufs.FestivalRequest.newBuilder()
                .setType(FestivalProtobufs.FestivalRequest.Type.UpdateShow)
                .setId(showIdM)
                .setShow(newShowM)
                .build();
        return request;
    }


    public static String getError(FestivalProtobufs.FestivalResponse response) {
        String errorMessage = response.getError();
        return errorMessage;
    }


    public static OfficeEmployee getOfficeE(FestivalProtobufs.FestivalResponse response) {
        OfficeEmployee officeEmployee = new OfficeEmployee(response.getOfficeEmployee().getId(), response.getOfficeEmployee().getFirstName(),
                response.getOfficeEmployee().getLastName(), response.getOfficeEmployee().getCNP(), response.getOfficeEmployee().getUsername(),
                response.getOfficeEmployee().getPassword());

        return officeEmployee;
    }


    public static Show getShowUpdated(FestivalProtobufs.FestivalResponse response) {
        Artist artist = new Artist(response.getShow().getArtist().getId(), response.getShow().getArtist().getFirstName(),
                response.getShow().getArtist().getLastName(), response.getShow().getArtist().getAge(), response.getShow().getArtist().getOriginCountry());
        Show show = new Show(response.getShow().getId(), response.getShow().getShowName(),
                response.getShow().getDescription(), artist,
                Timestamp.valueOf(response.getShow().getDateTime()), response.getShow().getShowLocation(),
                response.getShow().getSeatsTotal(), response.getShow().getSeatsSold());
        return show;
    }

    public static Show getShowById(FestivalProtobufs.FestivalResponse response) {
        Artist artist = new Artist(response.getShow().getArtist().getId(), response.getShow().getArtist().getFirstName(),
                response.getShow().getArtist().getLastName(), response.getShow().getArtist().getAge(), response.getShow().getArtist().getOriginCountry());
        Show show = new Show(response.getShow().getId(), response.getShow().getShowName(),
                response.getShow().getDescription(), artist,
                Timestamp.valueOf(response.getShow().getDateTime()), response.getShow().getShowLocation(),
                response.getShow().getSeatsTotal(), response.getShow().getSeatsSold());
        return show;
    }


    public static List<Show> getShows(FestivalProtobufs.FestivalResponse response) {
        List<Show> shows = new ArrayList<>();

        for (int i = 0; i < response.getShowsList().size(); i++) {
            FestivalProtobufs.Show show = response.getShows(i);

            Artist artist = new Artist(show.getArtist().getId(), show.getArtist().getFirstName(),
                    show.getArtist().getLastName(), show.getArtist().getAge(), show.getArtist().getOriginCountry());

            Show show1 = new Show(show.getId(), show.getShowName(), show.getDescription(),
                    artist, Timestamp.valueOf(show.getDateTime()), show.getShowLocation(),
                    show.getSeatsTotal(), show.getSeatsSold());
            shows.add(show1);
        }

        return shows;
    }

    public static List<Show> getShowsByDate(FestivalProtobufs.FestivalResponse response) {
        List<Show> shows = new ArrayList<>();

        for (int i = 0; i < response.getShowsList().size(); i++) {
            FestivalProtobufs.Show show = response.getShows(i);

            Artist artist = new Artist(show.getArtist().getId(), show.getArtist().getFirstName(),
                    show.getArtist().getLastName(), show.getArtist().getAge(), show.getArtist().getOriginCountry());

            Show show1 = new Show(show.getId(), show.getShowName(), show.getDescription(),
                    artist, Timestamp.valueOf(show.getDateTime()), show.getShowLocation(),
                    show.getSeatsTotal(), show.getSeatsSold());

            shows.add(show1);
        }

        return shows;
    }


}
