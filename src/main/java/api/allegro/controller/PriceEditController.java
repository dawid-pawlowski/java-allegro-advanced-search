package api.allegro.controller;


import api.allegro.app.App;
import api.allegro.entity.OfferEntity;
import api.allegro.filter.DecimalFilter;
import api.allegro.filter.IntegerFilter;
import api.allegro.service.AuthorizationService;
import api.allegro.service.OfferService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.List;

public class PriceEditController {

    // TODO: use enums for change types
    public ObservableList<String> changeTypes = FXCollections.observableArrayList();
    @FXML
    private Button processBtn;
    @FXML
    private TextField valueTextField;
    @FXML
    private ChoiceBox<String> changeTypeChoiceBox;
    private OfferService offerSrv;

    @FXML
    private void initialize() {
        // TODO: should be inside constructor (?)
        AuthorizationService authSrv = new AuthorizationService("allegro-pu");
        offerSrv = new OfferService("allegro-pu", authSrv.getAccessToken());

        changeTypes.addAll("FIXED AMOUNT", "INCREMENT BY AMOUNT", "DECREMENT BY AMOUNT", "INCREASE BY PERCENTAGE", "DECREASE BY PERCENTAGE");
        changeTypeChoiceBox.setItems(changeTypes);

        TextFormatter<Double> formatter = new TextFormatter<Double>(
                new DoubleStringConverter(),
                null,
                new DecimalFilter()
        );

        valueTextField.setTextFormatter(formatter);
    }

    @FXML
    public void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    public void process() {
        if (offerSrv.batchOfferPriceChange((List<OfferEntity>) App.getStage().getUserData(), changeTypeChoiceBox.getSelectionModel().getSelectedItem(), valueTextField.getText())) {
            showAlertWindow(AlertType.INFORMATION, "Success");
        } else {
            showAlertWindow(AlertType.ERROR, "Error");
        }
    }

    @FXML
    public void showAlertWindow(AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
