package api.allegro.controller;

import api.allegro.app.App;
import api.allegro.bean.CategoryBean;
import api.allegro.bean.CategoryParamBean;
import api.allegro.bean.CategoryParamValueBean;
import api.allegro.entity.OfferEntity;
import api.allegro.exception.AllegroBadRequestException;
import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.service.AuthorizationService;
import api.allegro.service.CategoryService;
import api.allegro.service.OfferService;
import api.allegro.service.StatsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DashboardController {

    public ObservableMap<String, List<String>> filters = FXCollections.observableHashMap();
    public ObservableList<CategoryBean> categories = FXCollections.observableArrayList();
    @FXML
    private HBox categoryParamNodesContainer;
    @FXML
    private ChoiceBox<CategoryBean> categoryChoiceBox;
    @FXML
    private Label syncLbl;
    @FXML
    private Label offerCounterLbl;

    private CategoryService catSrv;
    private StatsService statsSrv;
    private OfferService offerSrv;

    @FXML
    private void initialize() throws IOException {
        // TODO: should be inside constructor (?)
        AuthorizationService authSrv = new AuthorizationService("allegro-pu");
        catSrv = new CategoryService("allegro-pu", authSrv.getAccessToken());
        statsSrv = new StatsService("allegro-pu", authSrv.getAccessToken());
        offerSrv = new OfferService("allegro-pu", authSrv.getAccessToken());

        loadRootCategory();
        loadOffers();
        getStats();

        // uncomment for real time offer matching
        /*filters.addListener(new MapChangeListener<String, List<String>>() {
            @Override
            public void onChanged(Change<? extends String, ? extends List<String>> change) {
                offerSrv.matchOffers(filters, categoryChoiceBox.getSelectionModel().getSelectedItem().getId());
            }
        });*/

        categoryChoiceBox.setItems(categories);
        categoryChoiceBox.setOnAction(this::choiceBoxSelectEventHandler);
    }

    private void choiceBoxSelectEventHandler(ActionEvent actionEvent) {
        ChoiceBox<CategoryBean> target = (ChoiceBox<CategoryBean>) actionEvent.getTarget();
        CategoryBean c = target.getSelectionModel().getSelectedItem();
        try {
            loadCategory(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showPreviewWnd() {
        TextArea textArea = new TextArea();

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(textArea);

        Scene secondScene = new Scene(secondaryLayout, 640, 480);
        Stage newWindow = new Stage();

        newWindow.setTitle("Matching offers");
        newWindow.setScene(secondScene);

        // position of second window, related to primary window.
        //newWindow.setX(primaryStage.getX() + 200);
        //newWindow.setY(primaryStage.getY() + 100);

        offerSrv.matchOffers(filters, categoryChoiceBox.getSelectionModel().getSelectedItem().getId());
        App.getStage().setUserData(offerSrv.getMatchingOffers());

        for (OfferEntity matchedOffer : offerSrv.getMatchingOffers()) {
            // TODO: use StringBuilder
            textArea.appendText(matchedOffer.getId() + " " + matchedOffer.getName() + "\n");
        }

        newWindow.show();
    }

    @FXML
    public void loadRootCategory() throws IOException {
        try {
            loadCategory(catSrv.getMainCategory());
        } catch (AllegroUnauthorizedException e) {
            Alert alert = new Alert(AlertType.WARNING, "Session expired. Please connect app.");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                App.setRoot("authorization");
            }
        } catch (AllegroNotFoundException | IOException | InterruptedException e) {
            showAlertWindow(AlertType.WARNING, "IO/Network error. Please try again.");
        }
    }

    @FXML
    public void loadOffers() {
        offerSrv.loadAllOffers();
    }

    @FXML
    public void getStats() {
        syncLbl.setText(String.valueOf(statsSrv.getUpdateDate()));
        offerCounterLbl.setText(String.valueOf(statsSrv.getOfferCount()));
    }

    @FXML
    public void clearFilters() throws IOException {
        loadRootCategory();
    }

    @FXML
    public void loadCategory(CategoryBean selected) throws IOException {
        System.out.println(Thread.currentThread().getStackTrace()[2].getMethodName());
        categoryChoiceBox.setOnAction(null);
        try {

            filters.clear();
            categories.clear();

            if (selected.getParent() != null) {
                CategoryBean parent = selected.getParent();
                categories.add(parent);
            }

            /*if (selected != catSrv.getMainCategory()) {
                categories.add(selected);
            }*/

            categories.add(selected);
            categoryChoiceBox.getSelectionModel().select(selected);

            List<CategoryBean> list = catSrv.getCategoryList(selected);
            List<CategoryParamBean> paramList = selected.getParameters();

            categories.addAll(list);
            categoryParamNodesContainer.getChildren().clear();
            for (CategoryParamBean param : paramList) {
                VBox vBox = new VBox();
                vBox.getChildren().add(new Label(param.getName()));
                if (param.getType().equals("dictionary")) {
                    ChoiceBox<CategoryParamValueBean> choiceBox = new ChoiceBox<>();
                    choiceBox.setItems(FXCollections.observableList(param.getValues()));
                    choiceBox.setId(param.getId());
                    choiceBox.setOnAction(event -> {
                        CategoryParamValueBean cp = choiceBox.getSelectionModel().getSelectedItem();
                        filters.put(choiceBox.getId(), Collections.singletonList(cp.getId()));
                    });
                    vBox.getChildren().add(choiceBox);
                } else {
                    TextField tf = new TextField();
                    tf.setId(param.getId());
                    tf.setOnAction(event -> {
                        // TODO: allow specific type (int for integer, float for flat)
                        filters.put(tf.getId(), Collections.singletonList(tf.getText()));
                    });
                    vBox.getChildren().add(tf);
                }
                categoryParamNodesContainer.getChildren().add(vBox);
            }
            categoryChoiceBox.setOnAction(this::choiceBoxSelectEventHandler);
        } catch (AllegroUnauthorizedException e) {
            Alert alert = new Alert(AlertType.WARNING, "Session expired. Please connect app.");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                App.setRoot("authorization");

            }
        } catch (AllegroNotFoundException | IOException | InterruptedException e) {
            showAlertWindow(AlertType.WARNING, "IO/Network error. Please try again.");
        }
    }

    @FXML
    public void synchronize() throws IOException {
        try {
            offerSrv.synchronize();
            loadRootCategory();
            getStats();
        } catch (AllegroUnauthorizedException e) {
            Alert alert = new Alert(AlertType.WARNING, "Session expired. Please connect app.");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                App.setRoot("authorization");
            }
        } catch (IOException | InterruptedException e) {
            showAlertWindow(AlertType.WARNING, "IO/Network error. Please try again.");
        } catch (AllegroBadRequestException e) {
            showAlertWindow(AlertType.WARNING, "Bad request. Please try again.");
        }
    }

    @FXML
    public void showAlertWindow(AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    public void editOffers() throws IOException {
        App.setRoot("edit");
    }

}
