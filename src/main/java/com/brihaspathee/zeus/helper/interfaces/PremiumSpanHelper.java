package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.account.PremiumSpanDto;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:32 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface PremiumSpanHelper {

    /**
     * Create the premium spans for the enrollment span
     * @param enrollmentSpan - The enrollment spans to which the premium spans are associted
     * @param premiumSpanDtos - The premium spans to be created
     */
    void updatePremiumSpans(EnrollmentSpan enrollmentSpan,
                            List<PremiumSpanDto> premiumSpanDtos);

    /**
     * Create the premium span dto from premium span entity and set it to the enrollment span dto
     * @param enrollmentSpanDto
     * @param premiumSpans
     */
    void setPremiumSpan(EnrollmentSpanDto enrollmentSpanDto, List<PremiumSpan> premiumSpans);
}
