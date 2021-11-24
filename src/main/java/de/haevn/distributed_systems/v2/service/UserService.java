package de.haevn.distributed_systems.v2.service;

import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.repository.UserRepository;
import de.haevn.distributed_systems.v2.service.interfaces.IUserService;
import de.haevn.distributed_systems.v2.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(User obj) {
        if(repository.findByEmail(obj.getEmail()).isEmpty()){
            repository.save(obj);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(List<User> objs) {
        repository.saveAll(objs);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> update(User obj) {
        return updateInternal(obj);
    }

    /**
     * {@inheritDoc}
     */
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
    public Optional<User> findByEmail(String mail) {
        return repository.findByEmail(mail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> updateInternal(User input){
        var repositoryResult = repository.findById(input.getId());
        if(repositoryResult.isEmpty()){
            return Optional.empty();
        }

        var firstname = input.getFirstname();
        var lastname = input.getLastname();
        var address = input.getAddress();
        var password = input.getPassword();

        if(AppUtils.isStringNeitherNullNorEmpty(firstname)){
            repositoryResult.get().setFirstname(firstname);
        }
        if(AppUtils.isStringNeitherNullNorEmpty(lastname)){
            repositoryResult.get().setLastname(lastname);
        }
        if(AppUtils.isStringNeitherNullNorEmpty(address)){
            repositoryResult.get().setAddress(address);
        }
        if(AppUtils.isStringNeitherNullNorEmpty(password)){
            repositoryResult.get().setPassword(password);
        }
        repository.save(repositoryResult.get());
        return repositoryResult;
    }
}
