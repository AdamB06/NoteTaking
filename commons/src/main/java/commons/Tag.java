package commons;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
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
     * @param name name of the tag
     */
    public Tag(String name) {
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
     * @param o  "random obj that is being compared"
     * @return true if equal else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    /**
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }



    /**
     * @return human-readable string format
     */
    public String toString() {
        return this.name;
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
     * @param notes set of notes associated with this tag
     */
    public void setNotes(Set<Note> notes){
        this.notes = notes;
    }







}