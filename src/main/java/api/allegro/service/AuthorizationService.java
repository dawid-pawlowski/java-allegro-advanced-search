package api.allegro.service;

import api.allegro.entity.TokenEntity;
import api.allegro.exception.AllegroAccessDeniedException;
import api.allegro.exception.AllegroAuthorizationPendingException;
import api.allegro.exception.AllegroInvalidRefreshTokenException;
import api.allegro.exception.AllegroSlowDownException;
import api.allegro.repository.TokenRepository;
import api.allegro.resource.AuthorizationResource;

import java.io.IOException;
import java.util.Map;

public class AuthorizationService {

    private final TokenRepository repository;
    private final AuthorizationResource resource;

    private String deviceCode;

    public AuthorizationService(String pu) {
        resource = new AuthorizationResource();
        repository = new TokenRepository(pu);
    }

    public String getVerificationUri() throws IOException, InterruptedException, IllegalStateException {
        Map<String, Object> verificationData = resource.getVerificationData();
        deviceCode = (String) verificationData.get("device_code");
        return (String) verificationData.get("verification_uri_complete");
    }

    public String getAccessToken() {
        TokenEntity token = repository.findFirst();

        if (token != null) {
            return token.getAccessToken();
        }

        return null;
    }

    public void clearTokens() {
        repository.clear();
    }

    public void refreshTokens() throws IllegalStateException, AllegroInvalidRefreshTokenException, IOException, InterruptedException {
        TokenEntity token = repository.findFirst();
        Map<String, Object> tokenData = resource.refreshTokens(token.getRefreshToken());
        token.setAccessToken((String) tokenData.get("access_token"));
        token.setRefreshToken((String) tokenData.get("refresh_token"));
        token.setExpireDate(((Integer) tokenData.get("expires_in") + (System.currentTimeMillis() / 1000)));
        repository.update(token);
    }

    public void getTokens() throws IllegalStateException, AllegroSlowDownException, AllegroAuthorizationPendingException, AllegroAccessDeniedException, IOException, InterruptedException {
        TokenEntity token = new TokenEntity();
        Map<String, Object> tokenData = resource.getTokens(deviceCode);
        token.setAccessToken((String) tokenData.get("access_token"));
        token.setRefreshToken((String) tokenData.get("refresh_token"));
        token.setExpireDate(((Integer) tokenData.get("expires_in") + (System.currentTimeMillis() / 1000)));
        repository.add(token);
    }
}
