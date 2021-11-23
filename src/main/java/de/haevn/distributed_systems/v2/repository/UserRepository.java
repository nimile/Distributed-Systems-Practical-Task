package de.haevn.distributed_systems.v2.repository;

import de.haevn.distributed_systems.v2.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String mail);
    List<User> findAll();
}
