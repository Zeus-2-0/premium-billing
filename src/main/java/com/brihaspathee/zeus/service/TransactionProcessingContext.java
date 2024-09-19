package com.brihaspathee.zeus.service;

import lombok.*;

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
     * Boolean indicator that indicates if an update regarding the enrollment span should be
     * sent to MMS or not
     */
    private boolean sendUpdateToMMS;
}
