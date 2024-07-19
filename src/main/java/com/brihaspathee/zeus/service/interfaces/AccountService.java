package com.brihaspathee.zeus.service.interfaces;

import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 4:51 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.interfaces
 * To change this template use File | Settings | File and Code Template
 */
public interface AccountService {

    /**
     * Update the account based on the changes received
     * @param accountDto
     */
    void updateAccount(AccountDto accountDto);

    /**
     * Get account by account number
     * @param accountNumber
     * @return
     */
    AccountDto getAccountByAccountNumber(String accountNumber);

    /**
     * Create the premium payment received for an enrollment span
     * @param premiumPaymentDto
     */
    void createPremiumPayment(PremiumPaymentDto premiumPaymentDto);
}
