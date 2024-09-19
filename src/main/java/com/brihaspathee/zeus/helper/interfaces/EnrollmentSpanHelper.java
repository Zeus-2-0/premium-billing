package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.Account;
import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;
import com.brihaspathee.zeus.service.TransactionProcessingContext;

import java.time.LocalDate;
import java.util.List;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:14 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface EnrollmentSpanHelper {

//    /**
//     * Create enrollment spans
//     * @param account - The account with which the enrollment span is associated
//     * @param enrollmentSpanDtos - The enrollment spans that needs to be created
//     */
//    void createEnrollmentSpan(Account account,
//                              List<EnrollmentSpanDto> enrollmentSpanDtos);

    /**
     * Update the enrollment span
     * @param context
     * @param account
     * @param enrollmentSpanDtos
     */
    void updateEnrollmentSpan(TransactionProcessingContext context,
                              Account account,
                              List<EnrollmentSpanDto> enrollmentSpanDtos);

//    /**
//     * Update the enrollment span status and paid through date
//     * @param enrollmentSpan
//     * @param effectuationDate
//     * @param paidThruDate
//     * @param status
//     */
//    void updateEnrollmentSpan(EnrollmentSpan enrollmentSpan,
//                              LocalDate effectuationDate,
//                              LocalDate paidThruDate,
//                              String status);

    /**
     * Create enrollment span dto from enrollment span entity and set it in the account dto
     * @param accountDto
     * @param enrollmentSpans
     */
    void setEnrollmentSpan(AccountDto accountDto, List<EnrollmentSpan> enrollmentSpans);

    /**
     * Create the premium payment received for an enrollment span
     * @param premiumPaymentDto
     */
    void createPremiumPayment(PremiumPaymentDto premiumPaymentDto);
}
