package de.haevn.distributed_systems.v2.service;

import de.haevn.distributed_systems.v2.model.Question;
import de.haevn.distributed_systems.v2.repository.QuestionRepository;
import de.haevn.distributed_systems.v2.service.interfaces.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class QuestionService implements IQuestionService {
    @Autowired
    private QuestionRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Question> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Question> findAll() {
        return repository.findAll();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(Question obj) {
        repository.save(obj);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(List<Question> objs) {
        repository.saveAll(objs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Question> update(Question obj) {
        return updateInternal(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long update(List<Question> objs) {
        long counter;
        counter = objs.stream()
                .filter(obj -> obj.getId() != null)
                .filter(obj -> repository.findById(obj.getId()).isPresent())
                .filter(obj -> updateInternal(obj).isPresent())
                .count();
        return counter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete() {
        repository.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Question> updateInternal(Question input) {
        return Optional.empty();
    }
}
