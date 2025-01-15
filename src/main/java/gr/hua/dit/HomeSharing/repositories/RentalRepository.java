package gr.hua.dit.HomeSharing.repositories;

import gr.hua.dit.HomeSharing.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByAcceptedIsNullAndRenterId(int renterId);
    List<Rental> findByAcceptedAndRenterId(Boolean accepted, int renterId);
    List<Rental> findByRenterId(int renterId);
    List<Rental> findByHomeId(int homeId);
    List<Rental> findByEndDateBefore(LocalDate endDate);
    List<Rental> findByStartDateAfter(LocalDate startDate);
}
