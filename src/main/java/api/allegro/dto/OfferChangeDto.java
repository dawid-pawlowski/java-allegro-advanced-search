package api.allegro.dto;

import org.json.JSONObject;

import java.util.UUID;

public class OfferChangeDto extends JSONObject {
    public void setOfferCriteria(OfferCriteriaDto offerCriteria) {
        put("offerCriteria", offerCriteria);
    }

    public void addModification(JSONObject modification) {
        accumulate("modification", modification);
    }

    public void setPublication(JSONObject publication) {
        put("publication", publication);
    }
}
