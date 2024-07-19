package com.brihaspathee.zeus.mapper.interfaces;

import com.brihaspathee.zeus.domain.entity.MemberPremium;
import com.brihaspathee.zeus.dto.account.MemberPremiumDto;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:45 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.mapper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface MemberPremiumMapper {

    /**
     * Convert member premium dto to member premium entity
     * @param memberPremiumDto - the dto to be converted to entity
     * @return - the converted entity
     */
    MemberPremium memberPremiumDtoToMemberPremium(MemberPremiumDto memberPremiumDto);

    /**
     * Conver member premium entity to dto
     * @param memberPremium - the entity to be converted to dto
     * @return - the converted dto
     */
    MemberPremiumDto memberPremiumToMemberPremiumDto(MemberPremium memberPremium);
}
