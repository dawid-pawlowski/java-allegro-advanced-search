package api.allegro.dto;

import java.util.Map;

public class OfferDeliveryModificationDto {
    private Map<String, String> shippingRates;

    public OfferDeliveryModificationDto() {
    }

    public Map<String, String> getShippingRates() {
        return shippingRates;
    }

    public void setShippingRates(String shippingRateId) {
        this.shippingRates = Map.of(
                "id", shippingRateId
        );
    }
}
