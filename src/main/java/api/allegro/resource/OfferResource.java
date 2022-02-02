package api.allegro.resource;

import api.allegro.exception.AllegroBadRequestException;
import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OfferResource {

    private final Map<String, String> headers = new HashMap<>();
    private final HttpClient client = HttpClient.newHttpClient();

    public OfferResource(String accessToken) {
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Accept", "application/vnd.allegro.public.v1+json");
    }

    public JSONObject editOffer(Map<String, Map<String, Object>> offer) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("PUT", HttpRequest.BodyPublishers.ofString(offer.toString()))
                .uri(URI.create("https://api.allegro.pl/sale/offers/" + offer.get("id")));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            // TODO: implement all exception cases
            return null;
        }

        return new JSONObject(response.body());
    }

    public JSONObject batchOfferPriceChange(List<String> offers, String type, String value) throws IOException, InterruptedException {
        Map<String, String> offersIdMap = new HashMap<>();
        for (String offer : offers) {
            offersIdMap.put("id", offer);
        }

        JSONArray jsonOffers = new JSONArray();
        jsonOffers.putAll(offersIdMap);

        JSONArray offerCriteria = new JSONArray();
        offerCriteria.put(Map.of(
                "offers", jsonOffers,
                "type", "CONTAINS_OFFERS"
        ));

        JSONObject modification = new JSONObject();
        modification.put("type", type);

        if (type.equals("FIXED_PRICE")) {
            modification.put("price", Map.of(
                    "amount", value,
                    "currency", "PLN"
            ));
        }

        if (type.equals("INCREASE_PRICE") || type.equals("DECREASE_PRICE")) {
            modification.put("value", Map.of(
                    "amount", value,
                    "currency", "PLN"
            ));
        }

        if (type.equals("INCREASE_PERCENTAGE") || type.equals("DECREASE_PERCENTAGE")) {
            modification.put("percentage", value);
        }

        UUID commandId = UUID.randomUUID();

        JSONObject payload = new JSONObject();
        payload.put("commandId", commandId);
        payload.put("modification", modification);
        payload.put("offerCriteria", offerCriteria);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("PUT", HttpRequest.BodyPublishers.ofString(payload.toString()))
                .uri(URI.create("https://api.allegro.pl/sale/offer-price-change-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            // TODO: implement all exception cases
            return null;
        }

        return new JSONObject(response.body());
    }

    public JSONObject batchOfferQuantityChange(List<String> offers, String type, String value) throws IOException, InterruptedException {
        Map<String, String> offersIdMap = new HashMap<>();
        for (String offer : offers) {
            offersIdMap.put("id", offer);
        }

        JSONArray jsonOffers = new JSONArray();
        jsonOffers.putAll(offersIdMap);

        JSONArray offerCriteria = new JSONArray();
        offerCriteria.put(Map.of(
                "offers", jsonOffers,
                "type", "CONTAINS_OFFERS"
        ));

        JSONObject modification = new JSONObject();
        modification.put("changeType", type);
        modification.put("value", value);

        UUID commandId = UUID.randomUUID();

        JSONObject payload = new JSONObject();
        payload.put("commandId", commandId);
        payload.put("modification", modification);
        payload.put("offerCriteria", offerCriteria);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("PUT", HttpRequest.BodyPublishers.ofString(payload.toString()))
                .uri(URI.create("https://api.allegro.pl/sale/offer-quantity-change-commands/" + commandId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            // TODO: implement all exception cases
            return null;
        }

        return new JSONObject(response.body());
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
