package api.allegro.dto;

import api.allegro.enums.QuantityChangeModeEnum;
import org.json.JSONObject;

public class QuantityModificationDto extends JSONObject {
    public QuantityModificationDto(QuantityChangeModeEnum mode, String value) {
        put("changeValue", mode.name());
        put("value", value);
    }
}
