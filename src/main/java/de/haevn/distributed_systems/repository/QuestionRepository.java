package de.haevn.distributed_systems.repository;

import de.haevn.distributed_systems.model.Question;
import de.haevn.distributed_systems.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends MongoRepository<Question, Long> {
    Optional<Question> findById(Long id);
    List<Question> findByCustomerId(Long id);
}
