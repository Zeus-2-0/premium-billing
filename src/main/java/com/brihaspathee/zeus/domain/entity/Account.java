package com.brihaspathee.zeus.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Types;
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
@Table(name = "ACCOUNT")
public class Account {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(Types.LONGVARCHAR)
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "account_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID accountSK;

    /**
     * Unique number that is assigned to the account
     */
    @Column(name = "account_number", length = 50, columnDefinition = "varchar", updatable = false, nullable = false)
    private String accountNumber;

    /**
     * The line of business of the account
     */
    @Column(name = "line_of_business_type_code", columnDefinition = "varchar", nullable = false, length = 50)
    private String lineOfBusinessTypeCode;

    /**
     * The enrollment spans that are associated with the account
     */
    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<EnrollmentSpan> enrollmentSpan;

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
