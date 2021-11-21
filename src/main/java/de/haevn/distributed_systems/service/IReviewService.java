package de.haevn.distributed_systems.service;

import de.haevn.distributed_systems.model.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewService {

    Optional<Review> findById(Long id);

    List<Review> findByPublisher(String publisher);
    List<Review> findByStarRating(Long publisher);

    List<Review> findAll();

    void save(Review review);

    void save(List<Review> review);

    void update(Review review);

    long update(List<Review> review);

    void deleteAll();

    void deleteById(Long id);
}
