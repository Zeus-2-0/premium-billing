package com.brihaspathee.zeus.mapper.impl;

import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.mapper.interfaces.EnrollmentSpanMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:21 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.mapper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentSpanMapperImpl implements EnrollmentSpanMapper {

    /**
     * Convert enrollment span dto to enrollment span entity
     * @param enrollmentSpanDto
     * @return
     */
    @Override
    public EnrollmentSpan enrollmentSpanDtoToEnrollmentSpan(EnrollmentSpanDto enrollmentSpanDto) {
        if(enrollmentSpanDto == null){
            return null;
        }
        EnrollmentSpan enrollmentSpan = EnrollmentSpan.builder()
                .enrollmentSpanSK(enrollmentSpanDto.getEnrollmentSpanSK())
                .enrollmentSpanCode(enrollmentSpanDto.getEnrollmentSpanCode())
                .enrollmentType(enrollmentSpanDto.getEnrollmentType())
                .stateTypeCode(enrollmentSpanDto.getStateTypeCode())
                .marketplaceTypeCode(enrollmentSpanDto.getMarketplaceTypeCode())
                .businessUnitTypeCode(enrollmentSpanDto.getBusinessUnitTypeCode())
                .coverageTypeCode(enrollmentSpanDto.getCoverageTypeCode())
                .startDate(enrollmentSpanDto.getStartDate())
                .endDate(enrollmentSpanDto.getEndDate())
                .exchangeSubscriberId(enrollmentSpanDto.getExchangeSubscriberId())
                .effectuationDate(enrollmentSpanDto.getEffectuationDate())
                .planId(enrollmentSpanDto.getPlanId())
                .groupPolicyId(enrollmentSpanDto.getGroupPolicyId())
                .productTypeCode(enrollmentSpanDto.getProductTypeCode())
                .delinqInd(enrollmentSpanDto.isDelinqInd())
                .paidThroughDate(enrollmentSpanDto.getPaidThroughDate())
                .claimPaidThroughDate(enrollmentSpanDto.getClaimPaidThroughDate())
                .statusTypeCode(enrollmentSpanDto.getStatusTypeCode())
                .effectiveReason(enrollmentSpanDto.getEffectiveReason())
                .termReason(enrollmentSpanDto.getTermReason())
                .build();
        return enrollmentSpan;
    }

    /**
     * Convert enrollment span entity to enrollment span dto
     * @param enrollmentSpan
     * @return
     */
    @Override
    public EnrollmentSpanDto enrollmentSpanToEnrollmentSpanDto(EnrollmentSpan enrollmentSpan) {
        if(enrollmentSpan == null){
            return null;
        }
        EnrollmentSpanDto enrollmentSpanDto = EnrollmentSpanDto.builder()
                .enrollmentSpanSK(enrollmentSpan.getEnrollmentSpanSK())
                .enrollmentSpanCode(enrollmentSpan.getEnrollmentSpanCode())
                .enrollmentType(enrollmentSpan.getEnrollmentType())
                .stateTypeCode(enrollmentSpan.getStateTypeCode())
                .marketplaceTypeCode(enrollmentSpan.getMarketplaceTypeCode())
                .businessUnitTypeCode(enrollmentSpan.getBusinessUnitTypeCode())
                .coverageTypeCode(enrollmentSpan.getCoverageTypeCode())
                .startDate(enrollmentSpan.getStartDate())
                .endDate(enrollmentSpan.getEndDate())
                .exchangeSubscriberId(enrollmentSpan.getExchangeSubscriberId())
                .effectuationDate(enrollmentSpan.getEffectuationDate())
                .planId(enrollmentSpan.getPlanId())
                .groupPolicyId(enrollmentSpan.getGroupPolicyId())
                .productTypeCode(enrollmentSpan.getProductTypeCode())
                .delinqInd(enrollmentSpan.isDelinqInd())
                .paidThroughDate(enrollmentSpan.getPaidThroughDate())
                .claimPaidThroughDate(enrollmentSpan.getClaimPaidThroughDate())
                .statusTypeCode(enrollmentSpan.getStatusTypeCode())
                .effectiveReason(enrollmentSpan.getEffectiveReason())
                .termReason(enrollmentSpan.getTermReason())
                .build();
        return enrollmentSpanDto;
    }
}
