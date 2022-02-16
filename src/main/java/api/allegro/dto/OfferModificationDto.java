package api.allegro.dto;

public class OfferModificationDto extends ModificationDto {
    private OfferDeliveryModificationDto delivery;
    // TODO: add other modification options


    public OfferModificationDto() {
    }

    public OfferDeliveryModificationDto getDelivery() {
        return delivery;
    }

    public void setDelivery(OfferDeliveryModificationDto delivery) {
        this.delivery = delivery;
    }
}
