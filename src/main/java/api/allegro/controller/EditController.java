package api.allegro.controller;


import api.allegro.app.App;
import api.allegro.bean.ShippingRateBean;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.filter.DecimalFilter;
import api.allegro.filter.IntegerFilter;
import api.allegro.service.AuthorizationService;
import api.allegro.service.OfferService;
import api.allegro.service.ShippingRateService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;

public class EditController {

    private final ObservableList<String> priceChangeModes = FXCollections.observableArrayList();
    private final ObservableList<String> quantityChangeModes = FXCollections.observableArrayList();
    private final ObservableList<String> publishModes = FXCollections.observableArrayList();
    private final ObservableList<ShippingRateBean> shippingRatesList = FXCollections.observableArrayList();
    // TODO: use enums for change types
    public ObservableList<String> changeTypes = FXCollections.observableArrayList();
    @FXML
    private TextField newPriceTxt;
    @FXML
    private TextField newQuantityTxt;
    @FXML
    private ChoiceBox<String> priceModeChoiceBox;
    @FXML
    private ChoiceBox<String> quantityModeChoiceBox;
    @FXML
    private ChoiceBox<String> publishChoiceBox;
    @FXML
    private ChoiceBox<ShippingRateBean> shippingRateChoiceBox;
    private AuthorizationService authorizationService;
    private OfferService offerService;
    private ShippingRateService shippingRateService;

    @FXML
    private void initialize() throws IOException {

        // TODO: should be inside constructor (?)
        authorizationService = new AuthorizationService("allegro-pu");
        offerService = new OfferService("allegro-pu", authorizationService.getAccessToken());
        shippingRateService = new ShippingRateService(authorizationService.getAccessToken());

        // TODO: use StringConverter instead of an UnaryOperator
        newPriceTxt.setTextFormatter(new TextFormatter<Float>(
                new FloatStringConverter(),
                null,
                new DecimalFilter()
        ));

        // TODO: use StringConverter instead of an UnaryOperator
        newQuantityTxt.setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(),
                null,
                new IntegerFilter()
        ));

        shippingRateChoiceBox.setItems(shippingRatesList);

        getShippingRates();
    }

    public void getShippingRates() throws IOException {
        try {
            shippingRatesList.addAll(shippingRateService.getShippingRates());
        } catch (AllegroUnauthorizedException e) {
            Alert alert = new Alert(AlertType.WARNING, "Session expired. Please connect app.");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                App.setRoot("authorization");
            }
        } catch (IOException | InterruptedException e) {
            showAlertWindow(AlertType.WARNING, "IO/Network error. Please try again.");
        }
    }

    @FXML
    public void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    public void process() {
    }

    @FXML
    public void showAlertWindow(AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
