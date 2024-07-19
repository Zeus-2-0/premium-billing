package com.brihaspathee.zeus.mapper.impl;

import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import com.brihaspathee.zeus.dto.account.PremiumSpanDto;
import com.brihaspathee.zeus.mapper.interfaces.PremiumSpanMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:40 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.mapper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumSpanMapperImpl implements PremiumSpanMapper {

    /**
     * Convert the dto to entity
     * @param premiumSpanDto - the dto that needs to be converted to entity
     * @return - the converted entity
     */
    @Override
    public PremiumSpan premiumSpanDtoToPremiumSpan(PremiumSpanDto premiumSpanDto) {
        if(premiumSpanDto == null){
            return null;
        }
        PremiumSpan premiumSpan = PremiumSpan.builder()
                .premiumSpanSK(premiumSpanDto.getPremiumSpanSK())
                .premiumSpanCode(premiumSpanDto.getPremiumSpanCode())
                .sequence(premiumSpanDto.getSequence())
                .startDate(premiumSpanDto.getStartDate())
                .endDate(premiumSpanDto.getEndDate())
                .statusTypeCode(premiumSpanDto.getStatusTypeCode())
                .csrVariant(premiumSpanDto.getCsrVariant())
                .totalPremAmount(premiumSpanDto.getTotalPremiumAmount())
                .totalResponsibleAmount(premiumSpanDto.getTotalResponsibleAmount())
                .aptcAmount(premiumSpanDto.getAptcAmount())
                .otherPayAmount(premiumSpanDto.getOtherPayAmount())
                .csrAmount(premiumSpanDto.getCsrAmount())
                .build();
        return premiumSpan;
    }

    /**
     * Convert the entity to dto
     * @param premiumSpan - the entity that needs to be converted to dto
     * @return - the converted dto
     */
    @Override
    public PremiumSpanDto premiumSpanToPremiumSpanDto(PremiumSpan premiumSpan) {
        if(premiumSpan == null){
            return null;
        }
        PremiumSpanDto premiumSpanDto = PremiumSpanDto.builder()
                .premiumSpanSK(premiumSpan.getPremiumSpanSK())
                .premiumSpanCode(premiumSpan.getPremiumSpanCode())
                .sequence(premiumSpan.getSequence())
                .startDate(premiumSpan.getStartDate())
                .endDate(premiumSpan.getEndDate())
                .statusTypeCode(premiumSpan.getStatusTypeCode())
                .csrVariant(premiumSpan.getCsrVariant())
                .totalPremiumAmount(premiumSpan.getTotalPremAmount())
                .totalResponsibleAmount(premiumSpan.getTotalResponsibleAmount())
                .aptcAmount(premiumSpan.getAptcAmount())
                .otherPayAmount(premiumSpan.getOtherPayAmount())
                .csrAmount(premiumSpan.getCsrAmount())
                .build();
        return premiumSpanDto;
    }
}
