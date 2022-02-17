package api.allegro.bean;

import api.allegro.enums.CommandTypeEnum;

import java.util.UUID;

public class CommandBean {
    private final String id;
    private final CommandTypeEnum type;
    private final long timestamp;

    public CommandBean(CommandTypeEnum type) {
        this.type = type;
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis() / 1000L;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public CommandTypeEnum getType() {
        return type;
    }
}
