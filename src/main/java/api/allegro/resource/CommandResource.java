package api.allegro.resource;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

public class CommandResource {

    private final Map<String, String> headers = new HashMap<>();
    private final HttpClient client = HttpClient.newHttpClient();

    public CommandResource(String accessToken) {
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Accept", "application/vnd.allegro.public.v1+json");
    }
}
