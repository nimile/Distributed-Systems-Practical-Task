package de.haevn.distributed_systems.v2.service.interfaces;

import java.util.List;
import java.util.Optional;

public interface IService<T> {

    /**
     * Returns an object corresponding to the ID.<br>
     * If no object is associated with the given ID an empty optional is returned.
     * @param id ID of the object
     * @return Optional containing the actual object
     */
    Optional<T> findById(Long id);

    /**
     * Returns a list of all objects
     * @return List of all existing objects
     */
    List<T> findAll();


    /**
     * Saves an object into the database upon successful execution true is returned
     * @param obj Object to store
     * @return True if the operation succeed
     */
    boolean save(T obj);

    /**
     * Stores a list of objects into the database
     * @param objs Objects to store
     */
    void save(List<T> objs);

    /**
     * Updates an object and returns the updated object.<br>
     * {@link IService#updateInternal(Object)} should be handle the update
     * @param obj Object to update
     * @return Updated object
     */
    Optional<T> update(T obj);

    /**
     * Updates a list of objects.<br>
     * For each element of the list the {@link IService#updateInternal(Object)} should be called
     * @param objs Objects to update
     * @return amount of updated objects
     */
    Long update(List<T> objs);

    /**
     * Deletes the object associated with the given id
     * @param id ID which associates the object
     */
    void delete(Long id);

    /**
     * Deletes all objects.<br>
     * The database sequence should be reset too
     */
    void delete();

    /**
     * This method implements the update logic for an object.<br>
     * It is called from the {@link IService#update(Object)} and {@link IService#update(List)} methods.
     * @param input Object to update
     * @return Updated object
     */
    Optional<T> updateInternal(T input);
}
