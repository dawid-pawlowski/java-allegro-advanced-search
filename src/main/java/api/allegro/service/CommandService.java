package api.allegro.service;

import api.allegro.bean.CommandBean;
import api.allegro.resource.CommandResource;

import java.util.ArrayList;
import java.util.List;

public class CommandService {
    private final CommandResource resource;
    private static final List<CommandBean> commands = new ArrayList<>();

    public CommandService(String accessToken) {
        resource = new CommandResource(accessToken);
    }

    public static CommandBean createCommand() {
        CommandBean commandBean = new CommandBean();
        commands.add(commandBean);
        return commandBean;
    }
}
