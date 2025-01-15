package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.services.RentalService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rental")
public class RentalController {
    private RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("{id}")
    public String getRental(@PathVariable int id) {
        Rental rental = rentalService.getRental(id);
        System.out.println(rental);
        return "index";
    }

    @Secured("ROLE_RENTER")
    @GetMapping("myrentals")
    public String getRentalsByRenter(@RequestParam int renterId, Model model) {
        List<Rental> rentals = rentalService.getRentalsByRenter(renterId);
        System.out.println(rentals);
        model.addAttribute("rentals", rentals);
        return "index";
    }

    @Secured("ROLE_RENTER")
    @GetMapping("myrentals/accepted")
    public String getAcceptedRentalsByRenter(@RequestParam int renterId, Model model) {
        List<Rental> rentals = rentalService.getAcceptedRentals(renterId);
        System.out.println(rentals);
        model.addAttribute("rentals", rentals);
        return "index";
    }

    @Secured("ROLE_RENTER")
    @GetMapping("myrentals/pending")
    public String getPendingRentalsByRenter(@RequestParam int renterId, Model model) {
        List<Rental> rentals = rentalService.getPendingRentals(renterId);
        System.out.println(rentals);
        model.addAttribute("rentals", rentals);
        return "index";
    }

    @Secured("ROLE_RENTER")
    @GetMapping("myrentals/rejected")
    public String getRejectedRentalsByRenter(@RequestParam int renterId, Model model) {
        List<Rental> rentals = rentalService.getRejectedRentals(renterId);
        System.out.println(rentals);
        model.addAttribute("rentals", rentals);
        return "index";
    }

    @GetMapping("home")
    public String getRentalsByHome(@RequestParam int homeId, Model model) {
        List<Rental> rentals = rentalService.getRentalsByHome(homeId);
        System.out.println(rentals);
        model.addAttribute("rentals", rentals);
        return "index";
    }

    @Secured("ROLE_HOMEOWNER")
    @PatchMapping("/accept")
    public String acceptRentalRequest(@RequestParam int rental_id) {
        rentalService.acceptRentalRequest(rental_id);
        System.out.println("Rental request accepted");
        return "index";
    }

    @Secured("ROLE_HOMEOWNER")
    @PatchMapping("/reject")
    public String rejectRentalRequest(@RequestParam int rental_id) {
        rentalService.rejectRentalRequest(rental_id);
        System.out.println("Rental request rejected");
        return "index";
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("{id}")
    public String deleteRental(@PathVariable int id) {
        rentalService.deleteRental(id);
        System.out.println("Rental deleted");
        return "index";
    }
}
