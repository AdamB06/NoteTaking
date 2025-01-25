import client.LanguageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageControllerTest {

    private LanguageController lc;

    @BeforeEach
    public void init() {
        lc = new LanguageController();
    }

    @Test
    public void testLoadEnglish() {
        String lang = "en";
        lc.loadLanguage(lang);

        String actual = lc.getEditText();
        String expected = "Edit";

        assertEquals(expected, actual);
    }

    @Test
    public void testLoadDefault(){
        String lang = "adfsdf";
        lc.loadLanguage(lang);

        String actual = lc.getEditText();
        String expected = "Edit";

        assertEquals(expected, actual);
    }

    @Test
    public void testLoadGerman() {
        String lang = "de";
        lc.loadLanguage(lang);

        String actual = lc.getEditText();
        String expected = "Bearbeiten";

        assertEquals(expected, actual);
    }

    @Test
    public void testLanguageSwitch() {
        String lang = "en";
        lc.loadLanguage(lang);

        String actual = lc.getEditText();
        String expected = "Edit";

        assertEquals(expected, actual);

        lang = "nl";
        lc.loadLanguage(lang);

        actual = lc.getEditText();
        expected = "Aanpassen";

        assertEquals(expected, actual);

        lang = "es";


        lc.loadLanguage(lang);

        actual = lc.getEditText();
        expected = "Editar";

        assertEquals(expected, actual);
    }

    @Test
    public void testLanguageSwitchDifferent() {
        String lang = "de";
        lc.loadLanguage(lang);

        String actual = lc.getEditText();
        String expected = "Bearbeiten";

        assertEquals(expected, actual);

        lang = "es";
        lc.loadLanguage(lang);

        actual = lc.getEditText();
        expected = "Editar";

        assertEquals(expected, actual);

        lang = "nl";


        lc.loadLanguage(lang);

        actual = lc.getEditText();
        expected = "Aanpassen";

        assertEquals(expected, actual);
    }

    @Test
    public void testPersistence() {
        String lang = "en";
        lc.loadLanguage(lang);

        LanguageController lc2 = new LanguageController();
        lc2.loadLanguage(lang);

        String expected = lc.getEditText();
        String actual = lc2.getEditText();

        assertEquals(actual, expected);
    }

}
