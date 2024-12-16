package server.database;
import org.springframework.data.jpa.repository.JpaRepository;
import commons.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    /**
     * @param name name of the collection
     * @return whether the collection exists
     */
    boolean existsByName(String name);
}
