package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 9:32 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.repository
 * To change this template use File | Settings | File and Code Template
 */
@Repository
public interface PayloadTrackerRepository extends JpaRepository<PayloadTracker, UUID> {

    /**
     * Find payload tracker by payload id
     * @param payloadId
     * @return
     */
    Optional<PayloadTracker> findPayloadTrackerByPayloadId(String payloadId);
}
