package de.haevn.distributed_systems.service;

import de.haevn.distributed_systems.model.Product;
import de.haevn.distributed_systems.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{
    public static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void save(Product product) {
        logger.info("Save product {}", product);
        productRepository.save(product);
    }

    @Override
    public void save(List<Product> products) {
        logger.info("Save products {}", products);
        productRepository.saveAll(products);
    }

    @Override
    public void update(Product product) {
        logger.info("Update product {}", product);
        updateProduct(product, Optional.of(product));
    }

    @Override
    public long update(List<Product> products) {
        logger.info("Update products {}", products);
        return products.stream()
                .filter(review -> review.getId() != null)
                .filter(target-> updateProduct(target, findById(target.getId())))
                .count();
    }

    @Override
    public void deleteById(Long id) {
        logger.warn("Delete product {}", id);
        productRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        logger.warn("Delete all products");
        productRepository.deleteAll();
    }





    private boolean updateProduct(Product received, Optional<Product> targetProduct){
        if(targetProduct.isEmpty()){
            logger.warn("Targeted product is empty");
            return false;
        }else if(productRepository.findById((received.getId())).isEmpty()){
            logger.warn("Product does not exists");
            return false;
        }
        var target = targetProduct.get();
        var name = received.getName();
        var brand = received.getBrand();
        var description = received.getDescription();
        var newPrice = received.getNewPrice();
        var oldPrice = received.getOldPrice();
        if(null != name && !name.isBlank()){
            target.setName(name);
        }
        if(null != brand && !brand.isBlank()){
            target.setBrand(brand);
        }
        if(null != description && !description.isBlank()){
            target.setDescription(description);
        }
        if(null != newPrice){
            target.setNewPrice(newPrice);
        }
        if(null != oldPrice){
            target.setOldPrice(oldPrice);
        }
        productRepository.save(target);
        return true;
    }
}
