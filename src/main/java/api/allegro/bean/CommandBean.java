package api.allegro.bean;

import java.util.UUID;

public class CommandBean {
    private final String id;

    public CommandBean() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
