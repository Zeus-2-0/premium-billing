package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import com.brihaspathee.zeus.domain.entity.PremiumPayment;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 19, July 2024
 * Time: 5:40 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface PremiumPaymentHelper {

    /**
     * Create the premium payment received for an enrollment span
     * @param enrollmentSpan
     * @param premiumPaymentDto
     * @return - returns the list of premium payments that were received for the enrollment span
     */
    List<PremiumPayment> createPremiumPayment(EnrollmentSpan enrollmentSpan,
                                              PremiumPaymentDto premiumPaymentDto);

    /**
     * Create the premium payment dto from premium payment entity and set it to the enrollment span dto
     * @param enrollmentSpanDto
     * @param premiumPayments
     */
    void setPremiumPayment(EnrollmentSpanDto enrollmentSpanDto, List<PremiumPayment> premiumPayments);
}
