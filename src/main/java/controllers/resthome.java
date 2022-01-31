package controllers;

import Entities.Product;
import Repos.ProductRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class resthome {

    @Autowired
    ProductRepository prepo;

    Product prod =  new Product();

    @GetMapping("/retrieve")
    public List<Product> allretrieve() {
        System.out.println(prepo.findAll());
        return prepo.findAll();
    }

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void send(@RequestParam String name, @RequestParam String description, @RequestParam Integer price) {

        System.out.println("processing");
        prod.setName(name);
        prod.setDescription(description);
        prod.setPrice(price);
         prepo.save(prod);

//        System.out.println("done");
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Product myupdate(@PathVariable Integer id,  @RequestBody Product prod) {
        Product existinngprod= prepo.findById(id).get();
        BeanUtils.copyProperties(prod, existinngprod, "id");
        System.out.println("done");
        return prepo.saveAndFlush(existinngprod);
    }

}
