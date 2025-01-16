package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.EditCollectionCtrl;
import client.scenes.HomePageCtrl;
import client.scenes.PrimaryCtrl;
import com.google.inject.Injector;

import client.utils.ServerUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private WebSocketClient webSocketClient;

    /**
     * PSVM method
     * @param args PSVM method arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Start the client
     * @param primaryStage Stage for client
     * @throws Exception if server doesn't work well
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        var serverUtils = INJECTOR.getInstance(ServerUtils.class);
        if (!serverUtils.isServerAvailable()) {
            var msg = "Server needs to be started before the client, " +
                    "but it does not seem to be available. Shutting down.";
            System.err.println(msg);
            Platform.exit();
            return;
        }
        var overview = FXML.load(HomePageCtrl.class, "client", "scenes", "NetNoteScene.fxml");
        var editCollection = FXML.load(EditCollectionCtrl.class, "client", "scenes", "EditCollection.fxml");
        var mainCtrl = INJECTOR.getInstance(PrimaryCtrl.class);
        mainCtrl.init(primaryStage, overview, editCollection);

        // Create and connect to WebSocket server
        webSocketClient = INJECTOR.getInstance(WebSocketClient.class);
        webSocketClient.connect();
    }

    /**
     * Stop the client
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}