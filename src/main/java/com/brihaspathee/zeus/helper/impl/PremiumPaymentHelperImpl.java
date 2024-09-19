package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import com.brihaspathee.zeus.domain.entity.PremiumPayment;
import com.brihaspathee.zeus.domain.repository.PremiumPaymentRepository;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;
import com.brihaspathee.zeus.helper.interfaces.PremiumPaymentHelper;
import com.brihaspathee.zeus.mapper.interfaces.PremiumPaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 19, July 2024
 * Time: 5:40 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumPaymentHelperImpl implements PremiumPaymentHelper {

    /**
     * Repository instance for premium payment
     */
    private final PremiumPaymentRepository premiumPaymentRepository;

    /**
     * Premium payment mapper instance
     */
    private final PremiumPaymentMapper premiumPaymentMapper;

    /**
     * Create premium payment for an enrollment span
     * @param enrollmentSpan
     * @param premiumPaymentDto
     * @return - Returns the list of premium payments received thus far
     */
    @Override
    public List<PremiumPayment> createPremiumPayment(EnrollmentSpan enrollmentSpan, PremiumPaymentDto premiumPaymentDto) {
        PremiumPayment payment = premiumPaymentMapper.premiumPaymentDtoToPremiumPayment(premiumPaymentDto);
        payment.setEnrollmentSpan(enrollmentSpan);
        premiumPaymentRepository.save(payment);
        List<PremiumPayment> payments = premiumPaymentRepository.findPremiumPaymentsByEnrollmentSpanCode(
                premiumPaymentDto.getEnrollmentSpanCode());
        return payments;
    }

    /**
     * Create the premium payment dto from premium payment entity and set it to the enrollment span dto
     * @param enrollmentSpanDto
     * @param premiumPayments
     */
    @Override
    public void setPremiumPayment(EnrollmentSpanDto enrollmentSpanDto, List<PremiumPayment> premiumPayments) {
        Set<PremiumPaymentDto> premiumPaymentDtos = new HashSet<>();
        premiumPayments.forEach(premiumPayment -> {
            PremiumPaymentDto premiumPaymentDto = premiumPaymentMapper.premiumPaymentToPremiumPaymentDto(premiumPayment);
            premiumPaymentDtos.add(premiumPaymentDto);
        });
        enrollmentSpanDto.setPremiumPayments(premiumPaymentDtos);
    }

    /**
     * Get premium payments for the enrollment span code
     * @param enrollmentSpanCode
     * @return
     */
    @Override
    public List<PremiumPayment> getPremiumPayments(String enrollmentSpanCode) {
        return premiumPaymentRepository.findPremiumPaymentsByEnrollmentSpanCode(enrollmentSpanCode);
    }
}
