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
import java.util.List;
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
@Table(name = "PREMIUM_SPAN")
public class PremiumSpan {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(Types.LONGVARCHAR)
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "premium_span_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID premiumSpanSK;

    /**
     * Unique span code that is assigned to the premium span
     */
    @Column(name = "premium_span_code", columnDefinition = "varchar", length = 50, nullable = false)
    private String premiumSpanCode;

    /**
     * The start date of the premium span
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * The end date of the enrollment span
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * The status of the premium span
     */
    @Column(name = "status_type_code", length = 50, columnDefinition = "varchar", nullable = false)
    private String statusTypeCode;

    /**
     * The CSR Variant present in the plan id received for the enrollment span
     */
    @Column(name = "csr_variant", nullable = false)
    private String csrVariant;

    /**
     * The total premium amount for the premium span
     */
    @Column(name = "total_prem_amt", nullable = false)
    private BigDecimal totalPremAmount;

    /**
     * The total responsible amount for the premium span
     */
    @Column(name = "total_resp_amt", nullable = false)
    private BigDecimal totalResponsibleAmount;

    /**
     * The APTC amount for the premium span
     */
    @Column(name = "aptc_amt", nullable = true)
    private BigDecimal aptcAmount;

    /**
     * The sum of other pay amt 1 and other pay amt 2 for the premium span
     */
    @Column(name = "other_pay_amt", nullable = true)
    private BigDecimal otherPayAmount;

    /**
     * The CSR Amount for the premium span
     */
    @Column(name = "csr_amt", nullable = true)
    private BigDecimal csrAmount;

    /**
     * The sequence in which the premium span was created
     */
    @Column(name = "sequence", nullable = true)
    private int sequence;

    /**
     * List of members associated with the premium span
     */
    @OneToMany(mappedBy = "premiumSpan", cascade = CascadeType.REMOVE)
    List<MemberPremium> memberPremiums;

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
