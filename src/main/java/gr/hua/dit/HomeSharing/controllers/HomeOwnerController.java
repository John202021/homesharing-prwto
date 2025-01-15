package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.HomeOwner;
import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.entities.Renter;
import gr.hua.dit.HomeSharing.services.HomeOwnerService;
import gr.hua.dit.HomeSharing.services.RenterService;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/homeowner")
public class HomeOwnerController {
    HomeOwnerService homeOwnerService;

    public HomeOwnerController(HomeOwnerService homeOwnerService) {
        this.homeOwnerService = homeOwnerService;
    }

    @GetMapping
    public String getHomeOwners(Model model) {
        List<HomeOwner> homeOwners = homeOwnerService.getHomeOwners();
        System.out.println(homeOwners);
        model.addAttribute("homeOwners", homeOwners);
        return "index";
    }

    @PostMapping("new")
    public String newHomeOwner(Model model) {
//test
//        HomeOwner homeOwner = new HomeOwner(new UserDetails("John", "Galliano",
//                                            "johnghallia@gmail.com", "564321",
//                                            "6988803226",
//                                            LocalDate.of(1950,11,28)));
//        homeOwnerService.createHomeOwner(homeOwner);
        System.out.println("New HomeOwner request");
        System.out.println("Test");
        return "index";
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("homeowners/{homeOwnerId}")
    public String deleteHomeOwner(@PathVariable int homeOwnerId) {
        homeOwnerService.deleteHomeOwner(homeOwnerId);
        System.out.println("Delete HomeOwner request");
        return "index";
    }

}
