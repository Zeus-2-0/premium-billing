package com.brihaspathee.zeus.helper.interfaces;

import com.brihaspathee.zeus.domain.entity.PayloadTracker;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 9:30 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface PayloadTrackerHelper {

    /**
     * Create a new payload tracker
     * @param payloadTracker
     * @return
     */
    PayloadTracker createPayloadTracker(PayloadTracker payloadTracker);

    /**
     * Get payload tracker by payload id
     * @param payloadId
     * @return
     */
    PayloadTracker getPayloadTracker(String payloadId);

    /**
     * Delete all payload tracker information
     */
    void deleteAll();
}
