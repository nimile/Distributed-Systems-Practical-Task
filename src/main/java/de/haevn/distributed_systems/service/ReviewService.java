package de.haevn.distributed_systems.service;

import de.haevn.distributed_systems.model.Review;
import de.haevn.distributed_systems.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService{
    public static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository repository;

    @Override
    public Optional<Review> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Review> findByPublisher(String publisher) {
        return repository.findByPublisher(publisher);
    }

    @Override
    public List<Review> findByStarRating(Long rating) {
        return repository.findByStarRating(rating);
    }

    @Override
    public List<Review> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Review review) {
        logger.info("Save review {}", review);
        repository.save(review);
    }

    @Override
    public void save(List<Review> reviews) {
        logger.info("Save reviews {}", reviews);
        repository.saveAll(reviews);
    }

    @Override
    public void update(Review review) {
        logger.info("Update review {}", review);
        updateReview(review, Optional.of(review));
    }

    @Override
    public long update(List<Review> reviews) {
        logger.info("Update reviews {}", reviews);
        return reviews.stream()
                .filter(review -> review.getId() != null)
                .filter(target-> updateReview(target, findById(target.getId())))
                .count();
    }

    @Override
    public void deleteAll() {
        logger.warn("Delete all reviews");
        repository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        logger.warn("Delete review {}", id);
        repository.deleteById(id);
    }






    private boolean updateReview(Review received, Optional<Review> targetReview){
        if(targetReview.isEmpty()){
            logger.warn("Targeted review is empty");
            return false;
        }

        var target = targetReview.get();
        var publisher = received.getPublisher();
        var text = received.getText();
        var rating = received.getStarRating();

        if(null != publisher){
            target.setPublisher(publisher);
        }
        if(null != text && !text.isBlank()){
            target.setText(text);
        }
        if(null != rating){
            target.setStarRating(rating);
        }
        repository.save(target);
        return true;
    }
}
