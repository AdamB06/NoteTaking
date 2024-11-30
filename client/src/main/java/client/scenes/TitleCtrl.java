package client.scenes;

import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TitleCtrl {
    @FXML
    private TextField titleField;
    @FXML
    private Button editButton;
    private PrimaryCtrl pc;

    @Inject
    public TitleCtrl(TextField titleField, Button editButton, PrimaryCtrl pc) {
        this.titleField = titleField;
        this.editButton = editButton;
        this.pc = pc;

        initialize();
    }

    @FXML
    public void initialize() {
        editButton.setOnAction(actionEvent -> {
            titleField.setEditable(true); //Makes sure you can edit
            titleField.requestFocus(); //Focuses on text field
            titleField.selectAll(); //Selects everything in the text field
        });


        titleField.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus) {
                pc.editTitle(titleField.getText());
                titleField.setEditable(false); // Disable editing after saving
            }
        });
    }
}


