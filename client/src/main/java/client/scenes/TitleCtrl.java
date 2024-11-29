package client.scenes;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TitleCtrl {
    private final TextField titleField;
    private final Button editButton;

    public TitleCtrl(TextField titleField, Button editButton){
        this.titleField = titleField;
        this.editButton = editButton;
        initialize();
    }

    public void initialize(){
        editButton.setOnAction(actionEvent -> {
            titleField.setEditable(true); //Makes sure you can edit
            titleField.requestFocus(); //Focuses on text field
            titleField.selectAll(); //Selects everything in the text field
        });
    }

    public void click(){

    }

}
