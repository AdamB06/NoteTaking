package client;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public class MnemonicCreator {

    public void initialize(Button editSave, Button add, Button delete, Button refresh) {

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

    private Mnemonic create(Button b, KeyCode primary){
        KeyCombination kc = new KeyCodeCombination(primary, KeyCombination.ALT_ANY);
        return new Mnemonic(b, kc);
    }
}
