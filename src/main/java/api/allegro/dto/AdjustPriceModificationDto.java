package api.allegro.dto;

import java.util.Map;

public class AdjustPriceModificationDto extends ModificationDto {
    private String type;
    private Map<String, String> value;

    public AdjustPriceModificationDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = Map.of(
                "amount", value,
                "currency", "PLN"
        );
    }
}
