package server.database;

import commons.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    /**
     * Find notes by tag name
     * @param tagName tag name
     * @return list of notes with the given tag name
     */
    @Query("SELECT n FROM Note n JOIN n.tags t WHERE t.name = :tagName")
    List<Note> findNotesByTagName(@Param("tagName") String tagName);

}
