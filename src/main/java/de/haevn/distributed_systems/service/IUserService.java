package de.haevn.distributed_systems.service;

import de.haevn.distributed_systems.exceptions.ExistenceException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String id);

    List<User> findAll();

    User save(String firstName, String lastName, String email, String address, String password) throws ExistenceException, ArgumentMismatchException;

    void save(List<User> users);

    void update(User user);

    long update(List<User> users);

    void deleteById(Long id);

    void deleteByEmail(String email);

    void deleteAll();

}
