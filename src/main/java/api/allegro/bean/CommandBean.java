package api.allegro.bean;

import api.allegro.enums.CommandTypeEnum;

import java.util.UUID;

public class CommandBean {
    private final String id;
    private final CommandTypeEnum type;
    private final long timestamp;
    private int total;
    private int failed;
    private int success;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public boolean isSuccessful() {
        return success == total;
    }
}
