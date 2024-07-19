package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 27, April 2024
 * Time: 6:09 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.repository
 * To change this template use File | Settings | File and Code Template
 */
@Repository
public interface EnrollmentSpanRepository extends JpaRepository<EnrollmentSpan, UUID> {

    /**
     * Find enrollment span by span code
     * @param enrollmentSpanCode - the enrollment span code that is to be matched
     * @return - return the matched enrollment span
     */
    Optional<EnrollmentSpan> findEnrollmentSpanByEnrollmentSpanCode(String enrollmentSpanCode);
}
