package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.constants.EnrollmentSpanStatus;
import com.brihaspathee.zeus.domain.entity.Account;
import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import com.brihaspathee.zeus.domain.entity.PremiumPayment;
import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import com.brihaspathee.zeus.domain.repository.EnrollmentSpanRepository;
import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.account.EnrollmentSpanDto;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;
import com.brihaspathee.zeus.exception.EnrollmentSpanNotFoundException;
import com.brihaspathee.zeus.helper.interfaces.EnrollmentSpanHelper;
import com.brihaspathee.zeus.helper.interfaces.PremiumPaymentHelper;
import com.brihaspathee.zeus.helper.interfaces.PremiumSpanHelper;
import com.brihaspathee.zeus.mapper.interfaces.EnrollmentSpanMapper;
import com.brihaspathee.zeus.service.interfaces.MemberManagementService;
import com.brihaspathee.zeus.web.model.PremiumPeriodDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 29, April 2024
 * Time: 5:16 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.helper.impl
 * To change this template use File | Settings | File and Code Template
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentSpanHelperImpl implements EnrollmentSpanHelper {

    /**
     * Instance of enrollment span repository
     */
    private final EnrollmentSpanRepository enrollmentSpanRepository;

    /**
     * Instance of the enrollment span mapper
     */
    private final EnrollmentSpanMapper enrollmentSpanMapper;

    /**
     * The instance of the premium span helper
     */
    private final PremiumSpanHelper premiumSpanHelper;

    /**
     * The instance of the premium payment helper
     */
    private final PremiumPaymentHelper premiumPaymentHelper;

    /**
     * Instance of the member management service
     */
    private final MemberManagementService memberManagementService;


    /**
     * Update the enrollment spans
     * @param account - The account for which the enrollment span is to be updated
     * @param enrollmentSpanDtos - the enrollment spans that needs to be updated
     */
    @Override
    public void updateEnrollmentSpan(Account account, List<EnrollmentSpanDto> enrollmentSpanDtos) {
        // return if the list is null or empty
        if(enrollmentSpanDtos == null || enrollmentSpanDtos.isEmpty()){
            return;
        }
        // iterate through the enrollment span to see if the enrollment span is changed
        enrollmentSpanDtos.forEach(enrollmentSpanDto -> {
            EnrollmentSpan enrollmentSpan = getEnrollmentSpan(enrollmentSpanDto.getEnrollmentSpanCode());
            if(enrollmentSpanDto.getChanged().get()){
                log.info("About to update the enrollment span");
                // update only if the enrollment span has changed
                // Check if the enrollment span is present with the account

                if(enrollmentSpan == null){
                    // enrollment span is not present with the account
                    // create the enrollment span
                    updateEnrollmentSpan(account, enrollmentSpanDto);
                }else{
                    // The enrollment span exist
                    // so set the enrollment span sk before updating it
                    enrollmentSpanDto.setEnrollmentSpanSK(enrollmentSpan.getEnrollmentSpanSK());
                    updateEnrollmentSpan(account, enrollmentSpanDto);
                }
            }else{
                // enrollment span is not changed
                // check if premium spans have changed
                if(enrollmentSpanDto.getPremiumSpans() != null){
                    premiumSpanHelper.updatePremiumSpans(enrollmentSpan, enrollmentSpanDto.getPremiumSpans().stream().toList());
                }

            }
        });

    }

    /**
     * Update enrollment span
     * @param enrollmentSpan
     * @param effectuationDate
     * @param paidThruDate
     * @param status
     */
    @Override
    public void updateEnrollmentSpan(EnrollmentSpan enrollmentSpan,
                                     LocalDate effectuationDate,
                                     LocalDate paidThruDate,
                                     String status) {
        LocalDate currentPTD = enrollmentSpan.getPaidThroughDate();
        String currentStatus = enrollmentSpan.getStatusTypeCode();
        // Check if either the paid through date or the current status is different from what is passed in the input
        if(currentPTD == null || !paidThruDate.isEqual(currentPTD) || !currentStatus.equals(status)){
            enrollmentSpan.setPaidThroughDate(paidThruDate);
            enrollmentSpan.setClaimPaidThroughDate(paidThruDate);
            enrollmentSpan.setStatusTypeCode(status);
            enrollmentSpan.setEffectuationDate(effectuationDate);
            enrollmentSpan = enrollmentSpanRepository.save(enrollmentSpan);
            memberManagementService.updateEnrollmentSpan(EnrollmentSpanDto.builder()
                            .enrollmentSpanCode(enrollmentSpan.getEnrollmentSpanCode())
                            .effectuationDate(enrollmentSpan.getEffectuationDate())
                            .paidThroughDate(enrollmentSpan.getPaidThroughDate())
                            .statusTypeCode(enrollmentSpan.getStatusTypeCode())
                    .build());
        }
    }

    /**
     * Create the enrollment span dto from enrollment span and set it in the account dto
     * @param accountDto
     * @param enrollmentSpans
     */
    @Override
    public void setEnrollmentSpan(AccountDto accountDto, List<EnrollmentSpan> enrollmentSpans) {
        Set<EnrollmentSpanDto> enrollmentSpanDtos = new HashSet<>();
        enrollmentSpans.forEach(enrollmentSpan -> {
            EnrollmentSpanDto enrollmentSpanDto = enrollmentSpanMapper.enrollmentSpanToEnrollmentSpanDto(enrollmentSpan);
            premiumSpanHelper.setPremiumSpan(enrollmentSpanDto, enrollmentSpan.getPremiumSpans());
            premiumPaymentHelper.setPremiumPayment(enrollmentSpanDto, enrollmentSpan.getPremiumPayments());
            enrollmentSpanDtos.add(enrollmentSpanDto);
        });
        accountDto.setEnrollmentSpans(enrollmentSpanDtos);
    }

    /**
     * Manage premium payments for an enrollment span and
     * update the enrollment span appropriately
     * @param premiumPaymentDto
     */
    @Override
    public void createPremiumPayment(PremiumPaymentDto premiumPaymentDto) {
        String enrollmentSpanCode = premiumPaymentDto.getEnrollmentSpanCode();
        Optional<EnrollmentSpan> optionalEnrollmentSpan = enrollmentSpanRepository.findEnrollmentSpanByEnrollmentSpanCode(enrollmentSpanCode);
        if (optionalEnrollmentSpan.isEmpty()){
            throw new EnrollmentSpanNotFoundException("Enrollment span with enrollment span code " +
                    enrollmentSpanCode + " not found");
        }
        EnrollmentSpan enrollmentSpan = optionalEnrollmentSpan.get();
        List<PremiumPayment> payments = premiumPaymentHelper.createPremiumPayment(enrollmentSpan, premiumPaymentDto);
        LocalDate effectuationDate = null;
        if(enrollmentSpan.getEffectuationDate() == null){
            effectuationDate = determineEffectuationDate(payments);
        }else{
            effectuationDate = enrollmentSpan.getEffectuationDate();
        }
        LocalDate paidThroughDate = determinePaidThruDate(enrollmentSpan.getPremiumSpans(),
                payments);

        if (paidThroughDate != null){
            updateEnrollmentSpan(enrollmentSpan,
                    effectuationDate,
                    paidThroughDate,
                    EnrollmentSpanStatus.ENROLLED.name());
        }
    }

    /**
     * Determine effectuation date of an enrollment span from the list of payments
     * @param payments
     * @return
     */
    private LocalDate determineEffectuationDate(List<PremiumPayment> payments) {
        LocalDate effectuationDate = null;
        if(!payments.isEmpty()){
            if(payments.size() == 1){
                PremiumPayment premiumPayment = payments.getFirst();
                effectuationDate = premiumPayment.getPaymentDate();
            }else{
                effectuationDate = payments.stream()
                        .map(PremiumPayment::getPaymentDate)
                        .filter(Objects::nonNull)
                        .min(LocalDate::compareTo)
                        .orElse(null);
            }
        }
        return effectuationDate;
    }

    /**
     * Determine the paid through date
     * @param premiumSpans
     * @param premiumPayments
     * @return
     */
    private LocalDate determinePaidThruDate(List<PremiumSpan> premiumSpans,
                                            List<PremiumPayment> premiumPayments) {
        AtomicReference<List<PremiumPeriodDto>> premiumPeriods = new AtomicReference<>(new ArrayList<>());
        AtomicReference<BigDecimal> cumulativeTotalRestAmt = new AtomicReference<>(BigDecimal.ZERO);
        premiumSpans.forEach(premiumSpan -> {
            BigDecimal totRestAmt = premiumSpan.getTotalResponsibleAmount();
            LocalDate startDate = premiumSpan.getStartDate();
            LocalDate endDate = premiumSpan.getEndDate();
            List<PremiumPeriodDto> localPremiumPeriods = premiumPeriods(startDate, endDate, totRestAmt);

            for (PremiumPeriodDto premiumPeriodDto : localPremiumPeriods) {
                cumulativeTotalRestAmt.set(setTotalPaidThruAmount(premiumPeriodDto, cumulativeTotalRestAmt.get()));
            }
            premiumPeriods.get().addAll(localPremiumPeriods);
        });
//        premiumPeriods.get().forEach(premiumPeriodDto -> {
//            log.info("Premium period Start Date: {}", premiumPeriodDto.getStartPeriod());
//            log.info("Premium period End Date: {}", premiumPeriodDto.getEndPeriod());
//            log.info("Premium period responsibility amount: {}", premiumPeriodDto.getResponsibilityAmount());
//            log.info("Premium period Premium Percent: {}", premiumPeriodDto.getPremiumPercent());
//            log.info("Premium period Paid Through Amount: {}", premiumPeriodDto.getPaidThroughAmount());
//        });
        BigDecimal premiumPaymentTotal = premiumPayments.stream().map(PremiumPayment::getPremiumPayment).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<PremiumPeriodDto> paidThroughPeriods =
                premiumPeriods.get()
                        .stream()
                        .takeWhile(premiumPeriodDto ->
                                premiumPeriodDto.getPaidThroughAmount()
                                        .compareTo(premiumPaymentTotal) <= 0)
                        .toList();
        if(paidThroughPeriods.isEmpty()){
            return null;
        }else {
            return paidThroughPeriods.getLast().getEndPeriod();
        }
    }

    /**
     * Identify the list of end dates of each month between the two dates
     * @param startDate
     * @param endDate
     * @param totRestAmt
     * @return
     */
    private List<PremiumPeriodDto> premiumPeriods(LocalDate startDate,
                                                  LocalDate endDate,
                                                  BigDecimal totRestAmt) {
        List<LocalDate> endOfMonthDates = new ArrayList<>();
        List<PremiumPeriodDto> premiumPeriodDtos = new ArrayList<>();
        LocalDate monthEndDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        while (!monthEndDate.isAfter(endDate)) {
//            endOfMonthDates.add(monthEndDate);
            LocalDate monthStartDate = monthEndDate.with(TemporalAdjusters.firstDayOfMonth());
            if(monthStartDate.isBefore(startDate)){
                double premiumPercent = monthsBetween(startDate, monthEndDate);
                premiumPeriodDtos.add(PremiumPeriodDto.builder()
                        .startPeriod(startDate)
                        .endPeriod(monthEndDate)
                        .premiumPercent(premiumPercent)
                        .responsibilityAmount(totRestAmt.multiply(BigDecimal.valueOf(premiumPercent)))
                        .build());
            }else{
                double premiumPercent = monthsBetween(monthStartDate, monthEndDate);
                premiumPeriodDtos.add(PremiumPeriodDto.builder()
                        .startPeriod(monthStartDate)
                        .endPeriod(monthEndDate)
                        .premiumPercent(premiumPercent)
                        .responsibilityAmount(totRestAmt.multiply(BigDecimal.valueOf(premiumPercent)))
                        .build());
            }
            monthEndDate = monthEndDate.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        }
        if(!premiumPeriodDtos.isEmpty()){
            PremiumPeriodDto premiumPeriodDto = premiumPeriodDtos.getLast();
            if(!premiumPeriodDto.getEndPeriod().isEqual(endDate)){
                LocalDate monthStartDate = endDate.with(TemporalAdjusters.firstDayOfMonth());
                double premiumPercent = monthsBetween(monthStartDate, endDate);
                premiumPeriodDtos.add(PremiumPeriodDto.builder()
                        .startPeriod(monthStartDate)
                        .endPeriod(endDate)
                        .premiumPercent(premiumPercent)
                        .responsibilityAmount(totRestAmt.multiply(BigDecimal.valueOf(premiumPercent)))
                        .build());
            }
        }else{
            double premiumPercent = monthsBetween(startDate, endDate);
            premiumPeriodDtos.add(PremiumPeriodDto.builder()
                    .startPeriod(startDate)
                    .endPeriod(endDate)
                    .premiumPercent(premiumPercent)
                    .responsibilityAmount(totRestAmt.multiply(BigDecimal.valueOf(premiumPercent)))
                    .build());
        }
        return premiumPeriodDtos;
    }

    /**
     * Determines the number of months between the two given dates
     * @param startDate
     * @param endDate
     * @return
     */
    private double monthsBetween(LocalDate startDate, LocalDate endDate) {

        // Get the number of full months between the two dates
        long fullMonthsBetween = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1));

        // Calculate the remaining days if the end date is not the start of a new month
        LocalDate tempDate = startDate.plusMonths(fullMonthsBetween);
        if (tempDate.isAfter(endDate)) {
            fullMonthsBetween--;
            tempDate = startDate.plusMonths(fullMonthsBetween);
        }

        // Calculate the days remaining after the full months
        long remainingDays = ChronoUnit.DAYS.between(tempDate, endDate.plusDays(1)); // plusDays(1) to include end date

        // Check if remaining days complete a month
        double totalMonths = fullMonthsBetween;
        if (remainingDays == tempDate.lengthOfMonth()) {
            totalMonths += 1;
        } else if (remainingDays > 0) {
            totalMonths += (double) remainingDays / tempDate.lengthOfMonth();
        }
