package com.brihaspathee.zeus.service.impl;

import com.brihaspathee.zeus.domain.entity.Account;
import com.brihaspathee.zeus.domain.repository.AccountRepository;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;
import com.brihaspathee.zeus.exception.AccountNotFoundException;
import com.brihaspathee.zeus.helper.interfaces.EnrollmentSpanHelper;
import com.brihaspathee.zeus.mapper.interfaces.AccountMapper;
import com.brihaspathee.zeus.service.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 4:52 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.service.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    /**
     * Instance of the account repository
     */
    private final AccountRepository accountRepository;

    /**
     * Instance of the enrollment span helper
     */
    private final EnrollmentSpanHelper enrollmentSpanHelper;

    /**
     * Instance of the account entity mapper
     */
    private final AccountMapper accountMapper;

    /**
     * Perform updates for the account
     * @param accountDto
     */
    @Override
    public void updateAccount(AccountDto accountDto) {
        log.info("Inside the account service to update the account");
        // get the account number of the account
        String accountNumber = accountDto.getAccountNumber();
        // check if the account already exists
        Optional<Account> optional = accountRepository.findAccountByAccountNumber(accountNumber);
        if(optional.isPresent()){
            // perform updates on the account
            if(accountDto.getEnrollmentSpans() !=null){
                enrollmentSpanHelper.updateEnrollmentSpan(optional.get(),
                        accountDto.getEnrollmentSpans().stream().toList());
            }
        }else{
            log.info("There is no account with this account number:{}, hence creating the account.", accountNumber);
            // create the new account
            createAccount(accountDto);
        }
    }

    /**
     * Get account by account number
     * @param accountNumber
     * @return
     */
    @Override
    public AccountDto getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account with account number " +
                                accountNumber +
                                " is not found"));

        return createAccountDto(account);
    }

    /**
     * Manage payments for an account
     * @param premiumPaymentDto
     */
    @Override
    public void createPremiumPayment(PremiumPaymentDto premiumPaymentDto) {
        enrollmentSpanHelper.createPremiumPayment(premiumPaymentDto);
    }

    /**
     * Create the new account
     * @param accountDto
     */
    private void createAccount(AccountDto accountDto){
        Account account = accountMapper.accountDtoToAccount(accountDto);
        account = accountRepository.save(account);
        log.info("Account is created:{}", account.getAccountSK());
        enrollmentSpanHelper.updateEnrollmentSpan(account, accountDto.getEnrollmentSpans().stream().toList());
    }

    /**
     * Create the account dto object from the account entity
     * @param account
     * @return
     */
    private AccountDto createAccountDto(Account account){
        AccountDto accountDto = accountMapper.accountToAccountDto(account);
        enrollmentSpanHelper.setEnrollmentSpan(accountDto, account.getEnrollmentSpan());
        return accountDto;
    }
}
