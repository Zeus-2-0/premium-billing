package com.brihaspathee.zeus.domain.repository;

import com.brihaspathee.zeus.domain.entity.MemberPremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 27, April 2024
 * Time: 6:11 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.entity
 * To change this template use File | Settings | File and Code Template
 */
@Repository
public interface MemberPremiumRepository extends JpaRepository<MemberPremium, UUID> {
}
