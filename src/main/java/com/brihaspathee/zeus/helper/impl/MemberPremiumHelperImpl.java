package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.domain.entity.MemberPremium;
import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import com.brihaspathee.zeus.domain.repository.MemberPremiumRepository;
import com.brihaspathee.zeus.dto.account.MemberPremiumDto;
import com.brihaspathee.zeus.dto.account.PremiumSpanDto;
import com.brihaspathee.zeus.helper.interfaces.MemberPremiumHelper;
import com.brihaspathee.zeus.mapper.interfaces.MemberPremiumMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 9:13 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberPremiumHelperImpl implements MemberPremiumHelper {

    /**
     * The instance of the member premium repository
     */
    private final MemberPremiumRepository memberPremiumRepository;

    /**
     * The instance of the member premium mapper
     */
    private final MemberPremiumMapper memberPremiumMapper;


    /**
     * Create member premiums
     * @param premiumSpan - The premium span to which the member premiums are associated
     * @param memberPremiumDtos - The member premiums associated with the premium span
     */
    @Override
    public void createMemberPremiums(PremiumSpan premiumSpan,
                                     List<MemberPremiumDto> memberPremiumDtos) {
        if(memberPremiumDtos == null || memberPremiumDtos.isEmpty()){
            return;
        }
        memberPremiumDtos.forEach(memberPremiumDto -> {
            MemberPremium memberPremium = memberPremiumMapper.memberPremiumDtoToMemberPremium(memberPremiumDto);
            log.info("Member premium to be created is for member:{}", memberPremium.getMemberCode());
            memberPremium.setPremiumSpan(premiumSpan);
            memberPremium = memberPremiumRepository.save(memberPremium);
            log.info("Member premium created for the member is:{}", memberPremium.getMemberPremiumSK());
        });
    }

    /**
     * Create member premium dto from member premium entity and set it to premium span dto
     * @param premiumSpanDto
     * @param memberPremiums
     */
    @Override
    public void setMemberPremiums(PremiumSpanDto premiumSpanDto, List<MemberPremium> memberPremiums) {
        Set<MemberPremiumDto> memberPremiumDtos = new HashSet<>();
        memberPremiums.forEach(memberPremium -> {
           MemberPremiumDto memberPremiumDto = memberPremiumMapper.memberPremiumToMemberPremiumDto(memberPremium);
           memberPremiumDtos.add(memberPremiumDto);
        });
        premiumSpanDto.setMemberPremiumSpans(memberPremiumDtos);
    }
}
