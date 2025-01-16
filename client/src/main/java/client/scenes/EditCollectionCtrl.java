package client.scenes;

import client.MyModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import commons.Collection;
import client.services.*;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.*;

public class EditCollectionCtrl implements Initializable {
    private PrimaryCtrl primaryCtrl;
    //TODO The Labels to be translated
    @FXML
    private TextField title;
    @FXML
    private TextField serverUrl;
    @FXML
    private TextField collection;
    @FXML
    private TextField status;
    @FXML
    private Button makeDefault;
    @FXML
    private ListView<Collection> collectionsListView;
    @FXML
    private Button addCollection;
    @FXML
    private Button removeCollection;


    private final CollectionService collectionService;
    private Injector injector;

    private final SimpleObjectProperty<Collection> currentCollection = new SimpleObjectProperty<>();

    @Inject
    public EditCollectionCtrl(PrimaryCtrl primaryCtrl) {
        this.primaryCtrl = primaryCtrl;
        injector = Guice.createInjector(new MyModule());
        collectionService = injector.getInstance(CollectionService.class);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO Continue...
        refreshCollectionsInternal();
        disableBinding();
    }

    /**
     * We properly handle the creation of a Collection after clicking
     * @param event the clicking of the delete button
     */
    @FXML
    private void handleAddCollection(ActionEvent event) {
        Collection createdCollection = collectionService.createCollection();
        if(createdCollection != null){
            collectionsListView.getItems().add(createdCollection);
            collectionsListView.getSelectionModel().select(createdCollection);
            System.out.println("Collection created with ID: " + createdCollection.getId());
        }
        //TODO are mnemonics to be included here? something for WEEK8
    }

    /**
     * We properly handle the deletion of a Collection after clicking
     * @param event the clicking of the delete button
     */
    @FXML
    public void handleDeleteCollection(ActionEvent event) {
        Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if(selectedCollection != null){
            String status = collectionService.deleteCollection(selectedCollection);
            if("Successful".equals(status)){
                refreshCollectionsInternal();
            }
            //TODO are mnemonics to be included here? something for WEEK8
        }
    }

    //TODO saveChangesButton

    //TODO makeCollectionDefaultButton

    /**
     * Refresh the collections in the ListView
     */
    private void refreshCollectionsInternal() {
        Platform.runLater(() -> {
            collectionService.refreshCollections();
            List<Collection> collections = collectionService.getCollections();
            if(collectionsListView != null) {
                Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
                collectionsListView.getItems().clear();
                collectionsListView.getItems().addAll(collections);

                //Preserve the selection if possible
                if(selectedCollection != null && collections.contains(selectedCollection)){
                    collectionsListView.getSelectionModel().select(selectedCollection);
                }
            }
            else{
                System.err.println("ListView not initialized");
            }
        });

    }

    /**
     * To prevent user interaction with UI elements when no Collection is selected. This ensures that:
     * - Users can't edit details of a collection when there isn't one selected.
     * - UI behavior stays consistent with the application state, improving user experience and avoiding potential errors.
     */
    private void disableBinding() {
        title.disableProperty().bind(Bindings.createBooleanBinding(
                () -> currentCollection.get() == null,
                currentCollection
        ));
        serverUrl.disableProperty().bind(Bindings.createBooleanBinding(
                () -> currentCollection.get() == null,
                currentCollection
        ));
        collection.disableProperty().bind(Bindings.createBooleanBinding(
                () -> currentCollection.get() == null,
                currentCollection
        ));
        status.disableProperty().bind(Bindings.createBooleanBinding(
                () -> currentCollection.get() == null,
                currentCollection
        ));
        makeDefault.disableProperty().bind(Bindings.createBooleanBinding(
                () -> currentCollection.get() == null,
                currentCollection
        ));
    }

    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            cancel();
        }
    }

    public void cancel() {
        clearFields();
        primaryCtrl.showHome();
    }

    public void clearFields(){
        collectionsListView.getItems().clear();
        title.clear();
        //TODO serverUrl attribute
        serverUrl.clear();

        //TODO collection attribute
        collection.clear();

        status.clear();

        currentCollection.set(null);
        collectionsListView.getItems().clear();
    }
}
