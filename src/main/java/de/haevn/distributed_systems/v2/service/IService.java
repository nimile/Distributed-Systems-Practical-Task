package de.haevn.distributed_systems.v2.service;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    Optional<T> findById(Long id);
    List<T> findAll();

    boolean save(T obj);
    void save(List<T> objs);

    Optional<T> update(T obj);
    Long update(List<T> objs);

    void delete(Long id);
    void delete(T obj);
    void delete();


    Optional<T> updateInternal(T input);
}
