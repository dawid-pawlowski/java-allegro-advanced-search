package api.allegro.dto;

import api.allegro.enums.PublishChangeModeEnum;
import org.json.JSONObject;

public class PublicationModificationDto extends ModificationDto {
    private String action;

    public PublicationModificationDto() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
