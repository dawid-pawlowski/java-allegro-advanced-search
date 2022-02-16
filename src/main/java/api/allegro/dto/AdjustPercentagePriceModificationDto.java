package api.allegro.dto;

public class AdjustPercentagePriceModificationDto extends ModificationDto {
    private String type;
    private String percentage;

    public AdjustPercentagePriceModificationDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
