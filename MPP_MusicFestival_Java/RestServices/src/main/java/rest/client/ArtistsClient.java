package rest.client;

import festival.domain.Artist;
import festival.services.rest.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class ArtistsClient {
    public static final String URL = "http://localhost:8090/festival/Artist";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Artist[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Artist[].class));
    }

    public Artist getById(Long id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Artist.class));
    }

    public void create(Artist artist) {
        execute(() -> restTemplate.postForObject(URL, artist, Artist.class));
    }

    public void update(Artist artist) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, artist.getId()), artist);
            return null;
        });
    }

    public void delete(Long id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
