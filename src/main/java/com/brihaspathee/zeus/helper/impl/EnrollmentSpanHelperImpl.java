package com.brihaspathee.zeus.helper.impl;

import com.brihaspathee.zeus.constants.EnrollmentSpanStatus;
import com.brihaspathee.zeus.domain.entity.Account;
import com.brihaspathee.zeus.domain.entity.EnrollmentSpan;
import com.brihaspathee.zeus.domain.entity.PremiumPayment;
import com.brihaspathee.zeus.domain.entity.PremiumSpan;
import com.brihaspathee.zeus.domain.repository.EnrollmentSpanRepository;
import com.brihaspathee.zeus.dto.account.*;
import com.brihaspathee.zeus.exception.EnrollmentSpanNotFoundException;
import com.brihaspathee.zeus.helper.interfaces.EnrollmentSpanHelper;
import com.brihaspathee.zeus.helper.interfaces.PremiumPaymentHelper;
import com.brihaspathee.zeus.helper.interfaces.PremiumSpanHelper;
import com.brihaspathee.zeus.mapper.interfaces.EnrollmentSpanMapper;
import com.brihaspathee.zeus.mapper.interfaces.PremiumPaymentMapper;
import com.brihaspathee.zeus.mapper.interfaces.PremiumSpanMapper;
import com.brihaspathee.zeus.service.TransactionProcessingContext;
import com.brihaspathee.zeus.service.interfaces.AccountProcessorService;
import com.brihaspathee.zeus.service.interfaces.MemberManagementService;
import com.brihaspathee.zeus.web.model.PremiumPeriodDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
     * Instance of the account processor service
     */
    private final AccountProcessorService accountProcessorService;

    /**
     * Instance of the premium span mapper
     */
    private final PremiumSpanMapper premiumSpanMapper;


    /**
     * Update the enrollment spans
     * @param context - Context for the transaction
     * @param account - The account for which the enrollment span is to be updated
     * @param enrollmentSpanDtos - the enrollment spans that needs to be updated
     */
    @Override
    public void updateEnrollmentSpan(TransactionProcessingContext context, Account account, List<EnrollmentSpanDto> enrollmentSpanDtos) {
        // return if the list is null or empty
        if(enrollmentSpanDtos == null || enrollmentSpanDtos.isEmpty()){
            return;
        }
        // iterate through the enrollment span to see if the enrollment span is changed
        enrollmentSpanDtos.forEach(enrollmentSpanDto -> {
            EnrollmentSpan enrollmentSpan = getEnrollmentSpan(enrollmentSpanDto.getEnrollmentSpanCode());
            if(enrollmentSpan == null){
                // enrollment span is not present with the account
                // create the enrollment span
                updateEnrollmentSpan(context, account, null, enrollmentSpanDto);
            }else{
                // The enrollment span exist
                // so set the enrollment span sk before updating it
                log.info("Updating enrollment span: {}", enrollmentSpanDto.getEnrollmentSpanCode());
                log.info("Status of the enrollment span: {}", enrollmentSpanDto.getEnrollmentSpanCode());
                enrollmentSpanDto.setEnrollmentSpanSK(enrollmentSpan.getEnrollmentSpanSK());
                updateEnrollmentSpan(context, account, enrollmentSpan, enrollmentSpanDto);
//                // The enrollment span exists, check if it has changed
//                if (enrollmentSpanDto.getChanged().get()){
//                    // The enrollment span exist
//                    // so set the enrollment span sk before updating it
//                    enrollmentSpanDto.setEnrollmentSpanSK(enrollmentSpan.getEnrollmentSpanSK());
//                    updateEnrollmentSpan(context, account, enrollmentSpan, enrollmentSpanDto);
//                }else{
//                    // enrollment span is not changed
//                    // check if premium spans have changed
//                    if(enrollmentSpanDto.getPremiumSpans() != null){
//                        premiumSpanHelper.updatePremiumSpans(enrollmentSpan, enrollmentSpanDto.getPremiumSpans().stream().toList());
//                    }
//                }

            }
//            if(enrollmentSpanDto.getChanged().get()){
//                log.info("About to update the enrollment span");
//                // update only if the enrollment span has changed
//                // Check if the enrollment span is present with the account
//
//                if(enrollmentSpan == null){
//                    // enrollment span is not present with the account
//                    // create the enrollment span
//                    updateEnrollmentSpan(context, account, enrollmentSpanDto, true);
//                }else{
//                    // The enrollment span exist
//                    // so set the enrollment span sk before updating it
//                    enrollmentSpanDto.setEnrollmentSpanSK(enrollmentSpan.getEnrollmentSpanSK());
//                    updateEnrollmentSpan(context, account, enrollmentSpanDto, false);
//                }
//            }else{
//                // enrollment span is not changed
//                // check if premium spans have changed
//                if(enrollmentSpanDto.getPremiumSpans() != null){
//                    premiumSpanHelper.updatePremiumSpans(enrollmentSpan, enrollmentSpanDto.getPremiumSpans().stream().toList());
//                }
//
//            }
        });

    }

    /**
     * Update enrollment span
     * @param enrollmentSpan
     * @param effectuationDate
     * @param paidThruDate
     * @param status
     */
    private void updateEnrollmentSpan(EnrollmentSpan enrollmentSpan,
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
        if(premiumPayments == null || premiumPayments.isEmpty()){
            return null;
        }
        // filter our any canceled premium spans
        // The calculation of paid thru date has to be done only using active premium spans
        List<PremiumSpan> nonCancelledPremiumSpans = new ArrayList<>(premiumSpans.stream()
                .filter(premiumSpan ->
                        premiumSpan.getStatusTypeCode().equals("ACTIVE")).toList());
        AtomicReference<List<PremiumPeriodDto>> premiumPeriods = new AtomicReference<>(new ArrayList<>());
        AtomicReference<BigDecimal> cumulativeTotalRestAmt = new AtomicReference<>(BigDecimal.ZERO);
        log.info("Non cancelled premium spans:{}", nonCancelledPremiumSpans.size());
        nonCancelledPremiumSpans.sort(Comparator.comparing(PremiumSpan::getStartDate));
        nonCancelledPremiumSpans.forEach(premiumSpan -> {
            BigDecimal totRestAmt = premiumSpan.getTotalResponsibleAmount();
            LocalDate startDate = premiumSpan.getStartDate();
            LocalDate endDate = premiumSpan.getEndDate();
            List<PremiumPeriodDto> localPremiumPeriods = premiumPeriods(startDate, endDate, totRestAmt);
            for (PremiumPeriodDto premiumPeriodDto : localPremiumPeriods) {
                cumulativeTotalRestAmt.set(setTotalPaidThruAmount(premiumPeriodDto, cumulativeTotalRestAmt.get()));
            }
            premiumPeriods.get().addAll(localPremiumPeriods);
        });
        premiumPeriods.get().sort(Comparator.comparing(PremiumPeriodDto::getStartPeriod));
        premiumPeriods.get().forEach(premiumPeriodDto -> {
            log.info("Premium period Start Date: {}", premiumPeriodDto.getStartPeriod());
            log.info("Premium period End Date: {}", premiumPeriodDto.getEndPeriod());
            log.info("Premium period responsibility amount: {}", premiumPeriodDto.getResponsibilityAmount());
            log.info("Premium period Premium Percent: {}", premiumPeriodDto.getPremiumPercent());
            log.info("Premium period Paid Through Amount: {}", premiumPeriodDto.getPaidThroughAmount());
        });
        BigDecimal premiumPaymentTotal = premiumPayments.stream().map(PremiumPayment::getPremiumPayment).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<PremiumPeriodDto> paidThroughPeriods =
                premiumPeriods.get()
                        .stream()
                        .takeWhile(premiumPeriodDto ->
                                premiumPeriodDto.getPaidThroughAmount()
                                        .compareTo(premiumPaymentTotal) <= 0)
                        .toList();
        if(paidThroughPeriods.isEmpty()){
            log.info("Paid through periods is empty");
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
    private static List<PremiumPeriodDto> premiumPeriods(LocalDate startDate,
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
        premiumPeriodDtos.sort(Comparator.comparing(PremiumPeriodDto::getStartPeriod));
        return premiumPeriodDtos;
    }

    /**
     * Determines the number of months between the two given dates
     * @param startDate
     * @param endDate
     * @return
     */
    private static double monthsBetween(LocalDate startDate, LocalDate endDate) {

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
    private static BigDecimal setTotalPaidThruAmount(PremiumPeriodDto premiumPeriodDto,
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
     * @param context - context of the transaction
     * @param account - the account to which the enrollment span is to be associated
     * @param currentEnrollmentSpan - The current state of the enrollment span if it is present in the account
     * @param enrollmentSpanDto - the enrollment span to be created
     */
    private void updateEnrollmentSpan(TransactionProcessingContext context,
                                      Account account,
                                      EnrollmentSpan currentEnrollmentSpan,
                                      EnrollmentSpanDto enrollmentSpanDto){
        checkForEnrollmentSpanUpdates(context, account, currentEnrollmentSpan, enrollmentSpanDto);
        if(enrollmentSpanDto.getChanged().get() ||
        context.isSendUpdateToMMS()){
            // if either the enrollment span was updated based on the transaction that was received
            // or it was updated based on the paid through date determination
            // the enrollment span has to be updated
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
        }else{
            // enrollment span is not updated
            // check if any updates are necessary for premium spans
            premiumSpanHelper.updatePremiumSpans(currentEnrollmentSpan,
                    enrollmentSpanDto
                            .getPremiumSpans()
                            .stream()
                            .toList());
        }

        if(context.isSendUpdateToMMS()){
            // If an update about the enrollment span is to be sent to MMS then send the update
            EnrollmentSpanDto updatedEnrollmentSpan = EnrollmentSpanDto.builder()
                    .enrollmentSpanCode(enrollmentSpanDto.getEnrollmentSpanCode())
                    .statusTypeCode(enrollmentSpanDto.getStatusTypeCode())
                    .effectuationDate(enrollmentSpanDto.getEffectuationDate())
                    .paidThroughDate(enrollmentSpanDto.getPaidThroughDate())
                    .claimPaidThroughDate(enrollmentSpanDto.getClaimPaidThroughDate())
                    .build();
            memberManagementService.updateEnrollmentSpan(updatedEnrollmentSpan);
        }
    }

    /**
     * Determine the paid through date for an enrollment span the is either newly created
     * or has been updated by transaction processing
     * @param context - context of the transaction
     * @param account  - Account associated with the enrollment span
     * @param currentEnrollmentSpan - current state of the enrollment span if it exists
     * @param enrollmentSpanDto - the new enrollment span
     */
    private void checkForEnrollmentSpanUpdates(TransactionProcessingContext context,
                                       Account account,
                                       EnrollmentSpan currentEnrollmentSpan,
                                       EnrollmentSpanDto enrollmentSpanDto){
        String spanStatus = enrollmentSpanDto.getStatusTypeCode();
        log.info("Status of the span to be updated:{}", spanStatus);
        List<PremiumPayment> premiumPayments = premiumPaymentHelper.getPremiumPayments(enrollmentSpanDto.getEnrollmentSpanCode());
        if(currentEnrollmentSpan == null){
            // Do these steps if a new enrollment span is to be created
            // If enrollment span status is enrolled and paid through date is not set
            // then set the paid through date to the end of the year
            if(spanStatus.equals(EnrollmentSpanStatus.ENROLLED.toString())){
                EnrollmentSpan priorEnrollmentSpan = getPriorEnrollmentSpan(account, enrollmentSpanDto.getStartDate());
                LocalDate paidThruDate = determinePaidThruDate(priorEnrollmentSpan, enrollmentSpanDto, premiumPayments);
                enrollmentSpanDto.setPaidThroughDate(paidThruDate);
                enrollmentSpanDto.setClaimPaidThroughDate(paidThruDate);
                context.setSendUpdateToMMS(true);
            }
        }else{
            // the enrollment span is not new
            LocalDate currentPaidThruDate = currentEnrollmentSpan.getPaidThroughDate();
            if(currentPaidThruDate == null &&
                    spanStatus.equals(EnrollmentSpanStatus.ENROLLED.toString())){
                // There is no paid through date in the current enrollment span and the new status is enrolled
                // Determine if a paid thru date needs to be set
                LocalDate paidThruDate = determinePaidThruDate(null, enrollmentSpanDto, premiumPayments);
                if(paidThruDate !=null){
                    enrollmentSpanDto.setPaidThroughDate(paidThruDate);
                    enrollmentSpanDto.setClaimPaidThroughDate(paidThruDate);
                    context.setSendUpdateToMMS(true);
                }
            }else if(currentPaidThruDate != null &&
                    !spanStatus.equals(EnrollmentSpanStatus.CANCELED.toString())){
                // the current enrollment span has a paid through date (which means the status cannot be premember)
                // and the new status is not canceled
                // check to see if the effectuation date needs to be removed and enrollment span status needs to be
                //       calculated - This will be done as below
                //                    Retrieve the previous enrollment span that is before this span
                //                    if no previous span exists or if there is a gap in coverage or if the previous span
                //                    is enrolled in a different plan then the effectuation date needs to be removed
                //                    CPTD and PTD needs to be removed, the span status needs to be recalculated
                EnrollmentSpan priorEnrollmentSpan = getPriorEnrollmentSpan(account, enrollmentSpanDto.getStartDate());
                LocalDate paidThruDate = determinePaidThruDate(priorEnrollmentSpan, enrollmentSpanDto, premiumPayments);
                if(paidThruDate!= null){
                    // If paid through date is not null and if it is different
                    // from the current paid through date
                    // then set the PTD and CPTD to the new value
                    if(!paidThruDate.isEqual(currentPaidThruDate)){
                        enrollmentSpanDto.setPaidThroughDate(paidThruDate);
                        enrollmentSpanDto.setClaimPaidThroughDate(paidThruDate);
                        context.setSendUpdateToMMS(true);
                    }
                    // if it is not null but is same as the current date then do not update
                }else{
                    // If new paid through date is null then remove
                    // PTD, CPTD and effectuation date from the span
                    enrollmentSpanDto.setPaidThroughDate(null);
                    enrollmentSpanDto.setClaimPaidThroughDate(null);
                    enrollmentSpanDto.setEffectuationDate(null);
                    context.setSendUpdateToMMS(true);
                }
            }else if(currentPaidThruDate != null &&
                    spanStatus.equals(EnrollmentSpanStatus.CANCELED.toString())){
                // The enrollment span is being canceled and contains a paid through date
                // this means the below dates needs to be set to null as they are no longer appropriate for the span
                // Effectuation Date, Paid through date and Claim Paid through date
                enrollmentSpanDto.setPaidThroughDate(null);
                enrollmentSpanDto.setClaimPaidThroughDate(null);
                enrollmentSpanDto.setEffectuationDate(null);
                context.setSendUpdateToMMS(true);

            }
        }
    }

    /**
     * Determine the paid through date of the enrollment span
     * @param priorEnrollmentSpan - Enrollment span prior to the span to which the paid through date is requested
     * @param enrollmentSpanDto - the enrollment span for which the paid through date is requested
     * @param premiumPayments - Any premium payments received for the enrollment span
     * @return
     */
    private LocalDate determinePaidThruDate(EnrollmentSpan priorEnrollmentSpan,
                                                   EnrollmentSpanDto enrollmentSpanDto,
                                                   List<PremiumPayment> premiumPayments) {
        // paymentPTD is the paid through date determined based on the premium payments
        // made by the member
        LocalDate paymentPTD = determinePaidThruDate(premiumSpanMapper.premiumSpanDtosToPremiumSpans(
                enrollmentSpanDto.getPremiumSpans().stream().toList()),
                premiumPayments);
        log.info("PTD based on payment Date:{}", paymentPTD);
        // psPTD is the paid through date determined purely based on the premium spans total responsibility amount
        // or if the enrollment span has the same plan as the prior enrollment span and does not have any gap
        // in coverage
        LocalDate psPTD;
        // Take all the premium spans where the total responsibility amount is zero
        log.info("Premiums spans present in the dto:{}", enrollmentSpanDto.getPremiumSpans());
        List<PremiumSpanDto> totRespZeroPremiumSpans = enrollmentSpanDto.getPremiumSpans()
                .stream()
                .sorted(Comparator.comparing(PremiumSpanDto::getSequence))
                .takeWhile(premiumSpanDto ->
                        BigDecimal.ZERO.compareTo(
                                premiumSpanDto.getTotalResponsibleAmount()) == 0)
                .toList();
//        List<PremiumSpanDto> totRespZeroPremiumSpans = enrollmentSpanDto.getPremiumSpans()
//                .stream()
//                .filter(premiumSpanDto ->
//                        BigDecimal.ZERO.compareTo(
//                                premiumSpanDto.getTotalResponsibleAmount()) == 0)
//                .toList();
        log.info("Premiums spans where the TOTRESAMT is zero:{}", totRespZeroPremiumSpans);
        if(totRespZeroPremiumSpans.isEmpty()){
            log.info("There are no premium spans where TOTRESAMT is zero");
            log.info("Enrollment span prior to the current enrollment span:{}", priorEnrollmentSpan);
            // If total responsibility amount is not zero even for a single premium span
            // then check if prior enrollment span exist and the prior enrollment span has the
            // same plan and there is no gap in coverage
            // If not the paid through date will be null
            if(priorEnrollmentSpan!=null && isSamePlan(enrollmentSpanDto.getPlanId(), priorEnrollmentSpan.getPlanId()) &&
                    !isThereGapInCoverage(enrollmentSpanDto.getStartDate(), priorEnrollmentSpan)) {
                psPTD = enrollmentSpanDto.getStartDate().with(TemporalAdjusters.lastDayOfMonth());
            } else {
                psPTD = null;
            }
//            }else {
//                // This means either there is no prior enrollment span or
//                // the prior enrollment span does not have the same plan or
//                // there is gap in coverage
//                return null;
//            }

        }else{
            log.info("There are premium spans where TOTRESAMT is zero");
            // if there are premium spans where the total responsibility amount is zero
            // get the end date of the last premium span where the total responsibility amount is zero
            psPTD = totRespZeroPremiumSpans.stream()
                    .max(Comparator.comparing(PremiumSpanDto::getEndDate))
                    .map(PremiumSpanDto::getEndDate).orElse(null);
        }
        log.info("Premium Span PTD:{}", psPTD);
        // The logic is if paymentPTD and psPTD are both present, the final paid through date will be the greatest of the two
        // If only one present then it will be sent back as the paid through date
        // if both are absent then null should be returned
        //
        // 1. Optional.ofNullable(paymentPTD) - Wraps "paymentPTD" in an optional which will be empty if "paymentPTD" is null
        // 2. filter(paymentPaidThruDate -> Optional.ofNullable(psPTD)
        //       --- Applies the filter to paymentPTD is present
        // 3. map(paymentPaidThruDate::isAfter)
        //        --- Applies the mapping function to "psPTD" if present
        //        --- The mapping function checks if "paymentPaidThruDate" is after "psPTD". It returns a boolean indicating
        //            whether "paymentPaidThruDate" is after "psPTD"
        // 4. orElse(true)
        //         --- If psPTD is null, the mapping function cannot be applied, so the orElse provides a default value of true.
        //         --- This means if psPTD is null, the filter condition will be true, allowing paymentPTD to pass the filter.
        // 5. orElse(psPTD)
        //         --- If paymentPTD is present and passes the filter (meaning paymentPaidThruDate is after psPTD or psPTD is null), paymentPTD is returned.
        //         --- If paymentPTD is not present (i.e., it is null), or it does not pass the filter (i.e., paymentPTD is not after psPTD), psPTD is returned.
        return Optional.ofNullable(paymentPTD).filter(paymentPaidThruDate -> Optional.ofNullable(psPTD)
                .map(paymentPaidThruDate::isAfter)
                .orElse(true))
                .orElse(psPTD);
    }

    /**
     * Get the prior enrollment span
     * @param account
     * @param spanStartDate
     * @return
     */
    private EnrollmentSpan getPriorEnrollmentSpan(Account account, LocalDate spanStartDate){
        // get the list of all enrollment spans associated with the account currently
        List<EnrollmentSpan> enrollmentSpans = enrollmentSpanRepository.findEnrollmentSpanByAccount_AccountNumber(
                account.getAccountNumber());
        enrollmentSpans =
                enrollmentSpans.stream()
                        .sorted(Comparator.comparing(EnrollmentSpan::getStartDate))
                        .collect(Collectors.toList());
        enrollmentSpans =
                enrollmentSpans.stream()
                        .takeWhile(
                                enrollmentSpanDto ->
                                        enrollmentSpanDto.getEndDate().isBefore(spanStartDate))
                        .collect(Collectors.toList());
        enrollmentSpans = enrollmentSpans.stream()
                .filter(
                        enrollmentSpan1 ->
                                !enrollmentSpan1.getStatusTypeCode()
                                        .equals(EnrollmentSpanStatus.CANCELED.toString()))
                .collect(Collectors.toList());
        if(!enrollmentSpans.isEmpty()){
            return enrollmentSpans.getLast();
        }else {
            return null;
        }
    }

    /**
     * Determine if the planIds are same
     * @param currentPlanId
     * @param priorPlanId
     * @return
     */
    private boolean isSamePlan(String currentPlanId,
                                      String priorPlanId){
        return currentPlanId.equals(priorPlanId);
    }

    /**
     * Determine if there is a gap between the current and the prior enrollment span
     * @param effectiveStartDate of the enrollment span to be created
     * @param priorEnrollmentSpan the prior year enrollment span
     * @return return true if there is a gap in coverage
     */
    private boolean isThereGapInCoverage(LocalDate effectiveStartDate,
                                                EnrollmentSpan priorEnrollmentSpan){
        if(priorEnrollmentSpan.getStatusTypeCode().equals(EnrollmentSpanStatus.CANCELED.toString())){
            return true;
        }else{
            long numOfDays = ChronoUnit.DAYS.between(priorEnrollmentSpan.getEndDate(),
                    effectiveStartDate);
            // numOfDays will be 1 if the end date of the prior enrollment span is the day prior to the
            // start of the current enrollment span

            // num of days will be greater than one if there is a gap between the end of the prior enrollment span
            // and start of the current enrollment span

            // num of days will be 0 if the end date of the prior enrollment span is same as that of the start date of the
            // current enrollment span

            // number of days will be -ve if the end date of the prior enrollment span is greater than the start date
            // of the current enrollment span
            if(numOfDays > 1){
                // This means that there is a gap between the end of the prior enrollment span
                // and start of the current enrollment span hence return tru
                return true;
            }else{
                // in all other cases there is no gap, hence return false
                return false;
            }
        }
    }
}
