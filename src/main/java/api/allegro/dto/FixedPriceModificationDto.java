package api.allegro.dto;

import api.allegro.enums.PriceChangeModeEnum;

import java.util.HashMap;
import java.util.Map;

public class FixedPriceModificationDto extends ModificationDto {

    private String type;
    private Map<String, String> price;

    public FixedPriceModificationDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getPrice() {
        return price;
    }

    public void setPrice(String value) {
        this.price = Map.of(
                "amount", value,
                "currency", "PLN"
        );
    }
}
