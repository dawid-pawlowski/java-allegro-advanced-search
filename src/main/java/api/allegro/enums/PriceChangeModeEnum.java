package api.allegro.enums;

public enum PriceChangeModeEnum {
    FIXED_PRICE("Fixed price"),
    INCREASE_PRICE("Increase price by"),
    DECREASE_PRICE("Decrease price by"),
    INCREASE_PERCENTAGE("Increase by percentage"),
    DECREASE_PERCENTAGE("Decrease by percentage");

    private final String displayName;

    PriceChangeModeEnum(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
