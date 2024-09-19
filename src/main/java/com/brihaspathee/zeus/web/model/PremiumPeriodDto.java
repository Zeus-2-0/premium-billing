package com.brihaspathee.zeus.web.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 15, July 2024
 * Time: 2:26 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.web.model
 * To change this template use File | Settings | File and Code Template
 */
@Setter
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class PremiumPeriodDto {

    /**
     * Start of the premium period
     */
    private LocalDate startPeriod;

    /**
     * End of the premium period
     */
    private LocalDate endPeriod;

    /**
     * Monthly responsible amount
     */
    private BigDecimal responsibilityAmount;

    /**
     * Cumulative amounts that are to be paid by the end of the period
     */
    private BigDecimal paidThroughAmount;

    /**
     * The percentage of the responsibility amount for the period
     * It will be 1 if the period is for a full month
     * It will be less than 1 if the period is for less than a month
     */
    private double premiumPercent;

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "PremiumPeriodDto{" +
                "startPeriod=" + startPeriod +
                ", endPeriod=" + endPeriod +
                ", responsibilityAmount=" + responsibilityAmount +
                ", paidThroughAmount=" + paidThroughAmount +
                ", premiumPercent=" + premiumPercent +
                '}';
    }
}
