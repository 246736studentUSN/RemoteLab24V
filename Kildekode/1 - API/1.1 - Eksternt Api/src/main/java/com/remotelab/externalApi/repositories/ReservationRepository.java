package com.remotelab.externalApi.repositories;

import com.remotelab.externalApi.domain.entities.ReservationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// crud repository gives CRUD-functionality towards database
public interface ReservationRepository extends CrudRepository<ReservationEntity, Long> {

    // defining a custom method to be performed towards database
    Optional<ReservationEntity> findReservationByUserEmail(String userEmail);

    // defining a custom method to be performed towards database
    @Transactional
    void deleteByUserEmail(String userEmail);

    // defining a custom query to be performed towards database
    @Query("SELECT u FROM ReservationEntity u WHERE u.start = ?1")
    List<ReservationEntity> findNumberOfEntriesFromStartTime(LocalDateTime startTime);
}
