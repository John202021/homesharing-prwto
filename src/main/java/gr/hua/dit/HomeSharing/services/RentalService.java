package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.repositories.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {
    private RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Transactional
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    @Transactional
    public void saveRental(Rental rental) {
        rentalRepository.save(rental);
    }

    @Transactional
    public Rental getRental(int id) {
        return rentalRepository.findById(id).get();
    }

    @Transactional
    public void deleteRental(int id) {
        rentalRepository.deleteById(id);
    }

    @Transactional
    public void acceptRentalRequest(int id) {
        Rental rental = this.getRental(id);
        if (rental.getAccepted() != null) {
            throw new RuntimeException("Rental request already processed");
        }
        rental.setAccepted(true);
        rental.setRequestProcessedAt(LocalDateTime.now());
        rentalRepository.save(rental);
    }

    @Transactional
    public void rejectRentalRequest(int id) {
        Rental rental = this.getRental(id);
        if (rental.getAccepted() != null) {
            throw new RuntimeException("Rental request already processed");
        }
        rental.setAccepted(false);
        rental.setRequestProcessedAt(LocalDateTime.now());
        rentalRepository.save(rental);
    }

    @Transactional
    public List<Rental> getPendingRentals(int renterId) {
        return rentalRepository.findByAcceptedIsNullAndRenterId(renterId);
    }

    @Transactional
    public List<Rental> getAcceptedRentals(int renterId) {
        return rentalRepository.findByAcceptedAndRenterId(true, renterId);
    }

    @Transactional
    public List<Rental> getRejectedRentals(int renterId) {
        return rentalRepository.findByAcceptedAndRenterId(false, renterId);
    }

    @Transactional
    public List<Rental> getRentalsByRenter(int renterId) {
        return rentalRepository.findByRenterId(renterId);
    }

    @Transactional
    public List<Rental> getRentalsByHome(int homeId) {
        return rentalRepository.findByHomeId(homeId);
    }

    @Transactional
    public List<Rental> getPastRentals() {
        return rentalRepository.findByEndDateBefore(LocalDate.now());
    }

    @Transactional
    public List<Rental> getFutureRentals() {
        return rentalRepository.findByStartDateAfter(LocalDate.now());
    }
}
