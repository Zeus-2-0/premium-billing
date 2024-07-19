package com.brihaspathee.zeus.mapper.impl;

import com.brihaspathee.zeus.domain.entity.MemberPremium;
import com.brihaspathee.zeus.dto.account.MemberPremiumDto;
import com.brihaspathee.zeus.mapper.interfaces.MemberPremiumMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:51 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.mapper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberPremiumMapperImpl implements MemberPremiumMapper {

    /**
     * Convert member premium dto to entity
     * @param memberPremiumDto - the dto to be converted to entity
     * @return - the member premium entity
     */
    @Override
    public MemberPremium memberPremiumDtoToMemberPremium(MemberPremiumDto memberPremiumDto) {
        if(memberPremiumDto == null){
            return null;
        }
        MemberPremium memberPremium = MemberPremium.builder()
                .memberPremiumSK(memberPremiumDto.getMemberPremiumSK())
                .exchangeMemberId(memberPremiumDto.getExchangeMemberId())
                .individualRateAmount(memberPremiumDto.getIndividualPremiumAmount())
                .memberCode(memberPremiumDto.getMemberCode())
                .build();
        return memberPremium;
    }

    /**
     * Convert the member premium entity to dto
     * @param memberPremium - the entity to be converted to dto
     * @return - the converted member premium dto
     */
    @Override
    public MemberPremiumDto memberPremiumToMemberPremiumDto(MemberPremium memberPremium) {
        if(memberPremium == null){
            return null;
        }
        MemberPremiumDto memberPremiumDto = MemberPremiumDto.builder()
                .memberPremiumSK(memberPremium.getMemberPremiumSK())
                .exchangeMemberId(memberPremium.getExchangeMemberId())
                .individualPremiumAmount(memberPremium.getIndividualRateAmount())
                .memberCode(memberPremium.getMemberCode())
                .build();
        return memberPremiumDto;
    }
}
