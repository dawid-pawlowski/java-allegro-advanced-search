package api.allegro.resource;

import api.allegro.exception.AllegroBadRequestException;
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

public class CommandResource {

    private final Map<String, String> headers = new HashMap<>();
    private final HttpClient client = HttpClient.newHttpClient();

    public CommandResource(String accessToken) {
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Accept", "application/vnd.allegro.public.v1+json");
    }

    public JSONObject offerModificationChangeSummary(String commandId) throws AllegroUnauthorizedException, AllegroNotFoundException, IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/offer-modification-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 401, 403 -> throw new AllegroUnauthorizedException();
                case 404 -> throw new AllegroNotFoundException();
            }
        }

        return new JSONObject(response.body());
    }

    public JSONObject offerPriceChangeSummary(String commandId) throws AllegroUnauthorizedException, AllegroNotFoundException, IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/offer-price-change-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 401, 403 -> throw new AllegroUnauthorizedException();
                case 404 -> throw new AllegroNotFoundException();
            }
        }

        return new JSONObject(response.body());
    }

    public JSONObject offerQuantityChangeSummary(String commandId) throws AllegroUnauthorizedException, AllegroNotFoundException, IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/offer-quantity-change-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 401, 403 -> throw new AllegroUnauthorizedException();
                case 404 -> throw new AllegroNotFoundException();
            }
        }

        return new JSONObject(response.body());
    }

    public JSONObject offerPublicationSummary(String commandId) throws AllegroUnauthorizedException, AllegroNotFoundException, IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/offer-publication-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 401, 403 -> throw new AllegroUnauthorizedException();
                case 404 -> throw new AllegroNotFoundException();
            }
        }

        return new JSONObject(response.body());
    }
}
