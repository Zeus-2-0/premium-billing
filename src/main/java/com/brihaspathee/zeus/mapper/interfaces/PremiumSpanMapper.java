package com.brihaspathee.zeus.mapper.interfaces;

import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import com.brihaspathee.zeus.dto.account.PremiumSpanDto;

import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:34 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.mapper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface PremiumSpanMapper {

    /**
     * Convert premium span dto to premium span entity
     * @param premiumSpanDto - the dto that needs to be converted to entity
     * @return - the converted entity
     */
    PremiumSpan premiumSpanDtoToPremiumSpan(PremiumSpanDto premiumSpanDto);

    /**
     * Convert premium span entity to premium span dto
     * @param premiumSpan - the entity that needs to be converted to dto
     * @return - the converted dto
     */
    PremiumSpanDto premiumSpanToPremiumSpanDto(PremiumSpan premiumSpan);

    /**
     * Convert premium span dtos to premium span entities
     * @param premiumSpanDtos
     * @return
     */
    List<PremiumSpan> premiumSpanDtosToPremiumSpans(List<PremiumSpanDto> premiumSpanDtos);

    /**
     * Convert premium span entities to premium span dtos
     * @param premiumSpans
     * @return
     */
    List<PremiumSpanDto> premiumSpansToPremiumSpanDtos(List<PremiumSpan> premiumSpans);
}
