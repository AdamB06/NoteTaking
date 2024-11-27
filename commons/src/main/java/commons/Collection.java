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


}
