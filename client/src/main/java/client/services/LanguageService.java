package client.services;

import client.ClientConfig;
import client.LanguageController;
import javafx.scene.image.Image;

import java.util.List;

public class LanguageService {
    private final LanguageController languageController;
    private final ClientConfig config;
    private final String[] languages = {"en", "nl", "es"};
    private final String path = "flags/";

    public LanguageService(LanguageController languageController, ClientConfig config) {
        this.languageController = languageController;
        this.config = config;
    }

    public void loadDefaultLanguage() {
        String defaultLanguage = config.getPreferredLanguage();
        languageController.loadLanguage(defaultLanguage);
    }

    public void loadLanguage(String language) {
        languageController.loadLanguage(language);
        config.setPreferredLanguage(language);
    }

    public List<Image> loadAllFlags(int selectedIndex) {
        Image englishFlag = new Image(path + "uk_flag.png");
        Image dutchFlag = new Image(path + "nl_flag.png");
        Image spanishFlag = new Image(path + "es_flag.png");
        return List.of(englishFlag, dutchFlag, spanishFlag);
    }

    public String[] getLanguages() {
        return languages;
    }
}
