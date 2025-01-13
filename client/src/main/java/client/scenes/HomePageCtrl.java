package client.scenes;

import client.*;
import client.services.AutoSaveService;
import client.services.MarkdownService;
import client.services.NoteService;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import com.google.inject.Guice;
import commons.Collection;
import commons.Note;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.event.ActionEvent;
import javafx.scene.control.ListCell;

import java.net.URL;
import java.util.*;


public class HomePageCtrl implements Initializable {
    @FXML
    private TextArea notesBodyArea;
    @FXML
    private WebView webView;
    @FXML
    private TextField titleField;
    @FXML
    private Button editButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button shortcutsButton;
    @FXML
    private Label collectionsLabel;
    @FXML
    private Label previewTextLabel;
    @FXML
    private ListView<Note> notesListView;
    @FXML
    private TextField searchBox;
    @FXML
    private ComboBox<Image> languageComboBox;

    @FXML
    private Image englishFlag;
    @FXML
    private Image dutchFlag;
    @FXML
    private Image spanishFlag;

    private final String path = "flags/";
    private boolean isEditText;
    private boolean isLoadingLanguage = false;
    private final SimpleObjectProperty<Note> currentNote = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Collection> currentCollection = new SimpleObjectProperty<>();//TODO setup initialize and refreshing of current collection
    private final String[] languages = {"en", "nl", "es"};
    private String original;
    private Injector injector;
    private NoteService noteService;
    private MarkdownService markdownService;
    private AutoSaveService autoSaveService;
    private final LanguageController languageController;
    private final MnemonicCreator mnemonicCreator;
    private final Warnings warnings;

    /**
     * Constructor for HomePageCtrl.
     *
     * @param languageController the LanguageController instance to be injected
     * @param mnemonicCreator    the MnemonicCreator instance to be injected
     * @param warnings           the Warnings instance to be injected
     * @param serverUtils        the ServerUtils instance to be injected
     */
    @Inject
    public HomePageCtrl(LanguageController languageController, MnemonicCreator mnemonicCreator,
                        Warnings warnings, ServerUtils serverUtils) {
        this.languageController = languageController;
        this.mnemonicCreator = mnemonicCreator;
        this.warnings = warnings;

        injector = Guice.createInjector(new MyModule());

        this.noteService = injector.getInstance(NoteService.class);
        this.markdownService = injector.getInstance(MarkdownService.class);
        this.autoSaveService = new AutoSaveService(serverUtils, noteService);
    }

    /**
     * Initializes the HomePageCtrl.
     *
     * @param url            the URL
     * @param resourceBundle the ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String defaultLanguage = ClientConfig.loadConfig().getPreferredLanguage();
        System.out.println(defaultLanguage);
        languageController.loadLanguage(defaultLanguage);

        titleField.setEditable(false);
        addListener();
        webView.getEngine().setUserStyleSheetLocation(getClass()
                .getResource("/configuration/WebViewConfig.css").toString());
        initializeEdit();
        original = notesBodyArea.getText();
        refreshNotesInternal();

        // Load flag images
        englishFlag = new Image(path + "uk_flag.png");
        dutchFlag = new Image(path + "nl_flag.png");
        spanishFlag = new Image(path + "es_flag.png");

        loadAllFlags(Arrays.asList(languages).indexOf(defaultLanguage));
        languageComboBox.setOnAction(this::loadLanguage);

        initializeFilteringOfNotes();
        setupNotesListView();
        configureAutoSave();
        notesBodyArea.setDisable(true);
        editButton.setDisable(true);

        Platform.runLater(this::initializeButtonsGraphics);
        Platform.runLater(this::initializeMnemonicsAndLanguage);

        shortcutsButton.setText(languageController.getByTag("showShortcuts.text"));
        shortcutsButton.setOnAction(action -> shortcutsHint());
    }

    /**
     * Initializes the button graphics
     */
    private void initializeButtonsGraphics() {
        int size = 20;

        Image add = new Image("icons/add.png");
        ImageView addV = new ImageView(add);
        addV.setFitHeight(size);
        addV.setFitWidth(size);
        addV.setPreserveRatio(true);

        Image refresh = new Image("icons/refresh.png");
        ImageView refreshV = new ImageView(refresh);
        refreshV.setFitHeight(size);
        refreshV.setFitWidth(size);
        refreshV.setPreserveRatio(true);

        Image remove = new Image("icons/remove.png");
        ImageView removeV = new ImageView(remove);
        removeV.setFitHeight(size);
        removeV.setFitWidth(size);
        removeV.setPreserveRatio(true);

        refreshButton.setGraphic(refreshV);
        addButton.setGraphic(addV);
        deleteButton.setGraphic(removeV);
    }

