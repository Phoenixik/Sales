package controllers;

import Entities.Customer;
import Entities.Orders;
import Entities.Product;
import Repos.CustomerRepository;
import Repos.OrdersRepository;
import Repos.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class homecontrol {


    Product prod = new Product();
    Customer cus = new Customer();

    @Autowired
    ProductRepository prepo;


    @Autowired
    CustomerRepository crepo;
    @Autowired
    OrdersRepository orepo;

    @Autowired
    private KafkaTemplate<String, Customer> kafkatemplate;

    private static final String TOPIC = "KafkaExample";

    @RequestMapping("/home")
    public String homepage() {

        return "home_page";
    }

    @RequestMapping("/create")
    public String createpage(@ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("error .." + bindingResult);
        }
        model.addAttribute("product", product);
            return "create_page";

    }
    @RequestMapping("/saved")
    public String savs(@ModelAttribute Customer customer, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("error .." + bindingResult);
        }
        model.addAttribute("customer", customer);

        model.addAttribute("order", new Orders() );
        return "saved";
    }

    @PostMapping(value = "/api/home/orders")
    public String order( @RequestParam String cuname, @RequestParam Long pnumber, @RequestParam Integer prodid, Model mv) {
        cus.setCuname(cuname);
        cus.setPnumber(pnumber);
        Orders od = new Orders();
        mv.addAttribute("order", new Orders());
        od.getId();
        System.out.println(prodid);
        System.out.println(myfind(prodid));
        if(myfind(prodid) == 1) {
            System.out.println(od.getId());
            orepo.save(od);
            crepo.save(cus);

            kafkatemplate.send(TOPIC, new Customer(cuname, pnumber, prodid));


            System.out.println("processing");
            return "ordered";
        }
        else {
            return "error";
        }

    }

    public int myfind(Integer id ) {
        System.out.println(id);
        Boolean exists = prepo.existsById(id);
//        Product dataprod= prepo.findById(id).get();
        System.out.println(id);
//        if (dataprod.getId().equals(cus.getProdid())) {
        if(exists==true) {
         return 1;
        }
        return 0;
    }


}
