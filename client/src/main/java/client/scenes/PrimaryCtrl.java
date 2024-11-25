package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class PrimaryCtrl {
    private Stage primaryStage;

    private Scene homeScene;

    public void init(Stage primaryStage, Pair<HomeCtrl, Parent> home) {
        this.primaryStage = primaryStage;
        this.homeScene = new Scene(home.getValue());
        showHome();
        primaryStage.show();
    }

    public void showHome() {
        primaryStage.setTitle("NetNote");
        primaryStage.setScene(homeScene);
    }

}
