package api.allegro.dto;

import api.allegro.enums.QuantityChangeModeEnum;
import org.json.JSONObject;

public class QuantityModificationDto extends ModificationDto {
    private String changeValue;
    private String value;

    public QuantityModificationDto() {
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
