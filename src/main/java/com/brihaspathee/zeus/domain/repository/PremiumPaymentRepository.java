package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.PremiumPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 13, July 2024
 * Time: 5:44 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.repository
 * To change this template use File | Settings | File and Code Template
 */
@Repository
public interface PremiumPaymentRepository extends JpaRepository<PremiumPayment, UUID> {

    List<PremiumPayment> findPremiumPaymentsByEnrollmentSpanCode(String enrollmentSpanCode);
}
