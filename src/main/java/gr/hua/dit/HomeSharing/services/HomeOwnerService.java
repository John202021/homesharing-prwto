package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.Home;
import gr.hua.dit.HomeSharing.entities.HomeOwner;
import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.repositories.HomeOwnerRepository;
import gr.hua.dit.HomeSharing.repositories.HomeRepository;
import gr.hua.dit.HomeSharing.repositories.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomeOwnerService {
    private HomeOwnerRepository homeOwnerRepository;
    private RentalRepository rentalRepository;
    private HomeRepository homeRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public HomeOwnerService(HomeOwnerRepository homeOwnerRepository, RentalRepository rentalRepository, HomeRepository homeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.homeOwnerRepository = homeOwnerRepository;
        this.rentalRepository = rentalRepository;
        this.homeRepository = homeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<HomeOwner> getHomeOwners() {
        return homeOwnerRepository.findAll();
    }

    @Transactional
    public void updateHomeOwner(HomeOwner homeOwner) {
        if (!homeOwnerRepository.existsById(homeOwner.getId())) {
            throw new RuntimeException("HomeOwner with id " + homeOwner.getId() + " not found");
        }
        homeOwnerRepository.save(homeOwner);
    }

    @Transactional
    public HomeOwner getHomeOwner(int id) {
        return homeOwnerRepository.findById(id).get();
    }

    @Transactional
    public void deleteHomeOwner(int id) {
        homeOwnerRepository.deleteById(id);
    }

    //requests
    //helper function
    @Transactional
    public boolean hasRentalRequest(int homeOwnerId, Rental rental){
        HomeOwner homeOwner = getHomeOwner(homeOwnerId);
        List<Home> homes = homeOwner.getOwnedHomes();
        for (Home home : homes){
            List<Rental> rentals = home.getRentals();
            for (Rental rentalEl : rentals){
                if (rentalEl.equals(rental)){
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    public void accceptRentalRequest(int homeOwnerId, Rental rental){
        if (hasRentalRequest(homeOwnerId, rental)){
            rental.setAccepted(true);
            rental.setRequestProcessedAt(LocalDateTime.now());
            rentalRepository.save(rental);
        }else{
        throw new RequestRejectedException("Rental request doesn't belong to owner ");
        }
    }

    @Transactional
    public void declineRentalRequest(int homeOwnerId, Rental rental){
        if (hasRentalRequest(homeOwnerId, rental)){
            rental.setAccepted(false);
            rental.setRequestProcessedAt(LocalDateTime.now());
            rentalRepository.save(rental);
        }else{
            throw new RequestRejectedException("Rental request doesn't belong to owner");
        }
    }

    @Transactional
    public List<Rental> viewRentalRequests(int homeOwnerId){
        List<Home> ownedHomes = getHomeOwner(homeOwnerId).getOwnedHomes();
        List<Rental> rentalRequests = new ArrayList<>();
        for (Home home : ownedHomes){
            List<Rental> rentals = home.getRentals();
            for (Rental rental : rentals){
                if (rental.getAccepted() == null){
                    rentalRequests.add(rental);
                }
            }
        }
        return rentalRequests;
    }

    @Transactional
    public Home addHomeForOwner(int homeOwnerId, Home home){
        HomeOwner owner = getHomeOwner(homeOwnerId);
        home.setHomeOwner(owner);
        return homeRepository.save(home);
    }
}

