package api.allegro.service;

import api.allegro.bean.CommandBean;
import api.allegro.enums.CommandTypeEnum;
import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.resource.CommandResource;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandService {
    private final CommandResource resource;
    private static final List<CommandBean> commands = new ArrayList<>();

    public CommandService(String accessToken) {
        resource = new CommandResource(accessToken);
    }

    public static CommandBean createCommand(CommandTypeEnum type) {
        CommandBean commandBean = new CommandBean(type);
        commands.add(commandBean);
        return commandBean;
    }

    public List<CommandBean> getAllCommands() throws AllegroUnauthorizedException, IOException, AllegroNotFoundException, InterruptedException {
        for (CommandBean command : commands) {
            updateCommand(command);
        }
        return commands;
    }

    private void updateCommand(CommandBean commandBean) throws AllegroUnauthorizedException, IOException, AllegroNotFoundException, InterruptedException {
        // TODO: check if command is already finished
        JSONObject response;

        switch (commandBean.getType()) {
            case PRICE_CHANGE -> response = resource.offerPriceChangeSummary(commandBean.getId());
            case PUBLICATION_CHANGE -> response = resource.offerPublicationSummary(commandBean.getId());
            case QUANTITY_CHANGE -> response = resource.offerQuantityChangeSummary(commandBean.getId());
            case OFFER_MODIFICATION -> response = resource.offerModificationChangeSummary(commandBean.getId());
            default -> response = null;
        }

        commandBean.setTotal(Integer.parseInt(new JSONObject(response.get("taskCount")).get("total").toString()));
        commandBean.setSuccess(Integer.parseInt(new JSONObject(response.get("taskCount")).get("success").toString()));
        commandBean.setFailed(Integer.parseInt(new JSONObject(response.get("taskCount")).get("failed").toString()));
    }
}
