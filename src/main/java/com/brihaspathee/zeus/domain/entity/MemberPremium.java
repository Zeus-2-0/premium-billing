package com.brihaspathee.zeus.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 27, April 2024
 * Time: 5:38 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.domain.entity
 * To change this template use File | Settings | File and Code Template
 */
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER_PREMIUM")
public class MemberPremium {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(Types.LONGVARCHAR)
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "member_premium_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID memberPremiumSK;

    /**
     * The unique code for the member
     */
    @Column(name = "member_code", nullable = false, columnDefinition = "varchar", length = 50)
    private String memberCode;

    /**
     * The exchange member id of the member
     */
    @Column(name = "exchange_member_id", length = 50, columnDefinition = "varchar", nullable = false, updatable = false)
    private String exchangeMemberId;

    /**
     * The rate of the member
     */
    @Column(name = "individual_premium_amount")
    private BigDecimal individualRateAmount;

    /**
     * The premium span key
     */
    @ManyToOne
    @JoinColumn(name = "premium_span_sk")
    private PremiumSpan premiumSpan;

    /**
     * The date when the record was created
     */
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * The date when the record was updated
     */
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
