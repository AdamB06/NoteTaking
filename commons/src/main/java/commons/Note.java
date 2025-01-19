package commons;


import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashSet;
import java.util.Set;


@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String content;

    private long collectionID;
    private String collectionURL;

    @ManyToMany
    @JoinTable(
            name = "note_tag",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    /**
     * @return the tags linked to a certain note
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Default constructor for note
     */
    @SuppressWarnings("unused")
    public Note() {
        //this is for object mapping
    }

    /**
     * @param title   "title of the note"
     * @param content "content of the note"
     */
    public Note(String title, String content) {
        this.title = title.replaceAll(" ", "").isEmpty() ? "New Note" : title;
        this.content = content;
    }

    /**
     * @return id
     */
    public long getId() {
        return id;

    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return collectionID
     */
    public long getCollectionID() {
        return collectionID;
    }

    /**
     * @return collectionURL
     */
    public String getCollectionURL() {
        return collectionURL;
    }

    /**
     * a setter for the id of the collection this is in
     * @param collectionID id of the collection the note is in
     */
    public void setCollectionID(long collectionID) {
        this.collectionID = collectionID;
    }

    /**
     * a setter for the URL of the collection this is in
     * @param collectionURL URL of the collection the note is in
     */
    public void setCollectionURL(String collectionURL) {
        this.collectionURL = collectionURL;
    }

    /**
     * a setter for the content of a note
     *
     * @param content content of the note
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @param obj "random obj that is being compared"
     * @return true if equal else false
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * @return hashcode
     */
    public int hashcode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * @return human-readable string format
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }


    /**
     * Setter method for the note
     *
     * @param title New title of the note
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param tag a tag that gets added to the Note if it does not already contain that tag
     */
    public void addTag(Tag tag) {
        tags.add(tag);// does not need contains check as a Set cannot have duplicates
        tag.getNotes().add(this);

    }

    /**
     * @param tag a tag that gets removed from the note
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getNotes().remove(this);

    }


    /**
     * @param tagName name of the tag
     * @return returns the created tag
     */
    private Tag createTag(String tagName) {
        Tag newTag = new Tag(tagName);

        return newTag;
    }


}




