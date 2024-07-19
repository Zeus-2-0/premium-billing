package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.PayloadTrackerDetail;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 9:30 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface PayloadTrackerDetailHelper {

    /**
     * Create a payload tracker detail record
     * @param payloadTrackerDetail
     * @return
     */
    PayloadTrackerDetail createPayloadTrackerDetail(PayloadTrackerDetail payloadTrackerDetail);
}
