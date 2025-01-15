package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.User;
import gr.hua.dit.HomeSharing.repositories.RoleRepository;
import gr.hua.dit.HomeSharing.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {
    private UserService userService;

    private RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }


//    @PostMapping("/register")
//    public String saveUser(@ModelAttribute User user, Model model){
//        System.out.println("Roles: "+user.getRoles());
//        Integer id = userService.saveUser(user);
//        String message = "User '"+id+"' saved successfully !";
//        model.addAttribute("msg", message);
//        return "index";
//    }

    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "index";
    }

    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable Long user_id, Model model){
        model.addAttribute("user", userService.getUser(user_id));
        return "index";
    }
}