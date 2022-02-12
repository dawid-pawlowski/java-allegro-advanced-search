package api.allegro.service;

import api.allegro.bean.CommandBean;
import api.allegro.resource.CommandResource;

public class CommandService {
    private final CommandResource resource;

    public CommandService(String accessToken) {
        resource = new CommandResource(accessToken);
    }

    public CommandBean getCommand() {
        return resource.getCommand();
    }
}
