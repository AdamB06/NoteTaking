package server.database;
import org.springframework.data.jpa.repository.JpaRepository;
import commons.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    public boolean existsByName(String name);
}
