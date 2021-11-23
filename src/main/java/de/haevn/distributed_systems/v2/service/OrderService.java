package de.haevn.distributed_systems.v2.service;

import de.haevn.distributed_systems.v2.model.Order;
import de.haevn.distributed_systems.v2.repository.OrderRepository;
import de.haevn.distributed_systems.v2.service.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(Order obj) {
        repository.save(obj);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(List<Order> objs) {
        repository.saveAll(objs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> update(Order obj) {
        return updateInternal(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long update(List<Order> objs) {
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
    public Optional<Order> updateInternal(Order input) {
        return Optional.empty();
    }
}
