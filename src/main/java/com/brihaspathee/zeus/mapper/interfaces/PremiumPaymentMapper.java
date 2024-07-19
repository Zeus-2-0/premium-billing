package com.brihaspathee.zeus.mapper.interfaces;

import com.brihaspathee.zeus.domain.entity.PremiumPayment;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 13, July 2024
 * Time: 5:49 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.mapper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface PremiumPaymentMapper {

    /**
     * Convert premium payment dto tp premium payment entity
     * @param premiumPaymentDto
     * @return
     */
    PremiumPayment premiumPaymentDtoToPremiumPayment(PremiumPaymentDto premiumPaymentDto);

    /**
     * Convert premium payment entity to premium payment dto
     * @param premiumPayment
     * @return
     */
    PremiumPaymentDto premiumPaymentToPremiumPaymentDto(PremiumPayment premiumPayment);
}
