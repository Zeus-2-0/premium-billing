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

    private LocalDate startPeriod;

    private LocalDate endPeriod;

    private BigDecimal responsibilityAmount;

    private BigDecimal paidThroughAmount;

    private double premiumPercent;

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