    private void shortcutsHint(){
        warnings.inform(languageController.getByTag("shortcutsTitle.text"),
                languageController.getByTag("shortcutsInfo.text"),
                languageController.getByTag("shortcutsHeader.text"));
    }

    /**
     * Initializes the mnemonics
     */
    private void initializeMnemonicsAndLanguage() {
        loadLanguage(null);
        mnemonicCreator.initialize(editButton, addButton, deleteButton,
                refreshButton, searchBox, notesListView);
    }

    /**
     * Adds a listener to the notesBodyArea.
     */
    public void addListener() {
        if (notesBodyArea != null) {
            notesBodyArea.textProperty()
                    .addListener((observable, oldValue, markdownText) -> {
                        String html = markdownService.convertToHtml(markdownText);
                        updateWebView(html);
                    });
        } else {
            throw new IllegalStateException("TextArea is not initialized!");
        }
    }

    /**
     * Updates the WebView with the given HTML text.
     *
     * @param htmlText the HTML text to be displayed
     */
    private void updateWebView(String htmlText) {
        webView.getEngine().loadContent(htmlText);
    }

    /**
     * Sets up the notes ListView.
     */
    private void setupNotesListView() {
        // Custom cell factory to show only titles
        notesListView.setCellFactory(listView -> new ListCell<Note>() {
            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setText(null);
                } else {
                    setText(note.getTitle());
                }
            }
        });

        notesListView.getSelectionModel().selectedItemProperty().
                addListener((observable, oldNote, newNote) -> {
                    if (oldNote != null && !notesBodyArea.getText().equals(oldNote.getContent())) {
                        saveChanges(oldNote.getId(), notesBodyArea.getText());
                    }
                    if (newNote != null) {
                        titleField.setText(newNote.getTitle());
                        notesBodyArea.setText(newNote.getContent());
                        original = newNote.getContent();
                        currentNote.set(newNote);
                        notesBodyArea.setDisable(false);
                        editButton.setDisable(false);
                        autoSaveService.setOriginalContent(original);
                    } else {
                        titleField.clear();
                        notesBodyArea.clear();
                        currentNote.set(null);
                        notesBodyArea.setDisable(true);
                        editButton.setDisable(true);
                    }
                });
    }

    /**
     * Initializes the edit button.
     */
    private void initializeEdit() {
        isEditText = true;
        editButton.setText(languageController.getEditText());

        editButton.setOnAction(actionEvent -> {
            if (isEditText) {
                titleField.setEditable(true);
                titleField.requestFocus();
                titleField.selectAll();

                editButton.setText(languageController.getSaveText());
                isEditText = false;
            } else {
                Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
                if (selectedNote != null) {
                    String newTitle = titleField.getText();
                    String updatedTitle = noteService.updateNoteTitle(
                            selectedNote.getId(), newTitle);
                    if (updatedTitle.equals(newTitle)) {
                        selectedNote.setTitle(newTitle);
                        int selectedIndex = notesListView.getSelectionModel().getSelectedIndex();
                        notesListView.getItems().set(selectedIndex, selectedNote);
                        notesListView.refresh();
                    } else {
                        System.out.println(" new+ " + newTitle);
                        System.out.println("lold  " + updatedTitle);
                        String header, content;
                        boolean set = true;
                        if (selectedNote.getTitle().isEmpty()) {
                            header = languageController.getByTag("emptyTitleHeader.text");
                            content = languageController.getByTag("emptyTitleContent.text");
                        } else if (updatedTitle.equals("Error: 500")) {
                            header = languageController.getByTag("duplicateTitleHeader.text");
                            content = languageController.getByTag("duplicateTitleContent.text");
                        }
                        else {
                            set = false;
                            content = header = "";
                        }
                        if(set)
                            warnings.error(languageController.getByTag("errorText.text"), content, header);
                        // Optionally, revert the titleField to the original title
                        titleField.setText(selectedNote.getTitle());
                    }
                }
                titleField.setEditable(false);

                editButton.setText(languageController.getEditText());
                isEditText = true;
            }
        });
    }

    /**
     * Refreshes the notes in the ListView.
     */
    private void refreshNotesInternal() {
        Platform.runLater(() -> {
            noteService.refreshNotes();
            List<Note> notes = noteService.getNotes();
            if (notesListView != null) {
                Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
                notesListView.getItems().clear();
                notesListView.getItems().addAll(notes);

                // Preserve the selection if possible
                if (selectedNote != null && notes.contains(selectedNote)) {
                    notesListView.getSelectionModel().select(selectedNote);
                }
            } else {
                System.err.println("ListView not initialized!");
            }
        });
    }

    /**
     * Loads the chosen language from the ComboBox.
     *
     * @param event the event that triggers the language change
     */
    private void loadLanguage(ActionEvent event) {
        if (isLoadingLanguage)
            return;

        isLoadingLanguage = true;

        int i = getCurrentLanguage();
        String language = languages[i];
        languageController.loadLanguage(language);
        ClientConfig.loadConfig().setPreferredLanguage(language); // Update config

        // Update UI texts based on the selected language
        editButton.setText(isEditText ? languageController.getEditText() :
                languageController.getSaveText());
        collectionsLabel.setText(languageController.getCollectionsLabelText());
        previewTextLabel.setText(languageController.getPreviewLabelText());
        searchBox.setPromptText(languageController.getSearchBoxText());
        titleField.setPromptText(languageController.getTitleFieldText());
        notesBodyArea.setPromptText(languageController.getNotesBodyAreaText());

        loadAllFlags(i);

        isLoadingLanguage = false;
    }

    /**
     * @return the selected language index in the ComboBox
     */
    private int getCurrentLanguage() {
        for (int i = 0; i < languages.length; i++) {
            if (languageComboBox.getSelectionModel().isSelected(i)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Loads all the flags in the ComboBox
     */
    private void loadAllFlags(int index) {
        languageComboBox.getItems().clear();
        languageComboBox.getItems().addAll(englishFlag, dutchFlag, spanishFlag);
        languageComboBox.setCellFactory(unused -> new LanguageSelectCell());
        languageComboBox.setButtonCell(new LanguageSelectCell());
        languageComboBox.getSelectionModel().select(index);
    }

    private static class LanguageSelectCell extends ListCell<Image> {
        @Override
        protected void updateItem(Image image, boolean empty) {
            super.updateItem(image, empty);

            if (image == null || empty) {
                setGraphic(null);
            } else {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(50);
                imageView.setFitHeight(25);
                setGraphic(imageView);
            }
        }
    }

    /**
     * Configures auto-save functionality.
     */
    private void configureAutoSave() {
        notesBodyArea.setOnKeyTyped(event -> {
            Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
            if (selectedNote != null) {
                String currentContent = notesBodyArea.getText();
                autoSaveService.onKeyPressed(selectedNote, currentContent);
            }
        });
    }

    /**
     * Initializes the filtering of notes.
     */
    public void initializeFilteringOfNotes() {
        searchBox.setFocusTraversable(false);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                resetFilteredList();
            } else {
                List<Note> filteredNotes = noteService.filterNotes(
                        newValue, noteService.getNotes());
                notesListView.getItems().clear();
                notesListView.getItems().addAll(filteredNotes);
            }
        });

        titleField.setOnKeyTyped(event -> {
            String input = titleField.getText();
            if (currentNote.get() != null) {
                currentNote.get().setTitle(input);
            }
        });

        notesBodyArea.setOnKeyTyped(event -> {
            String input = notesBodyArea.getText();
            if (currentNote.get() != null) {
                currentNote.get().setContent(input);
            }
        });
    }

    /**
     * When the search box is empty, reset the filtered list to the full notes list.
     */
    private void resetFilteredList() {
        notesListView.getItems().clear();
        notesListView.getItems().addAll(noteService.getNotes());
    }

    /**
     * Handles adding a new note.
     */
    @FXML
    private void handleAddNote(ActionEvent event) {
        Note createdNote = noteService.createNote();
        if (createdNote != null) {
            notesListView.getItems().add(createdNote);
            notesListView.getSelectionModel().select(createdNote);
            mnemonicCreator.updateIndex(notesListView.getSelectionModel().getSelectedIndex());
            System.out.println("Note created with ID: " + createdNote.getId());
            warnings.inform(languageController.getByTag("noticeText.text"),
                    languageController.getByTag("noteAddedContent.text"),
                    languageController.getByTag("noteAddedHeader.text"));
        } else {
            warnings.error(languageController.getByTag("errorText.text"),
                    languageController.getByTag("noteFailedContent.text"),
                    languageController.getByTag("noteFailedHeader.text"));
        }
    }

    /**
     * Handles deleting the selected note.
     */
    @FXML
    private void handleDeleteNote(ActionEvent event) {
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            boolean confirm = warnings.askOkCancel(
                    languageController.getByTag("confirmationText.text"),
                    languageController.getByTag("confirmationText.message")
            );

            if (!confirm)
                return;

            String status = noteService.deleteNote(selectedNote);
            if ("Successful".equals(status)) {
                refreshNotesInternal();
                warnings.inform(
                        languageController.getByTag("noticeText.text"),
                        languageController.getByTag("notice.noteRemoved.message"),
                        languageController.getByTag("notice.noteRemoved.details")
                );
            } else {
                warnings.error(
                        languageController.getByTag("errorText.text"),
                        languageController.getByTag("error.deletionFailed.message"),
                        languageController.getByTag("error.deletionFailed.details")
                );
            }
        } else {
            warnings.inform(
                    languageController.getByTag("noticeText.text"),
                    languageController.getByTag("notice.noNoteSelected.message"),
                    languageController.getByTag("notice.noNoteSelected.details")
            );
        }

    }

    /**
     * Handles refreshing the notes list when the refresh button is clicked.
     *
     * @param event The ActionEvent triggered by clicking the refresh button.
     */
    @FXML
    private void handleRefreshNotes(ActionEvent event) {
        refreshNotesInternal();
    }

    /**
     * Saves changes made to a note.
     *
     * @param noteId  The ID of the note to save changes for.
     * @param content The new content of the note.
     */
    private void saveChanges(long noteId, String content) {
        System.out.println("Saving changes for note ID: " + noteId);
        Map<String, Object> changes = autoSaveService.getChanges(original, content);
        original = content;

        String status = noteService.saveChanges(noteId, changes);
        System.out.println("Save status: " + status);

        if (!"Successful".equals(status)) {
            Note note = noteService.getNoteById(noteId);
            autoSaveService.retrySave(note, changes);
        } else {
            noteService.refreshNotes();
            refreshNotesInternal();
        }
    }

    /**
     * This method force saves the text that is typed if the application is
     * abruptly closed
     */
    public void forceSaveBeforeClose() {
        Note current = currentNote.get();
        if (current != null) {
            String content = notesBodyArea.getText();

            // Compare content vs. original
            if (!content.equals(original)) {
                saveChanges(current.getId(), content);
            } else {
                System.out.println("No changes detected; skipping final save.");
            }
        }
    }
}
