package api.allegro.resource;

import api.allegro.app.App;
import api.allegro.exception.AllegroAccessDeniedException;
import api.allegro.exception.AllegroAuthorizationPendingException;
import api.allegro.exception.AllegroInvalidRefreshTokenException;
import api.allegro.exception.AllegroSlowDownException;
import api.allegro.util.Util;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationResource {
    private final String CLIENT_SECRET;
    private final String CLIENT_ID;

    private final HttpClient client = HttpClient.newHttpClient();
    private final Map<String, String> headers = new HashMap<>();

    public AuthorizationResource() {
        CLIENT_SECRET = App.getProperties().getProperty("client_secret");
        CLIENT_ID = App.getProperties().getProperty("client_id");

        byte[] encodedAuth = Base64.getEncoder().encode((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(StandardCharsets.UTF_8));

        headers.put("Authorization", "Basic " + new String(encodedAuth));
        headers.put("Content-Type", "application/x-www-form-urlencoded");
    }

    public Map<String, Object> getVerificationData() throws IOException, InterruptedException, IllegalStateException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("POST", HttpRequest.BodyPublishers.ofString("client_id=" + CLIENT_ID))
                .uri(URI.create("https://allegro.pl/auth/oauth/device"));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Unexpected value: " + response.statusCode());
        }

        JSONObject jsonObject = new JSONObject(response.body());
        return jsonObject.toMap();
    }

    public Map<String, Object> getTokens(String deviceCode) throws IOException, InterruptedException, AllegroAccessDeniedException, AllegroAuthorizationPendingException, AllegroSlowDownException, IllegalStateException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("POST", HttpRequest.BodyPublishers.ofString("grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Adevice_code&device_code=" + deviceCode))
                .uri(URI.create("https://allegro.pl/auth/oauth/token"));

        // TODO: send invalid device code and check error message

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            // TODO: check error message
            if (jsonObject.getString("error") != null) {
                switch (jsonObject.getString("error")) {
                    case "authorization_pending" ->
                            // TODO: allow fixed amount of attempts
                            throw new AllegroAuthorizationPendingException();
                    case "slow_down" -> throw new AllegroSlowDownException();
                    case "access_denied" -> throw new AllegroAccessDeniedException();
                    default -> throw new IllegalStateException("Unexpected value: " + jsonObject.get("error"));
                }
            }
        }

        return jsonObject.toMap();
    }

    public Map<String, Object> refreshTokens(String refreshToken) throws AllegroInvalidRefreshTokenException, IOException, InterruptedException, IllegalStateException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method("POST", HttpRequest.BodyPublishers.ofString("grant_type=refresh_token&refresh_token=" + refreshToken))
                .uri(URI.create("https://allegro.pl/auth/oauth/token"));

        Util.addRequestHeaders(builder, headers);
        HttpRequest request = builder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            // TODO: check error message
            if (jsonObject.getString("error") != null) {
                switch (jsonObject.getString("error")) {
                    case "invalid_grant" -> throw new AllegroInvalidRefreshTokenException();
                    default -> throw new IllegalStateException("Unexpected value: " + jsonObject.get("error"));
                }
            }
        }

        return jsonObject.toMap();
    }
}
