package com.brihaspathee.zeus.service;

import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import lombok.*;

import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 23, July 2024
 * Time: 10:32 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service
 * To change this template use File | Settings | File and Code Template
 */
@Setter
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TransactionProcessingContext {

    /**
     * The account for which the transaction is being processed
     */
    private String accountNumber;

    /**
     * The primary key of the account in PB for which the transaction is processed
     */
    private UUID accountSK;

    /**
     * Boolean indicator that indicates if an update regarding the enrollment span should be
     * sent to MMS or not
     */
    private boolean sendUpdateToMMS;

    /**
     * Boolean indicator that indicates if an existing enrollment span is being canceled
     */
    private boolean isESGettingCanceled;

    /**
     * Boolean indicator that indicates if an existing enrollment span is being termed
     */
    private boolean isESGettingTermed;

    /**
     * The enrollment span that is immediately next to the enrollment span that is being updated
     * This enrollment span may need to be updated if the current enrollment span is being termed or canceled
     */
    private EnrollmentSpan nextEnrollmentSpan;

    /**
     * Indicates if the next enrollment span was reset
     */
    private boolean isNextESReset;

    /**
     * to String method
     * @return
     */
    @Override
    public String toString() {
        return "TransactionProcessingContext{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountSK=" + accountSK +
                ", sendUpdateToMMS=" + sendUpdateToMMS +
                ", isESGettingCanceled=" + isESGettingCanceled +
                ", isESGettingTermed=" + isESGettingTermed +
                ", nextEnrollmentSpan=" + nextEnrollmentSpan +
                ", isNextESReset=" + isNextESReset +
                '}';
    }
}
