package commons;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String content;

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
     * @param title New title of the note
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter method for the note
     * @param content New content of the note
     */
    public void setContent(String content) {
        this.content = content;
    }
}


