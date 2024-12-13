package client;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageController {
    private ResourceBundle bundle;

    public void loadLanguage(String language){
        Locale locale = new Locale(language);
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    public String getEditText(){
        return bundle.getString("editButton.text");
    }

    public String getSaveText(){
        return bundle.getString("saveButton.text");
    }
}
