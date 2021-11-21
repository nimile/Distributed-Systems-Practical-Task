package de.haevn.distributed_systems.repository;

import de.haevn.distributed_systems.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, Long> {
    Optional<Review> findById(Long id);

    List<Review> findByPublisher(String publisher);
    List<Review> findByStarRating(Long rating);
    List<Review> findAll();
}
