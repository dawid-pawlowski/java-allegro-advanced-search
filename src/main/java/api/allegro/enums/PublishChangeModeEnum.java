package api.allegro.enums;

public enum PublishChangeModeEnum {
    ACTIVATE("Published"),
    END("Unpublished");

    private final String displayName;

    PublishChangeModeEnum(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
