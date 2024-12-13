package client;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageController {
    private ResourceBundle bundle;

    /**
     * loads a defined language for later use
     * @param language the language abbreviation
     */
    public void loadLanguage(String language){
        Locale locale = new Locale(language);
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    /**
     * @return edit button text
     */
    public String getEditText(){
        return bundle.getString("editButton.text");
    }

    /**
     * @return save button text
     */
    public String getSaveText(){
        return bundle.getString("saveButton.text");
    }
}
