package commons;


import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


import java.util.ArrayList;
import java.util.List;

@Entity
public class Collection {

    @Id
    private String id;

    private String name;

    @OneToMany
    private List<Note> notes;

    /**
     * Default constructor for collection
     */
    @SuppressWarnings("unused")
    public Collection() {
        //this is for object mapping
    }

    /**
     *
     * @param list "a list of notes"
     * @param name "name of the owner of the collection"
     * @param id "unique string as id of the collection"
     */
    public Collection(String name, List<Note> list, String id){
        this.notes = new ArrayList<>(list);
        this.name = name;
        this.id = id;
    }

    /**
     * @return the name (the owner) of the collection
     */
    public String getName() {
        return name;
    }

    /**
     * @return list of notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * @return id
     */
    public String getId(){
        return id;
    }

    /**
     * @param name that will be set for the Collection
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param notes that will be set in the Collection
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
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
}

