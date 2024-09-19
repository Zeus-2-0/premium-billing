package com.brihaspathee.zeus.service.interfaces;

import com.brihaspathee.zeus.dto.account.EnrollmentSpanStatusDto;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 25, July 2024
 * Time: 6:08 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface AccountProcessorService {

    /**
     * Get the status of the enrollment span
     * @param enrollmentSpanStatusDto - The object that contains the enrollment span for which the status is returned and any prior enrollment spans
     *                                that are present for the account
     * @return - returns the status of the enrollment span
     */
    String getEnrollmentSpanStatus(EnrollmentSpanStatusDto enrollmentSpanStatusDto);
}
