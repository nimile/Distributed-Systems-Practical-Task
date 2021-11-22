package de.haevn.distributed_systems.v2.repository;

import de.haevn.distributed_systems.v2.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, Long> {
    Optional<Product> findById(Long id);
    List<Product> findAll();
}
