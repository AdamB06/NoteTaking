package commons;


import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Note> notes = new HashSet<>();



    /**
     *
     * @param id id of the tag
     * @param name name of the tag
     */
    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id id of the tag
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name of the tag
     */
    public void setName(String name) {
        this.name = name;
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
     *
     * @return the set of notes associated with this tag
     */
    public Set<Note> getNotes(){
        return notes;
    }

    /**
     *
     * @param notes
     */
    public void setNotes(Set<Note> notes){
        this.notes = notes;
    }

}