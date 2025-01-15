package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.HomeOwner;
import gr.hua.dit.HomeSharing.entities.Renter;
import gr.hua.dit.HomeSharing.repositories.HomeOwnerRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import gr.hua.dit.HomeSharing.entities.Home;
import gr.hua.dit.HomeSharing.repositories.HomeRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import gr.hua.dit.HomeSharing.entities.Rental;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final HomeOwnerRepository homeOwnerRepository;

    public HomeService(HomeRepository homeRepository, HomeOwnerRepository homeOwnerRepository) {
        this.homeRepository = homeRepository;
        this.homeOwnerRepository = homeOwnerRepository;
    }


    @Transactional
    public void saveHome(Home home) {
        homeRepository.save(home);
    }


    @Transactional
    public void deleteHome(int id) {
        if (!homeRepository.existsById(id)) {
            throw new RuntimeException("Home not found with id: " + id);
        }
        homeRepository.deleteById(id);
    }

    @Transactional
    public int updateHome(Home home) {
        if (!homeRepository.existsById(home.getId())) {
            throw new RuntimeException("Home not found with id: " + home.getId());
        }
        homeRepository.save(home);
        return home.getId();
    }

    @Transactional
    public int addHome(Home home, int ownerId) {
        HomeOwner owner = homeOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("HomeOwner not found with id: " + ownerId));
        home.setHomeOwner(owner);
        homeRepository.save(home);
        return home.getId();
    }


    @Transactional
    public void acceptHomeRequest(int homeId){
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found with id: " + homeId));
        if (home.getAccepted() != null) {
            throw new RuntimeException("Home request already processed");
        }
        home.setAccepted(true);
        home.setRequestProcessedAt(LocalDateTime.now());
        homeRepository.save(home);
    }

    //@Secured("ROLE_ADMIN")
    @Transactional
    public void declineHomeRequest(int homeId){
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found with id: " + homeId));
        if (home.getAccepted() != null) {
            throw new RuntimeException("Home request already processed");
        }
        home.setAccepted(false);
        home.setRequestProcessedAt(LocalDateTime.now());
        homeRepository.save(home);
    }

    @Transactional
    public List<Home> getAcceptedHomes(){
        return homeRepository.findByAccepted(true);
    }

    public List<Home> getHomeRequests() {
        return homeRepository.findByAcceptedIsNull();
    }

    @Transactional
    public List<Home> getHomeByOwnerId(int ownerId) {
        HomeOwner owner = homeOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("HomeOwner not found with id: " + ownerId));
        return owner.getHomes();
    }

    @Transactional
    public Home getHome(int id) {
        return homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found with id: " + id));
    }

    @Transactional
    public List<Home> getHomes() {
        return homeRepository.findAll();
    }

}
