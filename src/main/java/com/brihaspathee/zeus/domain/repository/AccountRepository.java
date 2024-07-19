package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 27, April 2024
 * Time: 6:08 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.repository
 * To change this template use File | Settings | File and Code Template
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    /**
     * Find account by account number
     * @param accountNumber
     * @return
     */
    Optional<Account> findAccountByAccountNumber(String accountNumber);
}
