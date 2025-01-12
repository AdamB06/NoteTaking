package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class PrimaryCtrl {
    private Stage primaryStage;
    private Scene homeScene;
    private Scene editScene;
    private EditCollectionCtrl editCtrl;

    /**
     * Initialize the UI controller
     * @param primaryStage Variable for the stage
     * @param home Pair of UI and Controller
     */
    public void init(Stage primaryStage, Pair<HomePageCtrl, Parent> home, Pair<EditCollectionCtrl, Parent> edit) {
        this.primaryStage = primaryStage;
        this.homeScene = new Scene(home.getValue());
        this.editScene = new Scene(edit.getValue());
        this.editCtrl = edit.getKey();
        this.primaryStage.setOnCloseRequest(event -> {
            home.getKey().forceSaveBeforeClose();
        });
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

    /**
     * Displays the Edit Collection UI
     * If the key "ESCAPE" is pressed then we exit the scene
     * and should go back to the main one
     */
    public void showEdit(){
        primaryStage.setTitle("Edit Collection");
        primaryStage.setScene(editScene);
        editScene.setOnKeyPressed(event -> editCtrl.keyPressed(event));
    }
}
