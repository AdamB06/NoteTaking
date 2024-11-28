package commons;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;

    @OneToMany
    private List<Note> notes;


    /**
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the name of the user
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * A method returning the list of notes in the collection
     * @return collection
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * A method for setting the id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * A method for setting the name of the user
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A method for setting the list of notes of the user
     * @param notes
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
