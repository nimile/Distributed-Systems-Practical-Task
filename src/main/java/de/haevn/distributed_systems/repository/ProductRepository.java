package de.haevn.distributed_systems.repository;

import de.haevn.distributed_systems.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, Long> {
    Optional<Product> findById(Long id);

    List<Product> findByName(String name);
    List<Product> findByBrand(String brand);
    List<Product> findAll();
}
