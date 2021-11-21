package de.haevn.distributed_systems.service;

import de.haevn.distributed_systems.exceptions.ExistenceException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.model.User;
import de.haevn.distributed_systems.repository.UserRepository;
import de.haevn.distributed_systems.utils.CustomOptionalUtils;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{
    public static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(String firstName, String lastName, String email, String address, String password) throws ExistenceException, ArgumentMismatchException {
        CustomOptionalUtils.containsNullValue(firstName, lastName, email, address, password);
        logger.info("Create new user");
        // This method throw a runtime exception if one argument is empty
        if(findByEmail(email).isPresent()){
            // The user already exists throw a new ExistingException
            logger.warn("User already exists");
            throw new ExistenceException();
        }

        // Construct a new user object
        User user = User.builder()
                .firstname(firstName)
                .lastname(lastName)
                .email(email)
                .address(address)
                .password(password)
                .id(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME))
                .build();

        create(user);
        return user;
    }

    public void create(User user){
        userRepository.save(user);
    }

    @Override
    public void save(List<User> users) {
        logger.info("Save users {}", users);
        userRepository.saveAll(users);
    }

    @Override
    public void update(User user) {
        logger.info("Update user {}", user);
        updateUser(user, Optional.of(user));
    }

    @Override
    public long update(List<User> users) {
        logger.info("Update users {}", users);
        return users.stream()
                .filter(user -> !user.getEmail().isEmpty())
                .filter(target ->updateUser(target, findByEmail(target.getEmail())))
                .count();
    }

    @Override
    public void deleteById(Long id) {
        logger.warn("Delete user {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public void deleteByEmail(String email) {
        logger.warn("Delete user {}", email);
        findByEmail(email).ifPresent(user -> userRepository.deleteById(user.getId()));
    }

    @Override
    public void deleteAll() {
        logger.warn("Delete all user");

        userRepository.deleteAll();
    }





    private boolean updateUser(User received, Optional<User> targetUser){
        if(targetUser.isEmpty()){
            logger.warn("Targeted user is empty");
            return false;
        }else if(userRepository.findByEmail((received.getEmail())).isEmpty()){
            logger.warn("User does not exists");
            return false;
        }
        var target = targetUser.get();
        var firstname = received.getFirstname();
        var lastname = received.getLastname();
        var address = received.getAddress();
        var password = received.getPassword();
        if(null != firstname && !firstname.isBlank()){
            target.setFirstname(firstname);
        }
        if(null != lastname && !lastname.isBlank()){
            target.setLastname(lastname);
        }
        if(null != address && !address.isBlank()){
            target.setAddress(address);
        }
        if(null != password && !password.isBlank()){
            target.setPassword(password);
        }
        userRepository.save(target);
        return true;
    }
}
