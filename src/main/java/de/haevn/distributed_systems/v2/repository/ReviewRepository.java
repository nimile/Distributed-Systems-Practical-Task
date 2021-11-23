package de.haevn.distributed_systems.v2.repository;

import de.haevn.distributed_systems.v2.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, Long> {
    Optional<Review> findById(Long id);
    List<Review> findAll();
}
