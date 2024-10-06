package com.brihaspathee.zeus.service.interfaces;

import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanList;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 17, July 2024
 * Time: 3:33 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface MemberManagementService {

    /**
     * Send member management service the enrollment span that needs to be updated
     * @param enrollmentSpanList - The list of enrollment spans that needs to be updated
     */
    void updateEnrollmentSpan(EnrollmentSpanList enrollmentSpanList);
}
