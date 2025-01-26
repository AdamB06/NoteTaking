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
        var warnings = INJECTOR.getInstance(Warnings.class);
        var lc = INJECTOR.getInstance(LanguageController.class);
        lc.loadLanguage(ClientConfig.loadConfig().getPreferredLanguage());
        boolean reconnect = true;

        while (reconnect) {
            if (serverUtils.isServerAvailable()) {
                reconnect = false;
            } else {
                warnings = new Warnings();
                reconnect = warnings.askOkCancel(
                        lc.getByTag("serverNotFound.text"),
                        lc.getByTag("serverNotFound.message"),
                        lc
                );

                if (reconnect) {
                    System.out.println("Attempting to reconnect...");
                } else {
                    System.out.println("Server unavailable. Shutting down.");
                    Platform.exit();
                    return;
                }
            }
        }
        var overview = FXML.load(HomePageCtrl.class, "client", "scenes", "NetNoteScene.fxml");
        var editCollection = FXML.load(EditCollectionCtrl.class,
                "client", "scenes", "EditCollection.fxml");
        var mainCtrl = INJECTOR.getInstance(PrimaryCtrl.class);
        mainCtrl.init(primaryStage, overview, editCollection);
    }
}