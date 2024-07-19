package com.brihaspathee.zeus.mapper.impl;

import com.brihaspathee.zeus.domain.entity.PremiumPayment;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;
import com.brihaspathee.zeus.mapper.interfaces.PremiumPaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 13, July 2024
 * Time: 5:50 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.mapper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumPaymentMapperImpl implements PremiumPaymentMapper {

    /**
     * Convert premium payment dto to premium payment entity
     * @param premiumPaymentDto
     * @return
     */
    @Override
    public PremiumPayment premiumPaymentDtoToPremiumPayment(PremiumPaymentDto premiumPaymentDto) {
        if(premiumPaymentDto == null){
            return null;
        }
        PremiumPayment payment = PremiumPayment.builder()
                .enrollmentSpanCode(premiumPaymentDto.getEnrollmentSpanCode())
                .paymentDate(premiumPaymentDto.getPaymentDate())
                .premiumPayment(premiumPaymentDto.getPaymentAmount())
                .build();
        return payment;
    }

    /**
     * Convert premium payment entity to premium payment dto
     * @param premiumPayment
     * @return
     */
    @Override
    public PremiumPaymentDto premiumPaymentToPremiumPaymentDto(PremiumPayment premiumPayment) {
        if(premiumPayment == null){
            return null;
        }
        PremiumPaymentDto paymentDto = PremiumPaymentDto.builder()
                .enrollmentSpanCode(premiumPayment.getEnrollmentSpanCode())
                .paymentDate(premiumPayment.getPaymentDate())
                .paymentAmount(premiumPayment.getPremiumPayment())
                .build();
        return paymentDto;
    }
}
