package commons;

import jakarta.persistence.*;

@Entity
public class Collection{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String name;
}