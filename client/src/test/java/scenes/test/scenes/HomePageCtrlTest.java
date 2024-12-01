package scenes.test.scenes;

import client.scenes.HomePageCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import static org.junit.jupiter.api.Assertions.*;

public class HomePageCtrlTest {
    private Button editButton;
    private TextField titleField;
    private String notesBodyArea;
    private HomePageCtrl homePageCtrl;

    @BeforeAll
    public static void initJavaFX(){
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void setUp() {
        notesBodyArea = "";
        homePageCtrl = new HomePageCtrl(null);
        editButton = new Button();
        titleField = new TextField();

        homePageCtrl.setTitleField(titleField);
        homePageCtrl.setEditButton(editButton);
    }


    @Test
    public void testHeaderMarkdownConverter() {
        notesBodyArea = "# Hello World";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<h1>Hello World</h1>\n", htmlText);
    }

    @Test
    public void testEmptyMarkdownConverter() {
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("", htmlText);
    }

    @Test
    public void testSimpleTextMarkdownConverter() {
        notesBodyArea = "This is simple text to be tested.";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<p>This is simple text to be tested.</p>\n", htmlText);
    }

    @Test
    public void testSubHeaderMarkdownConverter() {
        notesBodyArea = "## Subheader";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<h2>Subheader</h2>\n", htmlText);
    }

    @Test
    public void testBoldItalicMarkdownConverter() {
        notesBodyArea = "***Bold And Italic Text***";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<p><em><strong>Bold And Italic Text</strong></em></p>\n", htmlText);
    }

    @Test
    public void testMultipleParagraphMarkdownConverter() {
        notesBodyArea = "First paragraph.\n\nSecond paragraph.";
        String htmlText = homePageCtrl.markdownConverter(notesBodyArea);
        assertEquals("<p>First paragraph.</p>\n<p>Second paragraph.</p>\n", htmlText);
    }

    @Test
    public void testUninitializedTextArea() {
        assertThrows(IllegalStateException.class, homePageCtrl::addListener);
    }


    @Test
    public void testInitializeEditInitialState() {
        //Platform.runLater makes sure that the value you want to run is
        //put in the queue for the JavaFX to run later, because it can only
        //be run by the JavaFX thread
        Platform.runLater(() -> {
            homePageCtrl.initializeEdit();

            assertFalse(titleField.isEditable(), "TitleField should initially be non-editable.");
            assertEquals("Edit", editButton.getText(), "EditButton should initially display 'Edit'.");
        });
    }


    @Test
    public void testEditButtonEnablesEdit() {
        // Initialize the edit functionality
        Platform.runLater(() -> {
            homePageCtrl.initializeEdit();

            // Simulate clicking the "Edit" button
            editButton.fire();

            assertTrue(titleField.isEditable(), "TitleField should be editable after clicking 'Edit'.");

            assertTrue(titleField.isFocused(), "TitleField should be focused after clicking 'Edit'.");

            // Assert that the entire text in the titleField is selected
            assertEquals(0, titleField.getSelection().getStart(), "Selection should start at 0.");
            assertEquals(titleField.getText().length(),
                    titleField.getSelection().getEnd(),
                    "Selection should extend to the end of the text.");

            // Assert that the button text changes to "Save"
            assertEquals("Save", editButton.getText(), "Edit button text should change to 'Save'.");
        });
    }


    @Test
    public void testDisableEditingAndSaveTitle() {
        Platform.runLater(() -> {
            homePageCtrl.initializeEdit();
            editButton.fire(); // Edit button switches to "Save"
            assertTrue(titleField.isEditable(), "TitleField should be editable after clicking 'Edit'.");

            // Set a title for the TextField
            titleField.setText("New Title");

            // Simulate clicking the "Save" button
            editButton.fire(); // Save button switches back to "Edit"

            // Assert that the titleField is no longer editable
            assertFalse(titleField.isEditable(), "TitleField should not be editable after clicking 'Save'.");

            // Assert that the editButton's text changes back to "Edit"
            assertEquals("Edit", editButton.getText(), "Edit button text should change back to 'Edit' after saving.");

        });
    }

    @Test
    public void testEditButtonDoesNotSaveWhenEmpty() {
        Platform.runLater(() -> {
            homePageCtrl.initializeEdit();

            // Simulate clicking the "Edit" button to enable editing
            editButton.fire();

            // Assert the field becomes editable and the button changes to "Save"
            assertTrue(titleField.isEditable());
            assertEquals("Save", editButton.getText());

            // Set the title to empty and simulate clicking "Save"
            titleField.setText("");
            editButton.fire();

            // Assert the field is no longer editable
            assertFalse(titleField.isEditable());
            assertEquals("Edit", editButton.getText());

            assertNotNull(titleField.getText(), "Title should not be null or empty.");
        });
    }



}