package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.MemberPremium;
import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import com.brihaspathee.zeus.dto.account.MemberPremiumDto;
import com.brihaspathee.zeus.dto.account.PremiumSpanDto;

import java.util.List;
import java.util.Set;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 9:11 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface MemberPremiumHelper {

    /**
     * Create member premiums
     * @param premiumSpan - The premium span to which the member premiums are associated
     * @param memberPremiumDtos - The member premiums associated with the premium span
     */
    void createMemberPremiums(PremiumSpan premiumSpan,
                              List<MemberPremiumDto> memberPremiumDtos);

    /**
     * Create member premium dto from member premium entity and set it to premium span dto
     * @param premiumSpanDto
     * @param memberPremiums
     */
    void setMemberPremiums(PremiumSpanDto premiumSpanDto, List<MemberPremium> memberPremiums);
}
