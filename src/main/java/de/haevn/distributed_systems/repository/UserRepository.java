package de.haevn.distributed_systems.repository;

import de.haevn.distributed_systems.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByFirstnameAndLastname(String firstName, String lastName);
    List<User> findAllByEmail(String mail);
}
