package com.brihaspathee.zeus.mapper.impl;

import com.brihaspathee.zeus.domain.entity.Account;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.mapper.interfaces.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:07 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.mapper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountMapperImpl implements AccountMapper {

    /**
     * Convert account dto to account entity
     * @param accountDto
     * @return
     */
    @Override
    public Account accountDtoToAccount(AccountDto accountDto) {
        if(accountDto == null){
            return null;
        }
        Account account = Account.builder()
                .accountSK(accountDto.getAccountSK())
                .accountNumber(accountDto.getAccountNumber())
                .lineOfBusinessTypeCode(accountDto.getLineOfBusinessTypeCode())
                .build();
        return account;
    }

    /**
     * Convert account entity to account dto
     * @param account
     * @return
     */
    @Override
    public AccountDto accountToAccountDto(Account account) {
        if(account == null){
            return null;
        }
        AccountDto accountDto = AccountDto.builder()
                .accountSK(account.getAccountSK())
                .accountNumber(account.getAccountNumber())
                .lineOfBusinessTypeCode(account.getLineOfBusinessTypeCode())
                .build();
        return accountDto;
    }
}
