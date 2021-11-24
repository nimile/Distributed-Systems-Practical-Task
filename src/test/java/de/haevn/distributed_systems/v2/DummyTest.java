package de.haevn.distributed_systems.v2;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.UserController;
import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.repository.UserRepository;
import de.haevn.distributed_systems.v2.service.UserService;
import de.haevn.distributed_systems.v2.utils.sequence_generator.SequenceGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = DistributedSystemsApplication.class)
@AutoConfigureMockMvc
public class DummyTest {


    @Autowired
    public UserController controller;

    @MockBean
    public UserRepository repository;

    @MockBean
    public UserService service;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;

    protected final static List<User> users = new ArrayList<>();
    static {
        users.add(User.builder().firstname("Test1").lastname("User1").email("U1@test.domain").address("Street No.1").password("1234").id(1L).build());
        users.add(User.builder().firstname("Test2").lastname("User2").email("U2@test.domain").address("Street No.2").password("1234").id(2L).build());
        users.add(User.builder().firstname("Test3").lastname("User3").email("U3@test.domain").address("Street No.3").password("1234").id(3L).build());
    }

    @BeforeEach
    void beforeEach(){
        when(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME)).thenReturn(1L);
    }

    @Test
    void run(){
        when(repository.save(users.get(0))).thenReturn(users.get(0));
        when(repository.findByEmail(users.get(0).getEmail())).thenReturn(Optional.of(users.get(0)));

        AtomicBoolean saved = new AtomicBoolean(false);
        when(service.findByEmail(users.get(0).getEmail())).thenReturn(Optional.of(users.get(0)));
        when(service.findByEmail(users.get(0).getEmail())).thenAnswer((Answer<?>) invocation ->{
           if(saved.get()){
               return Optional.of(users.get(0));
           }else{
               return Optional.empty();
           }
        });
        when(service.save(users.get(0))).thenAnswer(
                (Answer) invocation -> {
                    saved.set(true);
                    return true;
                }
        );
        var result= Assertions.assertDoesNotThrow(
                () -> controller.post(Optional.of(users.get(0))));
        System.out.println(result.getBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
