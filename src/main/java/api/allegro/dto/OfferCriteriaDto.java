package api.allegro.dto;

import api.allegro.entity.OfferEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class OfferCriteriaDto extends JSONArray {

    public OfferCriteriaDto(List<OfferEntity> offers) {
        JSONObject offerCriteriaObj = new JSONObject();
        offerCriteriaObj.put("type", "CONTAINS_OFFERS");

        JSONArray idArray = new JSONArray();
        for (OfferEntity offer : offers) {
            idArray.put(new JSONObject().put("id", offer.getId()));
        }

        offerCriteriaObj.put("offers", idArray);
        put(offerCriteriaObj);
    }
}
