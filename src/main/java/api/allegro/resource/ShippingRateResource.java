package api.allegro.resource;

import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.util.Util;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ShippingRateResource {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Map<String, String> headers = new HashMap<>();

    public ShippingRateResource(String accessToken) {
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Accept", "application/vnd.allegro.public.v1+json");
    }

    public JSONObject getShippingRates() throws IOException, InterruptedException, AllegroUnauthorizedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/shipping-rates/"));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                case 401, 493 -> throw new AllegroUnauthorizedException();
                default -> throw new IllegalStateException("Unexpected value: " + jsonObject.get("error"));
            }
        }

        return jsonObject;
    }

}
