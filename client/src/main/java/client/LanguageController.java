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

    /**
     * @return collection label text
     */
    public String getCollectionsLabelText(){
        return bundle.getString("collectionsLabel.text");
    }

    /**
     * @return preview label text
     */
    public String getPreviewLabelText(){
        return bundle.getString("previewLabel.text");
    }

    /**
     * @return search box text
     */
    public String getSearchBoxText(){
        return bundle.getString("searchBox.text");
    }

    /**
     * @return search box text
     */
    public String getTitleFieldText(){
        return bundle.getString("titleField.text");
    }

    /**
     * @return notes body area text
     */
    public String getNotesBodyAreaText(){
        return bundle.getString("notesBodyArea.text");
    }

    /**
     *
     * @return filter button text
     */
    public String getFilterButtonText(){return bundle.getString("filterButton.text");}


    /**
     *
     * @return clear filter button text
     */
    public String getClearFilterButtonText(){return bundle.getString("clearFilters.text");}

}
