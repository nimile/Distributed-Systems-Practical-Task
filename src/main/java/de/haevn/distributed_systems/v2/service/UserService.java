package de.haevn.distributed_systems.v2.service;

import de.haevn.distributed_systems.v2.exceptions.ExistenceException;
import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.repository.UserRepository;
import de.haevn.distributed_systems.v2.utils.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean save(User obj) {
        if(repository.findByEmail(obj.getEmail()).isEmpty()){
            repository.save(obj);
            return true;
        }
        return false;
    }

    @Override
    public void save(List<User> objs) {
        repository.saveAll(objs);
    }

    @Override
    public Optional<User> update(User obj) {
        return updateInternal(obj);
    }

    @Override
    public Long update(List<User> objs) {
        long counter;
        counter = objs.stream()
                .filter(obj -> obj.getId() != null)
                .filter(obj -> repository.findById(obj.getId()).isPresent())
                .filter(obj -> updateInternal(obj).isPresent())
                .count();
        return counter;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(User obj) {
        repository.delete(obj);
    }

    @Override
    public void delete() {
        repository.deleteAll();
    }

    @Override
    public Optional<User> findByEmail(String mail) {
        return repository.findByEmail(mail);
    }

    @Override
    public User save(String firstName, String lastName, String email, String address, String password) throws ExistenceException, ArgumentMismatchException {
        return null;
    }

    private Optional<User> updateInternal(User input){
        var repositoryUser = repository.findById(input.getId());
        if(repositoryUser.isEmpty()){
            return Optional.empty();
        }

        var firstname = input.getFirstname();
        var lastname = input.getLastname();
        var address = input.getAddress();
        var password = input.getPassword();

        if(null != firstname && !firstname.isBlank()){
            repositoryUser.get().setFirstname(firstname);
        }
        if(null != lastname && !lastname.isBlank()){
            repositoryUser.get().setLastname(lastname);
        }
        if(null != address && !address.isBlank()){
            repositoryUser.get().setAddress(address);
        }
        if(null != password && !password.isBlank()){
            repositoryUser.get().setPassword(password);
        }
        repository.save(repositoryUser.get());
        return repositoryUser;
    }
}