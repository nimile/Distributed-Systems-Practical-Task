package de.haevn.distributed_systems.v2.service;

import de.haevn.distributed_systems.v2.model.Product;
import de.haevn.distributed_systems.v2.repository.ProductRepository;
import de.haevn.distributed_systems.v2.service.interfaces.IProductService;
import de.haevn.distributed_systems.v2.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository repository;


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(Product obj) {
        repository.save(obj);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(List<Product> objs) {
        repository.saveAll(objs);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> update(Product obj) {
        return updateInternal(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long update(List<Product> objs) {
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
    public Optional<Product> updateInternal(Product input) {
        var repositoryResult = repository.findById(input.getId());
        if(repositoryResult.isEmpty()){
            return Optional.empty();
        }

        var name = input.getName();
        var brand = input.getBrand();
        var newPrice = input.getNewPrice();
        var oldPrice = repositoryResult.get().getNewPrice();

        if(AppUtils.isStringNeitherNullNorEmpty(name)){
            repositoryResult.get().setName(name);
        }
        if(AppUtils.isStringNeitherNullNorEmpty(brand)){
            repositoryResult.get().setBrand(brand);
        }
        if(null != newPrice){
            repositoryResult.get().setNewPrice(newPrice);
            repositoryResult.get().setOldPrice(oldPrice);
        }

        repository.save(repositoryResult.get());
        return repositoryResult;
    }
}
