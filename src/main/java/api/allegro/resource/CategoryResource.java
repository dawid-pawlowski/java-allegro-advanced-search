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

public class CategoryResource {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Map<String, String> headers = new HashMap<>();
    private final String mainCategoryId = "954b95b6-43cf-4104-8354-dea4d9b10ddf";

    public CategoryResource(String accessToken) {
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Accept", "application/vnd.allegro.public.v1+json");
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public JSONObject getCategoryById(String categoryId) throws AllegroUnauthorizedException, IOException, InterruptedException, AllegroNotFoundException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/categories/" + categoryId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                case 401 -> throw new AllegroUnauthorizedException();
                case 404 -> throw new AllegroNotFoundException();
                default -> throw new IllegalStateException("Unexpected value: " + jsonObject.get("errors"));
            }
        }

        return jsonObject;
    }

    public JSONObject getCategoryByParentId(String parentId) throws AllegroUnauthorizedException, InterruptedException, IOException, AllegroNotFoundException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/categories?parent.id=" + parentId));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                case 401 -> throw new AllegroUnauthorizedException();
                case 404 -> throw new AllegroNotFoundException();
                default -> throw new IllegalStateException("Unexpected value: " + jsonObject.get("errors"));
            }
        }

        return jsonObject;
    }

    public JSONObject getCategoryParameters(String categoryId) throws AllegroUnauthorizedException, InterruptedException, IOException, AllegroNotFoundException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.allegro.pl/sale/categories/" + categoryId + "/parameters"));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            switch (response.statusCode()) {
                case 401 -> throw new AllegroUnauthorizedException();
                case 404 -> throw new AllegroNotFoundException();
                default -> throw new IllegalStateException("Unexpected value: " + jsonObject.get("errors"));
            }
        }

        return jsonObject;
    }

}
