package api.allegro.dto;

import api.allegro.bean.ShippingRateBean;
import org.json.JSONObject;

public class ShippingRateModificationDto extends JSONObject {
    public ShippingRateModificationDto(ShippingRateBean shippingRate) {
        put("delivery", new JSONObject().put("shippingRates", new JSONObject().put("id", shippingRate.getId())));
    }
}
