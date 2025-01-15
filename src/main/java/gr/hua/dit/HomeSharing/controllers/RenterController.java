package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.Home;
import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.entities.Renter;
//import gr.hua.dit.HomeSharing.entities.UserDetails;
import gr.hua.dit.HomeSharing.services.HomeService;
import gr.hua.dit.HomeSharing.services.RentalService;
import gr.hua.dit.HomeSharing.services.RenterService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/renter")
public class RenterController {
    RenterService renterService;
    RentalService rentalService;
    HomeService homeService;

    public RenterController(RenterService renterService, RentalService rentalService, HomeService homeService) {
        this.renterService = renterService;
        this.rentalService = rentalService;
        this.homeService = homeService;
    }

    @GetMapping
    public String getRenters(Model model) {
        List<Renter> renters = renterService.getRenters();
        System.out.print(renters);
        model.addAttribute("renters", renters);
        return "index";
    }

    @PostMapping("new")
    public String newRenter() {
        LocalDate birthDate = LocalDate.of(1995, 1, 1);
//        UserDetails userDetails = new UserDetails("John", "T", "john@gmail.com", "12345678", "1234567890", birthDate);
//        Renter renter = new Renter(userDetails);
//        renterService.createRenter(renter);
//        System.out.println(renter);
        System.out.println("New Renter request");
        System.out.println("Test");
        return "index";
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("{id}")
    public String deleteRenter(@PathVariable int id) {
        renterService.deleteRenter(id);
        System.out.println("Delete Renter request");
        return "index";
    }

    @GetMapping("{id}")
    public String getRenter(@PathVariable int id, Model model) {
        Renter renter = renterService.getRenter(id);
        System.out.println(renter);
        model.addAttribute("renter", renter);
        return "index";
    }

    @GetMapping("{id}/rentals")
    public String getRentals(@PathVariable int id, Model model) {
        Renter renter = renterService.getRenter(id);
        List<Rental> rentals = renter.getRentals();
        System.out.println(rentals);
        model.addAttribute("rentals", rentals);
        return "index";
    }

    @PostMapping("{id}/assignrental")
    public String assignRentalToRenter(@PathVariable int id, @RequestParam int homeId) {
        Rental rental = new Rental(LocalDate.now().plusDays(4), LocalDate.now().plusDays(10), 2, 100);
        renterService.rentHome(id, homeId, rental);
        System.out.println("Assign Rental request");
        System.out.println(rental);
        return "index";
    }
}

