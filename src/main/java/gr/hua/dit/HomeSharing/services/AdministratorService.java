package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.*;
import gr.hua.dit.HomeSharing.repositories.AdministratorRepository;
import gr.hua.dit.HomeSharing.repositories.HomeOwnerRepository;
import gr.hua.dit.HomeSharing.repositories.HomeRepository;
import gr.hua.dit.HomeSharing.repositories.RenterRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdministratorService {
    private AdministratorRepository administratorRepository;
    private HomeRepository homeRepository;
    private RenterRepository renterRepository;
    private HomeOwnerRepository homeOwnerRepository;

    public AdministratorService(AdministratorRepository administratorRepository, HomeRepository homeRepository, RenterRepository renterRepository, HomeOwnerRepository homeOwnerRepository) {
        this.administratorRepository = administratorRepository;
        this.homeRepository = homeRepository;
        this.renterRepository = renterRepository;
        this.homeOwnerRepository = homeOwnerRepository;
    }

    @Transactional
    public List<Administrator> getAdministrators() {
        return administratorRepository.findAll();
    }

    @Transactional
    public Administrator getAdministrator(int id) {
        return administratorRepository.findById(id).get();
    }

    @Transactional
    public void deleteAdministrator(int id) {
        administratorRepository.deleteById(id);
    }

    //unused
    @Transactional
    public void acceptHomeRequest(int id, Home home){
        //an o admin einai egkyros
        if (administratorRepository.existsById(id)){
            home.setAccepted(true);
            home.setRequestProcessedAt(LocalDateTime.now());
            homeRepository.save(home);
        }else{
            throw new NoSuchElementException();
        }
    }

    @Transactional
    public void declineHomeRequest(int id, Home home){
        if (administratorRepository.existsById(id)){
            home.setAccepted(false);
            home.setRequestProcessedAt(LocalDateTime.now());
            homeRepository.save(home);
        }else{
            throw new NoSuchElementException();
        }
    }

    @Transactional
    public List<Renter> viewNewRenters(int id, Home home){
        if (administratorRepository.existsById(id)){
            List<Renter> renters = null;
            for (Rental rental : home.getRentals()){
                renters.add(rental.getRenter());
            }
            return renters;
        }else{
            throw new NoSuchElementException();
        }
    }

    //@Transactional
    //public void verifyRenter(){}

    @Transactional
    public void deleteRenter(int id, Renter renter){
        if (administratorRepository.existsById(id)){
            renterRepository.deleteById(renter.getId());
        }else{
            throw new NoSuchElementException();
        }
    }

    @Transactional
    public void deleteHomeOwner(int id, HomeOwner homeOwner){
        if (administratorRepository.existsById(id)){
            homeOwnerRepository.deleteById(homeOwner.getId());
        }else{
            throw new NoSuchElementException();
        }
    }


//ponao
}
