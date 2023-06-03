package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Request;

import javax.transaction.Transactional;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserId(Long userId);

    List<Request> findByEventId(Long eventId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update Requests set status = 'REJECTED' where status = 'PENDING' AND event_id" +
            " = :eventId")
    void setRejectedStatusToPendingRequests(Long eventId);

    @Query(nativeQuery = true, value = "select count(*) from Requests where status = 'APPROVED' and event_id = " +
            ":eventId")
    long countApprovedRequests(long eventId);
}