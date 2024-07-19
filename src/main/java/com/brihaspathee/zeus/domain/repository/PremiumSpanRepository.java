package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 27, April 2024
 * Time: 6:10 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.repository
 * To change this template use File | Settings | File and Code Template
 */
@Repository
public interface PremiumSpanRepository extends JpaRepository<PremiumSpan, UUID> {

    /**
     * Find the premium span by premium span code
     * @param premiumSpanCode - the code by which the premium span is to be matched
     * @return - Matched premium span
     */
    Optional<PremiumSpan> findPremiumSpanByPremiumSpanCode(String premiumSpanCode);
}
