package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.*;
import gr.hua.dit.HomeSharing.repositories.RoleRepository;
import gr.hua.dit.HomeSharing.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(User user) {
        String passwd= user.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        user.setPassword(encodedPassword);
        String roleName;
        if (user instanceof Administrator) {
            roleName = "ROLE_ADMIN";
        } else if (user instanceof HomeOwner) {
            roleName = "ROLE_HOMEOWNER";
        } else if (user instanceof Renter) {
            roleName = "ROLE_RENTER";
        } else {
            throw new RuntimeException("Error: User type is not found.");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public Integer updateUser(User user) {
        user = userRepository.save(user);
        return user.getId();
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // The username for us is the email
        Optional<User> opt = userRepository.findByEmail(email);

        if(opt.isEmpty())
            throw new UsernameNotFoundException("User with email: " + email +" not found !");
        else {
            User user = opt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.getRoles()
                            .stream()
                            .map(role-> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toSet())
            );
        }
    }

    @Transactional
    public Object getUsers() {
        return userRepository.findAll();
    }

    public Object getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional
    public void updateOrInsertRole(Role role) {
        roleRepository.updateOrInsert(role);
    }
}
