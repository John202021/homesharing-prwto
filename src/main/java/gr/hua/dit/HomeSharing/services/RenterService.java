package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.Home;
import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.entities.Renter;
import gr.hua.dit.HomeSharing.repositories.HomeRepository;
import gr.hua.dit.HomeSharing.repositories.RentalRepository;
import gr.hua.dit.HomeSharing.repositories.RenterRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RenterService {
    private RenterRepository renterRepository;
    private RentalRepository rentalRepository;
    private HomeRepository homeRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public RenterService(RenterRepository renterRepository, RentalRepository rentalRepository, HomeRepository homeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.renterRepository = renterRepository;
        this.rentalRepository = rentalRepository;
        this.homeRepository = homeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<Renter> getRenters() {
        return renterRepository.findAll();
    }

    @Transactional
    public void updateRenter(Renter renter) {
        if (!renterRepository.existsById(renter.getId())) {
            throw new RuntimeException("Renter with id " + renter.getId() + " not found");
        }
        renterRepository.save(renter);
    }

    @Transactional
    public Renter getRenter(int id) {
        return renterRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Renter with id " + id + " not found")
        );
    }

    @Transactional
    public Boolean existsRenter(int id) {
        return renterRepository.existsById(id);
    }

    @Transactional
    public void deleteRenter(int id) {
        if (!this.existsRenter(id)) {
            throw new RuntimeException("Renter with id " + id + " not found");
        }
        renterRepository.deleteById(id);
    }

    @Transactional
    public void rentHome(int renter_id, int home_id, Rental rental) {
        Renter renter = this.getRenter(renter_id);
        Home home = homeRepository.findById(home_id).orElseThrow(
                () -> new RuntimeException("Home with id " + home_id + " not found")
        );
        rental.setRenter(renter);
        rental.setHome(home);
        renter.addRental(rental);
        this.updateRenter(renter);
    }
    //@Transactional
    //public void verifyRenter(){}

}
