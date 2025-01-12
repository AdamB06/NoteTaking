package client;

import commons.Note;
import commons.Tag;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TagController {


    /**
     * Process tags in the note text and save them to the current note.
     *
     * @param content content of the note
     * @param note    current note
     */
    public void initializeTags(String content, Note note) {
        System.out.println("Initializing tags...");

        if (content == null || content.isEmpty()) {
            System.out.println("No content to process for tags.");
            return;
        }


        if (note == null) {
            System.out.println("No current note is selected.");
            return;
        }

        try {
            // Pattern to match hashtags like #tag1, #tag2, etc.
            Pattern pattern = Pattern.compile("#(\\w+)");
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String tagName = matcher.group(1); // this is so that we store the tag without the #
                System.out.println("Extracted tag: " + tagName);

                if (!(note.getTags().contains(tagName))) {
                    note.addTag(new Tag(tagName));
                    System.out.println(note.getTags());

                }


               /* for (Tag tag : note.getTags()) {
                    if (!note.getContent().contains(tag.getName())) {
                        note.removeTag(tag);
                        System.out.println("Tag removed: " + tag.getName());
                    }
                }



                */

            }
        } catch (Exception e) {
            System.err.println("Error initializing tags: " + e.getMessage());
        }
    }

    /**
     * @param content   the entire content of the note
     * @param character final input of the user
     * @param note      current note that we are looking at
     */
    public void checkForCorrectUserInput(String content, String character, Note note) {


        if (character.equals(" ") || character.equals("\n")) {
            System.out.println("Space detected.");
            String[] words = content.split("\\s+");
            String lastWord = words[words.length - 1];
            if (lastWord.startsWith("#") && lastWord.length() > 1 &&
                    lastWord.substring(1).matches("\\w+")) {
                System.out.println("Valid tag detected: " + lastWord);
                initializeTags(content, note);
            }
        }
    }
}
