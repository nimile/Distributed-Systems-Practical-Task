package de.haevn.distributed_systems.v2.repository;

import de.haevn.distributed_systems.v2.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends MongoRepository<Question, Long> {
    Optional<Question> findById(Long id);
    List<Question> findAll();
}
