package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.Home;
import gr.hua.dit.HomeSharing.entities.HomeCharacteristics;
import gr.hua.dit.HomeSharing.services.HomeService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("homes")
    public String getHomes(Model model) {
        try {
            List<Home> homes = homeService.getHomes();
            System.out.println("Fetched homes: " + homes);
            model.addAttribute("homes", homes);
            return "index"; // Return the index view template
        } catch (Exception e) {
            System.err.println("Error fetching homes: " + e.getMessage());
            model.addAttribute("error", "Unable to fetch homes.");
            return "index"; // Return the index view template with error message
        }
    }
    @Secured("ROLE_HOMEOWNER")
    @PostMapping
    public String addHome(@RequestParam int ownerId, Model model) {
        try {
            HomeCharacteristics characteristics = new HomeCharacteristics(
                    "123 Test Street",
                    "Test Area",
                    3,
                    2,
                    120,
                    2,
                    4
            );
            Home home = new Home();
            home.setCharacteristics(characteristics);
            home.setDailyPrice(100);
            int homeId = homeService.addHome(home, ownerId);
            System.out.println("New Home added: " + home);
            model.addAttribute("message", "Home added successfully with ID: " + homeId);
            return "index"; // Return the index view template with success message
        } catch (Exception e) {
            System.err.println("Error adding home: " + e.getMessage());
            model.addAttribute("error", "Unable to add home.");
            return "index"; // Return the index view template with error message
        }
    }

    @GetMapping("/{id}")
    public String getHomeById(@PathVariable int id, Model model) {
        try {
            Home home = homeService.getHome(id);
            if (home == null) {
                model.addAttribute("error", "Home not found.");
                return "index"; // Return the index view template with error message
            }
            System.out.println("Fetched home details: " + home);
            model.addAttribute("home", home);
            return "index"; // Return the index view template with home details
        } catch (Exception e) {
            System.err.println("Error fetching home by ID: " + e.getMessage());
            model.addAttribute("error", "Unable to fetch home details.");
            return "index"; // Return the index view template with error message
        }
    }
    @Secured("ROLE_HOMEOWNER")
    @DeleteMapping("/{id}")
    public String deleteHome(@PathVariable int id, Model model) {
        try {
            homeService.deleteHome(id);
                String message = "Deleted home with ID: " + id;
                System.out.println(message);
                model.addAttribute("message", message);
                return "index"; // Return the index view template with success message

        } catch (Exception e) {
            System.err.println("Error deleting home: " + e.getMessage());
            model.addAttribute("error", "Unable to delete home.");
            return "index"; // Return the index view template with error message
        }
    }
    @Secured("ROLE_HOMEOWNER")
    @GetMapping("/ownedhomes")
    public String getHomeByOwnerId(@RequestParam int ownerId, Model model) {
        try {
            List<Home> homes = homeService.getHomeByOwnerId(ownerId);
            if (homes == null || homes.isEmpty()) {
                model.addAttribute("error", "No homes found for this owner.");
                return "index"; // Return the index view template with error message
            }
            System.out.println("Fetched homes for owner ID " + ownerId + ": " + homes);
            model.addAttribute("homes", homes);
            return "index"; // Return the index view template with home details
        } catch (Exception e) {
            System.err.println("Error fetching homes by owner ID: " + e.getMessage());
            model.addAttribute("error", "Unable to fetch homes for the owner.");
            return "index"; // Return the index view template with error message
        }
    }
    @Secured("ROLE_HOMEOWNER")
    @GetMapping("/requests")
    public String getHomeRequests(Model model) {
        try {
            List<Home> homeRequests = homeService.getHomeRequests();
            return "index"; // Return the index view template with home requests
        } catch (Exception e) {
            System.err.println("Error fetching home requests: " + e.getMessage());
            model.addAttribute("error", "Unable to fetch home requests.");
            return "index"; // Return the index view template with error message
        }
    }

    //accept or decline home requests
    @Secured("ROLE_ADMIN")
    @PostMapping("/accept")
    public String acceptHomeRequest(@RequestParam int id, Model model) {
        try {
            homeService.acceptHomeRequest(id);
            String message = "Accepted home request with ID: " + id;
            System.out.println(message);
            model.addAttribute("message", message);
            return "index"; // Return the index view template with success message
        } catch (Exception e) {
            System.err.println("Error accepting home request: " + e.getMessage());
            model.addAttribute("error", "Unable to accept home request.");
            return "index"; // Return the index view template with error message
        }
    }
    @Secured("ROLE_ADMIN")
    @PostMapping("/decline")
    public String declineHomeRequest(@RequestParam int id, Model model) {
        try {
            homeService.declineHomeRequest(id);
            String message = "Declined home request with ID: " + id;
            System.out.println(message);
            model.addAttribute("message", message);
            return "index"; // Return the index view template with success message
        } catch (Exception e) {
            System.err.println("Error declining home request: " + e.getMessage());
            model.addAttribute("error", "Unable to decline home request.");
            return "index"; // Return the index view template with error message
        }
    }
}
