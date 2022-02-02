package api.allegro.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class App extends Application {

    private static Scene SCENE;
    private static Clipboard CLIPBOARD;
    private static Stage STAGE;
    private static Properties PROPERTIES;

    public static void setRoot(String fxml) throws IOException {
        SCENE.setRoot(loadFXML(fxml));
    }

    public static Stage getStage() {
        return STAGE;
    }

    public static Clipboard getClipboard() {
        return CLIPBOARD;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader(App.class.getResource("app.properties").getFile());
        PROPERTIES = new Properties();
        PROPERTIES.load(reader);

        launch();
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }

    @Override
    public void start(Stage stage) throws IOException {
        STAGE = stage;
        CLIPBOARD = Clipboard.getSystemClipboard();
        SCENE = new Scene(loadFXML("authorization"), 640, 480);
        stage.setScene(SCENE);
        stage.show();
    }

}
