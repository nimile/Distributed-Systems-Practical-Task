package de.haevn.distributed_systems.questions;

import de.haevn.distributed_systems.controller.ProductController;
import de.haevn.distributed_systems.controller.QuestionController;
import de.haevn.distributed_systems.model.Product;
import de.haevn.distributed_systems.model.Question;
import de.haevn.distributed_systems.repository.ProductRepository;
import de.haevn.distributed_systems.repository.QuestionRepository;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

public abstract class QuestionsTestConfiguration {
    protected final static List<Question> questions = new ArrayList<>();
    static {
        questions.add(Question.builder().id(1L).firstname("User").lastname("Test").email("user1.test@provider.domain").customerId(1L).category("Repair").subject("Test Subject 1").description("Test description").build());
        questions.add(Question.builder().id(2L).firstname("User").lastname("Test").email("user1.test@provider.domain").customerId(1L).category("Feedback").subject("Test Subject 2").description("Test description").build());

        questions.add(Question.builder().id(2L).firstname("User").lastname("Test").email("user2.test@provider.domain").customerId(1L).category("Repair").subject("Test Subject 3").description("Test description").build());
        questions.add(Question.builder().id(3L).firstname("User").lastname("Test").email("user3.test@provider.domain").customerId(1L).category("Feedback").subject("Test Subject 4").description("Test description").build());
    }


    @Autowired
    public QuestionController controller;

    @MockBean
    public QuestionRepository repository;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;
}
