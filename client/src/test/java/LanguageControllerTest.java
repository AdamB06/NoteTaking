import client.LanguageController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageControllerTest {

    @Test
    public void testLanguageLoad(){
        LanguageController lc = new LanguageController();

        String lang = "en";
        lc.loadLanguage(lang);

        String actual = lc.getEditText();
        String expected = "Edit";

        assertEquals(expected, actual);
    }

    @Test
    public void testLanguageSwitch(){
        LanguageController lc = new LanguageController();

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
}
