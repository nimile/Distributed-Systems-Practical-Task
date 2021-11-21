package de.haevn.distributed_systems.service;

import de.haevn.distributed_systems.exceptions.ExistenceException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.model.Question;

import java.util.List;
import java.util.Optional;

public interface IQuestionService {

    Optional<Question> findById(Long id);

    List<Question> findAll();

    Question save(Question question) throws ExistenceException, ArgumentMismatchException;

    void save(List<Question> users);

    void update(Question user);

    long update(List<Question> users);

    void deleteById(Long id);

    void deleteAll();

}
