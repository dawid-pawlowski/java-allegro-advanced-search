package api.allegro.controller;

import api.allegro.app.App;
import api.allegro.bean.CommandBean;
import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.service.AuthorizationService;
import api.allegro.service.CommandService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class SummaryController {

    private CommandService commandService;

    @FXML
    private TableView<CommandBean> commandsTableView;

    @FXML
    public void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void initialize() throws AllegroUnauthorizedException, IOException, AllegroNotFoundException, InterruptedException {
        App.getStage().setTitle("Summary");

        AuthorizationService authorizationService = new AuthorizationService("allegro-pu");
        commandService = new CommandService(authorizationService.getAccessToken());

        TableColumn<CommandBean, String> dateColumn = new TableColumn<>("Date:");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        TableColumn<CommandBean, String> typeColumn = new TableColumn<>("Type:");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<CommandBean, Integer> totalColumn = new TableColumn<>("Total:");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        commandsTableView.getColumns().add(dateColumn);
        commandsTableView.getColumns().add(typeColumn);
        commandsTableView.getColumns().add(totalColumn);

        refreshCommands();
    }

    @FXML
    public void refreshCommands() throws AllegroUnauthorizedException, IOException, AllegroNotFoundException, InterruptedException {
        commandsTableView.getItems().setAll(commandService.getAllCommands());
    }

}
