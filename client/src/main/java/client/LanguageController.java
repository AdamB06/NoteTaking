package client;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageController {
    private ResourceBundle bundle;

    /**
     * @return the language bundle
     */
    public String getFilterButtonText(){return bundle.getString("filterButton.text");}

    /**
     * loads a defined language for later use
     * @param language the language abbreviation
     */
    public void loadLanguage(String language){
        Locale locale = new Locale(language);
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }
    /**
     * @return clear filter button text
     **/
    public String getClearFilterButtonText(){return bundle.getString("clearFilters.text");}

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
     * @return add button testx
     */
    public String getAddButtonText(){
        return bundle.getString("addButton.text");
    }

    /**
     * @return delete button text
     */
    public String getDeleteButtonText(){
        return bundle.getString("deleteButton.text");
    }

    /**
     * @return refresh button text
     */
    public String getRefreshButtonText(){
        return bundle.getString("refreshButton.text");
    }

    /**
     * @param t the tag
     * @return the value
     */
    public String getByTag(String t){
        return bundle.getString(t);
    }

    /**
     * @return allTags splitMenuButton
     */
    public String getAllTags(){return bundle.getString("allTags.text");}

    /**
     * @return selectedTags splitMenuButton
     */
    public String getSelectedTags(){return bundle.getString("selectedTags.text");}
}
