package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import com.brihaspathee.zeus.domain.repository.PremiumSpanRepository;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.account.PremiumSpanDto;
import com.brihaspathee.zeus.helper.interfaces.MemberPremiumHelper;
import com.brihaspathee.zeus.helper.interfaces.PremiumSpanHelper;
import com.brihaspathee.zeus.mapper.interfaces.PremiumSpanMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:33 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumSpanHelperImpl implements PremiumSpanHelper {

    /**
     * The instance of premium span repository
     */
    private final PremiumSpanRepository premiumSpanRepository;

    /**
     * The instance of the premium span mapper
     */
    private final PremiumSpanMapper premiumSpanMapper;

    /**
     * The instance of the member premium helper
     */
    private final MemberPremiumHelper memberPremiumHelper;

    /**
     * Create the premium spans for the enrollment span
     * @param enrollmentSpan - The enrollment spans to which the premium spans are associted
     * @param premiumSpanDtos - The premium spans to be created
     */
    @Override
    public void updatePremiumSpans(EnrollmentSpan enrollmentSpan,
                                   List<PremiumSpanDto> premiumSpanDtos) {
        if(premiumSpanDtos == null || premiumSpanDtos.isEmpty()){
            return;
        }
        log.info("About to create premium spans");
        premiumSpanDtos.forEach(premiumSpanDto -> {
            // check if the premium span has changed
            if(premiumSpanDto.getChanged().get()){
                // check if the premium span exists with the enrollment span
                PremiumSpan premiumSpan = getPremiumSpan(premiumSpanDto.getPremiumSpanCode());
                if(premiumSpan == null){
                    // this means that this is a new premium span
                    // so create the new span
                    updatePremiumSpan(enrollmentSpan, premiumSpanDto, true);
                }else{
                    // this means its an existing premium span that needs to be updated
                    // set the premium span sk
                    premiumSpanDto.setPremiumSpanSK(premiumSpan.getPremiumSpanSK());
                    updatePremiumSpan(enrollmentSpan, premiumSpanDto, false);
                }
            }
//            PremiumSpan premiumSpan = premiumSpanMapper.premiumSpanDtoToPremiumSpan(premiumSpanDto);
//            premiumSpan.setEnrollmentSpan(enrollmentSpan);
//            log.info("Premium span to be created:{}", premiumSpan.getPremiumSpanCode());
//            premiumSpan = premiumSpanRepository.save(premiumSpan);
//            log.info("Premium span created:{}", premiumSpan.getPremiumSpanSK());
//            memberPremiumHelper.createMemberPremiums(
//                    premiumSpan, premiumSpanDto
//                            .getMemberPremiumSpans()
//                            .stream()
//                            .toList());
        });

    }

    /**
     * Create the premium span dto from premium span entity and set it to the enrollment span dto
     * @param enrollmentSpanDto
     * @param premiumSpans
     */
    @Override
    public void setPremiumSpan(EnrollmentSpanDto enrollmentSpanDto, List<PremiumSpan> premiumSpans) {
        Set<PremiumSpanDto> premiumSpanDtos = new HashSet<>();
        premiumSpans.forEach(premiumSpan -> {
            PremiumSpanDto premiumSpanDto = premiumSpanMapper.premiumSpanToPremiumSpanDto(premiumSpan);
            memberPremiumHelper.setMemberPremiums(premiumSpanDto, premiumSpan.getMemberPremiums());
            premiumSpanDtos.add(premiumSpanDto);
        });
        enrollmentSpanDto.setPremiumSpans(premiumSpanDtos);
    }

    /**
     * get the premium span matching the premium span code if present in the database
     * @param premiumSpanCode - the premium span code that matches the premium span
     * @return - matched premium span
     */
    private PremiumSpan getPremiumSpan(String premiumSpanCode){
        Optional<PremiumSpan> optional = premiumSpanRepository.findPremiumSpanByPremiumSpanCode(premiumSpanCode);
        return optional.orElse(null);
    }

    /**
     * Update the premium span
     * @param enrollmentSpan
     * @param premiumSpanDto
     * @param isNewSpan
     */
    private void updatePremiumSpan(EnrollmentSpan enrollmentSpan, PremiumSpanDto premiumSpanDto, boolean isNewSpan){
        PremiumSpan premiumSpan = premiumSpanMapper.premiumSpanDtoToPremiumSpan(premiumSpanDto);
        premiumSpan.setEnrollmentSpan(enrollmentSpan);
        log.info("Premium span to be created or updated:{}", premiumSpan.getPremiumSpanCode());
        premiumSpan = premiumSpanRepository.save(premiumSpan);
        log.info("Premium span created or updated:{}", premiumSpan.getPremiumSpanSK());
        if(isNewSpan){
            memberPremiumHelper.createMemberPremiums(
                    premiumSpan, premiumSpanDto
                            .getMemberPremiumSpans()
                            .stream()
                            .toList());
        }
    }
}
