package com.brihaspathee.zeus.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 5:56 AM
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
@Table(name = "PAYLOAD_TRACKER")
public class PayloadTracker {

    /**
     * Primary key of the table
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(Types.LONGVARCHAR)
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "payload_tracker_sk", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID payloadTrackerSK;

    /**
     * Unique payload id created for the payload
     */
    @Column(name = "payload_id", length = 45, nullable = false)
    private String payloadId;

    /**
     * The key for the type of payload, like account number for account payload and
     * zeus transaction control number for transaction payload
     */
    @Column(name = "payload_key", length = 50, columnDefinition = "varchar", nullable = false)
    private String payload_key;

    /**
     * Identifies the type of payload like ACCOUNT, TRANSACTION, FILE etc
     */
    @Column(name = "payload_key_type_code", length = 45, columnDefinition = "varchar", nullable = false)
    private String payload_key_type_code;

    /**
     * The payload data in JSON format
     */
    @Lob
    @JdbcTypeCode(Types.LONGVARCHAR)
    @Column(name = "payload")
    private String payload;

    /**
     * Identifies if the payload is an inbound or outbound payload
     */
    @Column(name = "payload_direction_type_code", length = 45, nullable = false)
    private String payloadDirectionTypeCode;

    /**
     * The source of the payload when its an inbound payload and the
     * destination of the payload when it is an outbound payload
     */
    @Column(name = "src_dest", length=100, nullable = false)
    private String sourceDestinations;

    /**
     * The detail tracker of the payload
     */
    @OneToMany(mappedBy = "payloadTracker", cascade = CascadeType.REMOVE)
    private Set<PayloadTrackerDetail> payloadTrackerDetails;

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

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "PayloadTracker{" +
                "payloadTrackerSK=" + payloadTrackerSK +
                ", payloadId='" + payloadId + '\'' +
                ", payload_key='" + payload_key + '\'' +
                ", payload_key_type_code='" + payload_key_type_code + '\'' +
                ", payload='" + payload + '\'' +
                ", payloadDirectionTypeCode='" + payloadDirectionTypeCode + '\'' +
                ", sourceDestinations='" + sourceDestinations + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
