package api.allegro.enums;

public enum QuantityChangeModeEnum {
    FIXED("Fixed"),
    GAIN("Increase / decrease");

    private final String displayName;

    QuantityChangeModeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
