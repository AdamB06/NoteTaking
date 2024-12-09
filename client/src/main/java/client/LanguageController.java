package client;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageController {
    private static final List<String> languages = List.of("en", "nl", "es");

    private ResourceBundle bundle;

    public void loadLanguage(int index){
        Locale locale = new Locale(languages.get(index));
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    public String getEditText(){
        return bundle.getString("editButton.text");
    }

    public String getSaveText(){
        return bundle.getString("saveButton.text");
    }
}
