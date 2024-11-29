package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class PrimaryCtrl {
    private Stage primaryStage;
    private Scene homeScene;

    /**
     * Initialize the UI controller
     * @param primaryStage Variable for the stage
     * @param home Pair of UI and Controller
     */
    public void init(Stage primaryStage, Pair<HomePageCtrl, Parent> home) {
        this.primaryStage = primaryStage;
        this.homeScene = new Scene(home.getValue());
        showHome();
        primaryStage.show();
    }

    /**
     * Display the HomeScreen UI
     */
    public void showHome() {
        primaryStage.setTitle("NetNote");
        primaryStage.setScene(homeScene);
    }

}
