package com.brihaspathee.zeus.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 12, July 2024
 * Time: 4:43 AM
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
@Table(name = "PREMIUM_PAYMENT")
public class PremiumPayment {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(Types.LONGVARCHAR)
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "premium_payment_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID premiumPaymentSK;

    /**
     * Unique enrollment span code that is assigned to the enrollment span
     */
    @Column(name = "enrollment_span_code", length = 50, columnDefinition = "varchar", nullable = false, updatable = false)
    private String enrollmentSpanCode;

    /**
     * The total premium payment made for the enrollment span
     */
    @Column(name = "premium_payment", nullable = false)
    private BigDecimal premiumPayment;

    /**
     * The date when the premium payment was made
     */
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    /**
     * The enrollment span to which the premium span is associated
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "enrollment_span_sk")
    private EnrollmentSpan enrollmentSpan;

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
