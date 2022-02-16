package api.allegro.dto;

import api.allegro.entity.OfferEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfferCriteriaDto {

    private String type;
    private List<Map<String, String>> offers;

    public OfferCriteriaDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Map<String, String>> getOffers() {
        return offers;
    }

    public void setOffers(List<String> offerIds) {
        List<Map<String, String>> offers = new ArrayList<>();
        for (String offerId : offerIds) {
            offers.add(Map.of("id", offerId));
        }
        this.offers = offers;
    }
}
