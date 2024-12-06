package server.api;

import commons.Note;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import server.database.NoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestNoteRepository implements NoteRepository {

    private List<Note> notes = new ArrayList<>();
    private List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    /**
     *
     * @param name
     * @return whether the calls list contains said name
     */
    public  boolean methodIsCalled(String name) {
        return calledMethods.contains(name);
    }

    /**
     *
     */
    @Override
    public void flush() {

    }

    /**
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    /**
     * @param entities
     */
    @Override
    public void deleteAllInBatch(Iterable<Note> entities) {

    }

    /**
     * @param longs
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     *
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Note getOne(Long aLong) {
        return find(aLong).orElse(null);
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Note getById(Long aLong) {
        call("getById");
        return find(aLong).orElse(null);
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Note getReferenceById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    private Optional<Note> find(Long id) {
        return notes.stream().filter(note -> note.getId() == id).findFirst();
    }

    /**
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> List<S> findAll(Example<S> example) {
        return List.of();
    }

    /**
     * @param example
     * @param sort
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    /**
     * @param example
     * @param pageable
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example
     * @param queryFunction
     * @param <S>
     * @param <R>
     * @return
     */
    @Override
    public <S extends Note, R> R findBy(Example<S> example,
                                        Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> S save(S entity) {
        call("save");
        notes.add(entity);
        return entity;
    }

    /**
     * @param entities
     * @param <S>
     * @return
     */
    @Override
    public <S extends Note> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Optional<Note> findById(Long aLong) {
        return Optional.empty();
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<Note> findAll() {
        return List.of();
    }

    /**
     * @param longs
     * @return
     */
    @Override
    public List<Note> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    /**
     * @return
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * @param aLong
     */
    @Override
    public void deleteById(Long aLong) {

    }

    /**
     * @param entity
     */
    @Override
    public void delete(Note entity) {

    }

    /**
     * @param longs
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * @param entities
     */
    @Override
    public void deleteAll(Iterable<? extends Note> entities) {

    }

    /**
     *
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param sort
     * @return
     */
    @Override
    public List<Note> findAll(Sort sort) {
        return List.of();
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<Note> findAll(Pageable pageable) {
        return null;
    }
}
