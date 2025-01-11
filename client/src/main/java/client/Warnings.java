package client;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class Warnings {

    /**
     * Gives a prompt to the user asking for confirmation
     *
     * @param title  the title
     * @param header the header
     * @return true if the OK button is clicked, false otherwise
     */
    public boolean askOkCancel(String title, String header) {

        Alert alert = alertBuilder(title, "", header, AlertType.CONFIRMATION);

        Optional<ButtonType> result = alert.showAndWait();
        return result
                .map(buttonType -> buttonType.equals(ButtonType.OK))
                .orElse(false);
    }

    /**
     * Informative prompt, to give a confirmation of an action
     *
     * @param title   the title
     * @param content the content
     * @param header  the header
     */
    public void inform(String title, String content, String header) {
        Alert alert = alertBuilder(title, content, header, AlertType.INFORMATION);
        alert.showAndWait();
    }

    /**
     * Warning prompt, to give a warning for a performed action
     *
     * @param title   the title
     * @param content the content
     * @param header  the header
     */
    public void warn(String title, String content, String header) {
        Alert alert = alertBuilder(title, content, header, AlertType.WARNING);
        alert.showAndWait();
    }

    /**
     * Error prompt, to give an error whenever something fails to inform the user about the error
     *
     * @param title   the title
     * @param content the content
     * @param header  the header
     */
    public void error(String title, String content, String header) {
        Alert alert = alertBuilder(title, content, header, AlertType.ERROR);
        alert.showAndWait();
    }

    /**
     * Informative prompt, to give a confirmation of an action
     *
     * @param title   the title
     * @param content the content
     * @param header  the header
     * @param e       the exception that has occurred
     */
    public void exception(String title, String content,
                          String header, Exception e) {

        Alert alert = alertBuilder(title, content, header, AlertType.ERROR);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label l = new Label("The exception was: ");

        TextArea area = new TextArea(exceptionText);
        area.setEditable(false);
        area.setWrapText(true);
        area.setFocusTraversable(false);

        area.setMaxWidth(1000d);
        area.setMaxHeight(1000d);
        GridPane.setVgrow(area, Priority.ALWAYS);
        GridPane.setHgrow(area, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(l, 0, 0);
        expContent.add(area, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    /**
     * Creates an Alert class item with the following parameters
     *
     * @param title   the title
     * @param content the content
     * @param header  the header
     * @param type    the alert type
     * @return the constructed Alert instance with the given arguments
     */
    private Alert alertBuilder(String title, String content,
                               String header, AlertType type) {
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(header);

        return alert;
    }
}
