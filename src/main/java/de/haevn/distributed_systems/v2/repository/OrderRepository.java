package de.haevn.distributed_systems.v2.repository;

import de.haevn.distributed_systems.v2.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, Long> {
    Optional<Order> findById(Long id);
    List<Order> findAll();
}
