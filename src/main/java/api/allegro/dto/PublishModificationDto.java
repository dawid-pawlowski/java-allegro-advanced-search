package api.allegro.dto;

import api.allegro.enums.PublishChangeModeEnum;
import org.json.JSONObject;

public class PublishModificationDto extends JSONObject {
    public PublishModificationDto(PublishChangeModeEnum mode) {
        put("action", mode.name());
    }
}
