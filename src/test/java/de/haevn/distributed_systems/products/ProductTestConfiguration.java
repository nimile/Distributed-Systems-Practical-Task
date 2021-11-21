package de.haevn.distributed_systems.products;

import de.haevn.distributed_systems.controller.ProductController;
import de.haevn.distributed_systems.model.Product;
import de.haevn.distributed_systems.repository.ProductRepository;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductTestConfiguration {
    protected final static List<Product> products = new ArrayList<>();
    static {
        products.add(Product.builder().name("Generic Mouse").brand("Generic Brand").description("Generic Description").newPrice(69.99).oldPrice(89.89).id(1L).build());
        products.add(Product.builder().name("Generic Keyboard").brand("Generic Brand").description("Generic Description").newPrice(129.99).oldPrice(179.89).id(2L).build());

        products.add(Product.builder().name("Ultra Mouse").brand("Ultra Brand").description("Generic Description").newPrice(129.99).oldPrice(179.89).id(3L).build());
        products.add(Product.builder().name("Ultra Keyboard").brand("Ultra Brand").description("Generic Description").newPrice(199.38).oldPrice(239.99).id(4L).build());
    }


    @Autowired
    public ProductController controller;

    @MockBean
    public ProductRepository repository;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;
}
