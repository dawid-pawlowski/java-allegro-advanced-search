package api.allegro.dto;

public class OfferChangeDto {

    private OfferCriteriaDto offerCriteria;
    private ModificationDto modification;
    private PublicationModificationDto publication;

    public OfferChangeDto() {
    }

    public OfferCriteriaDto getOfferCriteria() {
        return offerCriteria;
    }

    public void setOfferCriteria(OfferCriteriaDto offerCriteria) {
        this.offerCriteria = offerCriteria;
    }

    public ModificationDto getModification() {
        return modification;
    }

    public void setModification(ModificationDto modification) {
        this.modification = modification;
    }

    public PublicationModificationDto getPublication() {
        return publication;
    }

    public void setPublication(PublicationModificationDto publication) {
        this.publication = publication;
    }
}