//        log.info("Total number of months between {} and {} is: {}", startDate, endDate, totalMonths);
        return totalMonths;
    }

    /**
     * Set the total paid through amount
     * @param premiumPeriodDto
     * @param totalPaidThruAmount
     * @return
     */
    private BigDecimal setTotalPaidThruAmount(PremiumPeriodDto premiumPeriodDto,
                                              BigDecimal totalPaidThruAmount) {
//        log.info("Paid Through Amount before add {}", totalPaidThruAmount);
        totalPaidThruAmount = totalPaidThruAmount.add(premiumPeriodDto.getResponsibilityAmount());
//        log.info("Premium period dto:{}", premiumPeriodDto);
//        log.info("Paid Through Amount after add {}", totalPaidThruAmount);
        premiumPeriodDto.setPaidThroughAmount(totalPaidThruAmount.setScale(2, RoundingMode.HALF_UP));
        return totalPaidThruAmount;

    }

    /**
     * get the enrollment span matching the enrollment span code if present in the database
     * @param enrollmentSpanCode
     * @return
     */
    private EnrollmentSpan getEnrollmentSpan(String enrollmentSpanCode){
        Optional<EnrollmentSpan> optional = enrollmentSpanRepository.findEnrollmentSpanByEnrollmentSpanCode(enrollmentSpanCode);
        return optional.orElse(null);
    }

    /**
     * Create the new enrollment span
     * @param account - the account to which the enrollment span is to be associated
     * @param enrollmentSpanDto - the enrollment span to be created
     */
    private void updateEnrollmentSpan(Account account, EnrollmentSpanDto enrollmentSpanDto){
        EnrollmentSpan enrollmentSpan = enrollmentSpanMapper.enrollmentSpanDtoToEnrollmentSpan(enrollmentSpanDto);
        enrollmentSpan.setAccount(account);
        log.info("Enrollment span to be created is:{}", enrollmentSpan.getEnrollmentSpanCode());
        enrollmentSpan = enrollmentSpanRepository.save(enrollmentSpan);
        log.info("Enrollment span created is:{}", enrollmentSpan.getEnrollmentSpanSK());
        premiumSpanHelper.updatePremiumSpans(enrollmentSpan,
                enrollmentSpanDto
                        .getPremiumSpans()
                        .stream()
                        .toList());
    }
}
