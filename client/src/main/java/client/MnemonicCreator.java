package client;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.input.Mnemonic;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public class MnemonicCreator {

    private final Modifier controlKey = KeyCombination.ALT_ANY;

    /**
     * Initializes all given buttons and assigns them a mnemonic
     * @param editSave edit and save button
     * @param add add button
     * @param delete delete button
     * @param refresh refresh button
     */
    public void initialize(Button editSave, Button add,
                           Button delete, Button refresh) {
        Optional<Window> optionalWindow = Stage.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst();

        Window open;
        open = optionalWindow.orElse(null);

        if(open == null){
            System.out.println("window does not exist, returning to program");
            return;
        }

        Scene scene = open.getScene();

        Mnemonic editMN = create(editSave, KeyCode.E);
        Mnemonic addMN = create(add, KeyCode.A);
        Mnemonic deleteMN = create(delete, KeyCode.D);
        Mnemonic refreshMN = create(refresh, KeyCode.R);

        scene.addMnemonic(editMN);
        scene.addMnemonic(addMN);
        scene.addMnemonic(deleteMN);
        scene.addMnemonic(refreshMN);
    }

    /**
     * Creates a specific mnemonic
     * @param n the Node to create the mnemonic for
     * @param primary the primary key for the button name (secondary is the alt key by default)
     * @return the created mnemonic
     */
    private Mnemonic create(Node n, KeyCode primary){
        KeyCombination kc = new KeyCodeCombination(primary, controlKey);
        return new Mnemonic(n, kc);
    }
}
