package api.allegro.controller;


import api.allegro.app.App;
import api.allegro.converter.RemoveHtmlTagsConverter;
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
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.List;

public class EditController {

    // TODO: use enums for change types
    public ObservableList<String> changeTypes = FXCollections.observableArrayList();
    @FXML
    private Button processBtn;
    @FXML
    private TextArea newPriceTxt;
    @FXML
    private TextArea newQuantityTxt;
    @FXML
    private ChoiceBox<String> priceModeChoiceBox;
    private ObservableList<String> priceChangeModes = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> quantityModeChoiceBox;
    private ObservableList<String> quantityChangeModes = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> publishChoiceBox;
    private ObservableList<String> publishModes = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> shippingRateChoiceBox;
    private ObservableList<String> shippingRatesList = FXCollections.observableList();

    private OfferService offerSrv;

    @FXML
    private void initialize() {

        // TODO: should be inside constructor (?)
        AuthorizationService authSrv = new AuthorizationService("allegro-pu");
        offerSrv = new OfferService("allegro-pu", authSrv.getAccessToken());

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



    }

    @FXML
    public void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    public void process() {
        if (offerSrv.batchOfferDescriptionChange((List<OfferEntity>) App.getStage().getUserData(), oldValueTxt.getText(), newValueTxt.getText())) {
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
