package start;

import festival.domain.Artist;
import festival.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rest.client.ArtistsClient;

public class StartRestClient {

    private final static ArtistsClient artistsClient = new ArtistsClient();

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
            Artist artistT = new Artist(34L,"Mihaela", "Dorobant", 22, "Romania");
        try {

            show(()->artistsClient.create(artistT));
            show(() -> System.out.println(artistsClient.getById(1L)));

            show(() -> {
                Artist[] res = artistsClient.getAll();
                for (Artist a : res) {
                    System.out.println(a.getId() + ": " + a.getFirstName() + " " + a.getLastName());
                }

            });

           /* Artist art = artistsClient.getById(9L);
            art.setAge(47);
            show(() -> artistsClient.update(art));*/

            //show(()->artistsClient.delete(9L));
            show(() -> {
                Artist[] res = artistsClient.getAll();
                for (Artist a : res) {
                    System.out.println(a.getId() + ": " + a.getFirstName() + " " + a.getLastName()+""+a.getAge());
                }

            });


        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }

    }


    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception" + e);
        }
    }
}
