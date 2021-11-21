package de.haevn.distributed_systems.user;

import de.haevn.distributed_systems.controller.UserController;
import de.haevn.distributed_systems.model.User;
import de.haevn.distributed_systems.repository.UserRepository;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;

public abstract class UserTestConfiguration {
    protected final static List<User> users = new ArrayList<>();
    static {
        users.add(User.builder().firstname("Test1").lastname("User1").email("U1@test.domain").address("Street No.1").password("1234").id(1L).build());
        users.add(User.builder().firstname("Test2").lastname("User2").email("U2@test.domain").address("Street No.2").password("1234").id(2L).build());
        users.add(User.builder().firstname("Test3").lastname("User3").email("U3@test.domain").address("Street No.3").password("1234").id(3L).build());
    }

    @Autowired
    public UserController controller;

    @MockBean
    public UserRepository repository;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;
}
