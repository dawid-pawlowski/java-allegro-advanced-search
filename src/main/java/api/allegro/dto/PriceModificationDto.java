package api.allegro.dto;

import api.allegro.enums.PriceChangeModeEnum;
import org.json.JSONObject;

import java.util.Map;

public class PriceModificationDto extends JSONObject {
    public PriceModificationDto(PriceChangeModeEnum mode, String value) {
        put("type", mode.name());

        if (mode.equals(PriceChangeModeEnum.FIXED_PRICE)) {
            put("price", Map.of(
                    "amount", value,
                    "currency", "PLN"
            ));
        }

        if (mode.equals(PriceChangeModeEnum.INCREASE_PRICE) || mode.equals(PriceChangeModeEnum.DECREASE_PRICE)) {
            put("value", Map.of(
                    "amount", value,
                    "currency", "PLN"
            ));
        }

        if (mode.equals(PriceChangeModeEnum.INCREASE_PERCENTAGE) || mode.equals(PriceChangeModeEnum.DECREASE_PRICE)) {
            put("percentage", value);
        }
    }
}
