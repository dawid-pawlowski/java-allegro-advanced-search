package api.allegro.controller;


import api.allegro.app.App;
import api.allegro.bean.CommandBean;
import api.allegro.bean.ShippingRateBean;
import api.allegro.entity.OfferEntity;
import api.allegro.enums.PriceChangeModeEnum;
import api.allegro.enums.PublishChangeModeEnum;
import api.allegro.enums.QuantityChangeModeEnum;
import api.allegro.exception.AllegroBadRequestException;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditController {
    @FXML
    private TextField newPriceTxt;
    @FXML
    private TextField newQuantityTxt;
    @FXML
    private ChoiceBox<PriceChangeModeEnum> priceModeChoiceBox;
    @FXML
    private ChoiceBox<QuantityChangeModeEnum> quantityModeChoiceBox;
    @FXML
    private ChoiceBox<PublishChangeModeEnum> publishChoiceBox;
    @FXML
    private ChoiceBox<ShippingRateBean> shippingRateChoiceBox;
    private AuthorizationService authorizationService;
    private OfferService offerService;
    private ShippingRateService shippingRateService;

    private List<CommandBean> commands = new ArrayList<>();

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

        shippingRateChoiceBox.getItems().setAll(getShippingRates());
        priceModeChoiceBox.getItems().setAll(PriceChangeModeEnum.values());
        publishChoiceBox.getItems().setAll(PublishChangeModeEnum.values());
        quantityModeChoiceBox.getItems().setAll(QuantityChangeModeEnum.values());
    }

    public List<ShippingRateBean> getShippingRates() throws IOException {
        try {
            return shippingRateService.getShippingRates();
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

        return Collections.emptyList();
    }

    @FXML
    public void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    public void process() throws AllegroUnauthorizedException, IOException, InterruptedException, AllegroBadRequestException {
        if (priceModeChoiceBox.getValue() != null && !newPriceTxt.getText().trim().isEmpty()) {
            commands.add(offerService.batchOfferPriceChange((List<OfferEntity>) App.getStage().getUserData(), priceModeChoiceBox.getValue(), newPriceTxt.getText()));
        }

        if (quantityModeChoiceBox.getValue() != null && !newQuantityTxt.getText().trim().isEmpty()) {
            commands.add(offerService.batchOfferQuantityChange((List<OfferEntity>) App.getStage().getUserData(), quantityModeChoiceBox.getValue(), newQuantityTxt.getText()));
        }

        if (publishChoiceBox.getValue() != null) {
            commands.add(offerService.batchOfferPublishChange((List<OfferEntity>) App.getStage().getUserData(), publishChoiceBox.getValue()));
        }

        System.out.println(shippingRateChoiceBox.getValue());
    }

    @FXML
    public void showAlertWindow(AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
