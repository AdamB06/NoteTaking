package server.api;

import commons.Collection;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.CollectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestCollectionRepository implements CollectionRepository {

    private List<Collection> collections = new ArrayList<>();

    /**
     * @param name name of the collection
     * @return
     */
    @Override
    public boolean existsByName(String name) {
        return false;
    }

    public Collection getById(String string) {
        return find(string).orElse(null);
    }

    private Optional<Collection> find(String id) {
        return collections.stream().filter(collection -> collection.getId().equals(id)).findFirst();
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
    public <S extends Collection> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    /**
     * @param entities
     */
    @Override
    public void deleteAllInBatch(Iterable<Collection> entities) {

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
    public Collection getOne(Long aLong) {
        return null;
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Collection getById(Long aLong) {
        return null;
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Collection getReferenceById(Long aLong) {
        return null;
    }

    /**
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> List<S> findAll(Example<S> example) {
        return List.of();
    }

    /**
     * @param example
     * @param sort
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    /**
     * @param example
     * @param pageable
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> boolean exists(Example<S> example) {
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
    public <S extends Collection, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> S save(S entity) {
        collections.add(entity);
        return entity;
    }

    /**
     * @param entities
     * @param <S>
     * @return
     */
    @Override
    public <S extends Collection> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Optional<Collection> findById(Long aLong) {
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
    public List<Collection> findAll() {
        return List.of();
    }

    /**
     * @param longs
     * @return
     */
    @Override
    public List<Collection> findAllById(Iterable<Long> longs) {
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
    public void delete(Collection entity) {

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
    public void deleteAll(Iterable<? extends Collection> entities) {

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
    public List<Collection> findAll(Sort sort) {
        return List.of();
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<Collection> findAll(Pageable pageable) {
        return null;
    }
}
