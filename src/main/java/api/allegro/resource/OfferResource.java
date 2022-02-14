package api.allegro.resource;

import api.allegro.exception.AllegroBadRequestException;
import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.util.Util;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class OfferResource {

    private final Map<String, String> headers = new HashMap<>();
    private final HttpClient client = HttpClient.newHttpClient();

    public OfferResource(String accessToken) {
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Accept", "application/vnd.allegro.public.v1+json");
    }

    public void batchOfferPriceChange(String commandId, JSONObject change) throws IOException, InterruptedException, AllegroBadRequestException, AllegroUnauthorizedException {

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("PUT", HttpRequest.BodyPublishers.ofString(change.toString()))
                .uri(URI.create("https://api.allegro.pl/sale/offer-price-change-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 400 -> throw new AllegroBadRequestException();
                case 401, 403 -> throw new AllegroUnauthorizedException();
            }
        }
    }

    public void batchOfferPublishChange(String commandId, JSONObject change) throws AllegroBadRequestException, AllegroUnauthorizedException, IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("PUT", HttpRequest.BodyPublishers.ofString(change.toString()))
                .uri(URI.create("https://api.allegro.pl/sale/offer-publication-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 400 -> throw new AllegroBadRequestException();
                case 401, 403 -> throw new AllegroUnauthorizedException();
            }
        }
    }

    public void batchOfferModificationChange(String commandId, JSONObject change) throws IOException, InterruptedException, AllegroBadRequestException, AllegroUnauthorizedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("PUT", HttpRequest.BodyPublishers.ofString(change.toString()))
                .uri(URI.create("https://api.allegro.pl/sale/offer-modification-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 400 -> throw new AllegroBadRequestException();
                case 401, 403 -> throw new AllegroUnauthorizedException();
            }
        }
    }

    public void batchOfferQuantityChange(String commandId, JSONObject change) throws IOException, InterruptedException, AllegroUnauthorizedException, AllegroBadRequestException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("PUT", HttpRequest.BodyPublishers.ofString(change.toString()))
                .uri(URI.create("https://api.allegro.pl/sale/offer-quantity-change-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 400 -> throw new AllegroBadRequestException();
                case 401, 403 -> throw new AllegroUnauthorizedException();
            }
        }
    }

    // TODO: implement filtering via query params
    public JSONObject getOffers(int offset, int limit) throws IOException, InterruptedException, AllegroBadRequestException, AllegroUnauthorizedException {
        // TODO: use StringBuilder
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/offers?limit=" + limit + "&offset=" + offset));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                // TODO: implement all exception cases
                case 400 -> throw new AllegroBadRequestException();
                case 403 -> throw new AllegroUnauthorizedException();
            }
        }

        return new JSONObject(response.body());
    }

    public JSONObject getOffer(String offerId) throws IOException, InterruptedException, AllegroBadRequestException, AllegroUnauthorizedException, AllegroNotFoundException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/offers/" + offerId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                case 400 -> throw new AllegroBadRequestException();
                case 401, 403 -> throw new AllegroUnauthorizedException();
                case 404 -> throw new AllegroNotFoundException();
            }
        }

        return new JSONObject(response.body());
    }
}
