package de.haevn.distributed_systems.service;

import de.haevn.distributed_systems.exceptions.ExistenceException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.exceptions.found.NoSuchQuestionException;
import de.haevn.distributed_systems.model.Question;
import de.haevn.distributed_systems.model.User;
import de.haevn.distributed_systems.repository.QuestionRepository;
import de.haevn.distributed_systems.utils.CustomOptionalUtils;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService implements IQuestionService{
    public static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private QuestionRepository repository;

    @Override
    public Optional<Question> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Question> findAll() {
        return repository.findAll();
    }

    @Override
    public Question save(Question question) throws ExistenceException, ArgumentMismatchException {
        CustomOptionalUtils.containsNullValue(question);
        logger.info("Create new question");
        question.setId(sequenceGeneratorService.generateSequence(Question.SEQUENCE_NAME));
        create(question);
        return question;
    }

    @Override
    public void save(List<Question> questions) {
        logger.info("Save questions {}", questions);
        repository.saveAll(questions);
    }

    @Override
    public void update(Question question) {
        logger.info("Update question {}", question);
        updateQuestion(question, Optional.of(question));
    }

    @Override
    public long update(List<Question> questions) {
        logger.info("Update questions {}", questions);
        return questions.stream()
                .filter(question -> null != question.getId())
                .filter(target -> updateQuestion(target, findById(target.getId())))
                .count();
    }

    @Override
    public void deleteById(Long id) {
        logger.warn("Delete question {}", id);
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        logger.warn("Delete all questions");
        repository.deleteAll();
    }


    private boolean updateQuestion(Question received, Optional<Question> targetQuestion) {
        if(targetQuestion.isEmpty()){
            logger.warn("Targeted question is empty");
            return false;
        } else if(repository.findById(received.getId()).isEmpty()){
            logger.warn("Question does not exists");
            return false;
        }
        var target = repository.findById(received.getId()).orElseThrow(NoSuchQuestionException::new);
        var customerID = received.getCustomerId();
        var firstname = received.getFirstname();
        var lastname = received.getLastname();
        var email = received.getEmail();
        var subject = received.getSubject();
        var category = received.getCategory();
        var description = received.getDescription();


        if(null != firstname && !firstname.isBlank()){
            target.setFirstname(firstname);
        }
        if(null != lastname && !lastname.isBlank()){
            target.setLastname(lastname);
        }
        if(null != email && !email.isBlank()){
            target.setEmail(email);
        }
        if(null != subject && !subject.isBlank()){
            target.setSubject(subject);
        }
        if(null != customerID){
            target.setCustomerId(customerID);
        }
        if(null != description && !description.isBlank()){
            target.setDescription(description);
        }
        if(null != category && !category.isBlank()){
            target.setCategory(category);
        }

        repository.save(target);
        return true;
    }

    public void create(Question question){
        repository.save(question);
    }

    public List<Question> findByUserId(long uid) {
        return repository.findByCustomerId(uid);
    }
}
