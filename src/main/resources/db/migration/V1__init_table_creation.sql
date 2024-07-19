DROP TABLE IF EXISTS `premiumbillingdb`.`member_premium`;
DROP TABLE IF EXISTS `premiumbillingdb`.`premium_span`;
DROP TABLE IF EXISTS `premiumbillingdb`.`enrollment_span`;
DROP TABLE IF EXISTS `premiumbillingdb`.`account`;
DROP TABLE IF EXISTS `premiumbillingdb`.`payload_tracker`;
DROP TABLE IF EXISTS `premiumbillingdb`.`payload_tracker_detail`;
CREATE TABLE IF NOT EXISTS `premiumbillingdb`.`account` (
    `account_sk` VARCHAR(36) NOT NULL COMMENT 'Primary key of the table',
    `account_number` VARCHAR(50) NOT NULL COMMENT 'The account number of the account',
    `line_of_business_type_code` VARCHAR(50) NOT NULL,
    `created_date` DATETIME NULL,
    `updated_date` DATETIME NULL,
    PRIMARY KEY (`account_sk`))
    ENGINE = InnoDB
    COMMENT = 'This billing account table';
CREATE TABLE IF NOT EXISTS `premiumbillingdb`.`enrollment_span` (
    `enrollment_span_sk` VARCHAR(36) NOT NULL COMMENT 'Primary key of the table',
    `enrollment_span_code` VARCHAR(50) NOT NULL COMMENT 'The enrollment span code associated with the enrollment span',
    `enrollment_type` VARCHAR(50) NOT NULL COMMENT 'Indicates if the enrollment was a passive or active enrollment',
    `account_sk` VARCHAR(36) NOT NULL COMMENT 'The account for which the enrollment span is associated',
    `state_type_code` VARCHAR(50) NOT NULL COMMENT 'The state for which the enrollment span is created',
    `marketplace_type_code` VARCHAR(45) NOT NULL COMMENT 'The marketplace for which the enrollment span is created',
    `business_unit_type_code` VARCHAR(50) NOT NULL COMMENT 'The business unit for which the enrollment span is created',
    `coverage_type_code` VARCHAR(50) NOT NULL COMMENT 'The coverage type associated with the enrollment span',
    `start_date` DATE NOT NULL COMMENT 'The start date of the enrollment span',
    `end_date` DATE NOT NULL COMMENT 'The end date of the enrollment span',
    `exchange_subscriber_id` VARCHAR(50) NOT NULL COMMENT 'The exchange subscriber id for which the enrollment span is created',
    `effectuation_date` DATE NULL COMMENT 'The effectuation date of the enrollment span',
    `plan_id` VARCHAR(100) NOT NULL COMMENT 'The QHP Id of the enrollment span',
    `product_type_code` VARCHAR(100) NOT NULL COMMENT 'The product type of the plan',
    `group_policy_id` VARCHAR(100) NOT NULL COMMENT 'The group policy id of the enrollment span',
    `delinq_ind` BOOLEAN NOT NULL DEFAULT 0 COMMENT 'Identifies if the enrollment span is delinquent',
    `paid_through_date` DATE NULL,
    `claim_paid_through_date` DATE NULL COMMENT 'The claim paid through date associated with the enrollment span',
    `status_type_code` VARCHAR(50) NOT NULL COMMENT 'The status of the enrollment span',
    `effective_reason` VARCHAR(150) NULL COMMENT 'The effective reason of the enrollment span',
    `term_reason` VARCHAR(150) NULL COMMENT 'The term reason of the enrollment span',
    `created_date` DATETIME NULL COMMENT 'The date when the record is created',
    `updated_date` DATETIME NULL COMMENT 'The date when the record is updated',
    PRIMARY KEY (`enrollment_span_sk`),
    INDEX `trans_acct_fk_idx` (`account_sk` ASC) VISIBLE,
    CONSTRAINT `trans_acct_fk`
    FOREIGN KEY (`account_sk`)
    REFERENCES `premiumbillingdb`.`account` (`account_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'The enrollment span associated with the account';
CREATE TABLE IF NOT EXISTS `premiumbillingdb`.`premium_span` (
    `premium_span_sk` VARCHAR(36) NOT NULL,
    `enrollment_span_sk` VARCHAR(36) NOT NULL,
    `premium_span_code` VARCHAR(50) NOT NULL COMMENT 'Unique code created for each of the premium span',
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `status_type_code` VARCHAR(50) NOT NULL COMMENT 'The status of the premium span',
    `csr_variant` VARCHAR(10) NOT NULL,
    `total_prem_amt` DECIMAL(10,2) NOT NULL COMMENT 'The total premium amount per month for the plan chosen by the member',
    `total_resp_amt` DECIMAL(10,2) NOT NULL COMMENT 'Total amount that the member is responsible for payment towards the premium',
    `aptc_amt` DECIMAL(10,2) NULL COMMENT 'Federal contribution towards the premium',
    `other_pay_amt` DECIMAL(10,2) NULL COMMENT 'The amounts contributed by other sources (like the state) towards the premium',
    `csr_amt` DECIMAL(10,2) NULL COMMENT 'The Cost Sharing Reduction amount',
    `sequence` INT NOT NULL COMMENT 'The sequence in which the premium span is created',
    `created_date` DATETIME NULL COMMENT 'Date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'Date when the record was updated',
    PRIMARY KEY (`premium_span_sk`),
    INDEX `enrollment_fk_idx` (`enrollment_span_sk` ASC) VISIBLE,
    CONSTRAINT `enrollment_fk`
    FOREIGN KEY (`enrollment_span_sk`)
    REFERENCES `premiumbillingdb`.`enrollment_span` (`enrollment_span_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'The premium spans associated with the enrollment span';
CREATE TABLE IF NOT EXISTS `premiumbillingdb`.`member_premium` (
    `member_premium_sk` VARCHAR(36) NOT NULL COMMENT 'The primary key of the table',
    `premium_span_sk` VARCHAR(36) NOT NULL COMMENT 'The key of the premium span',
    `member_code` VARCHAR(50) NOT NULL COMMENT 'The member to whom the premium span is associated',
    `exchange_member_id` VARCHAR(50) NOT NULL COMMENT 'The exchange member id of the member',
    `individual_premium_amount` DECIMAL(10,2) NOT NULL COMMENT 'The rate of the individual member',
    `created_date` DATETIME NULL COMMENT 'The date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'The date when the record was updated',
    PRIMARY KEY (`member_premium_sk`),
    INDEX `premium_span_fk_idx` (`premium_span_sk` ASC) VISIBLE,
    CONSTRAINT `premium_span_fk`
    FOREIGN KEY (`premium_span_sk`)
    REFERENCES `premiumbillingdb`.`premium_span` (`premium_span_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'This table shows the relationship between the members and the premium spans';
CREATE TABLE IF NOT EXISTS `premiumbillingdb`.`payload_tracker` (
    `payload_tracker_sk` VARCHAR(36) NOT NULL,
    `payload_id` VARCHAR(45) NOT NULL,
    `payload_key` VARCHAR(50) NOT NULL COMMENT 'The key for the type of payload, like account number for account payload and zeus transaction control number for transaction payload\n',
    `payload_key_type_code` VARCHAR(45) NOT NULL COMMENT 'Identifies the type of payload like ACCOUNT, TRANSACTION, FILE etc',
    `payload` LONGTEXT NOT NULL COMMENT 'The payload as a string',
    `payload_direction_type_code` VARCHAR(45) NOT NULL COMMENT 'Identifies the direction of the payload',
    `src_dest` VARCHAR(100) NOT NULL COMMENT 'Identifies the source if the payload is inbound and destination if the payload is outbound',
    `created_date` DATETIME NULL COMMENT 'The date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'The date when the record was updated',
    PRIMARY KEY (`payload_tracker_sk`))
    ENGINE = InnoDB
    COMMENT = 'This table tracks all the inbound and outbound payloads in the validation service';
CREATE TABLE IF NOT EXISTS `premiumbillingdb`.`payload_tracker_detail` (
    `payload_tracker_detail_sk` VARCHAR(36) NOT NULL COMMENT 'Primary key of the table',
    `payload_tracker_sk` VARCHAR(36) NOT NULL COMMENT 'The foreign key of the payload tracker table',
    `response_type_code` VARCHAR(45) NOT NULL COMMENT 'The type of response received or sent. e.g. ACK, RESULT etc',
    `response_payload` LONGTEXT NOT NULL,
    `response_payload_id` VARCHAR(45) NULL,
    `payload_direction_type_code` VARCHAR(45) NOT NULL COMMENT 'Identifies the direction of the payload INBOUND or OUTBOUND',
    `src_dest` VARCHAR(100) NOT NULL COMMENT 'Identifies the source of the  payload if direction is inbound and destination if the direction is outbound',
    `created_date` DATETIME NULL COMMENT 'The date when the record was created',
    `updated_date` DATETIME NULL COMMENT 'The date when the record was updated',
    PRIMARY KEY (`payload_tracker_detail_sk`),
    INDEX `payload_tracker_fk_idx` (`payload_tracker_sk` ASC) VISIBLE,
    CONSTRAINT `payload_tracker_fk`
    FOREIGN KEY (`payload_tracker_sk`)
    REFERENCES `premiumbillingdb`.`payload_tracker` (`payload_tracker_sk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    COMMENT = 'The payload tracker detail table, that tracks all the responses received for an outbound payload and all the responses sent for an inbound payload';
CREATE TABLE `premiumbillingdb`.`premium_payment` (
                                                  premium_payment_sk varchar(36) NOT NULL,
                                                  enrollment_span_sk varchar(36) NOT NULL,
                                                  premium_payment DECIMAL NOT NULL,
                                                  payment_date DATE NOT NULL,
                                                  created_date DATETIME NOT NULL,
                                                  enrollment_span_code varchar(100) NOT NULL,
                                                  updated_date DATETIME NOT NULL,
                                                  CONSTRAINT premium_payment_pk PRIMARY KEY (premium_payment_sk)
)
    ENGINE=InnoDB
    COMMENT = 'This table stores all premium payments that are received for the enrollment span'
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
