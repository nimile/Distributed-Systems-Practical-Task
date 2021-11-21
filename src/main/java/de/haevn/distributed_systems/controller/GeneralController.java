package de.haevn.distributed_systems.controller;


import de.haevn.distributed_systems.service.ProductService;
import de.haevn.distributed_systems.service.ReviewService;
import de.haevn.distributed_systems.service.UserService;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GeneralController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

}
