package de.haevn.distributed_systems.service;

import de.haevn.distributed_systems.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    Optional<Product> findById(Long id);
    List<Product> findByName(String name);
    List<Product> findByBrand(String brand);
    List<Product> findAll();

    void save(Product product);

    void save(List<Product> products);

    void update(Product product);

    long update(List<Product> products);

    void deleteById(Long id);

    void deleteAll();
}
