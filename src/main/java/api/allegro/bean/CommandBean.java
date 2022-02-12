package api.allegro.bean;

import java.util.UUID;

public class CommandBean {
    private final UUID id;

    public CommandBean() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
}
