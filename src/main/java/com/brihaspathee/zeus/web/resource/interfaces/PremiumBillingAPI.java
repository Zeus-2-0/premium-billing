package com.brihaspathee.zeus.web.resource.interfaces;

import com.brihaspathee.zeus.dto.account.AccountDto;
import com.brihaspathee.zeus.dto.account.AccountList;
import com.brihaspathee.zeus.dto.account.PremiumPaymentDto;
import com.brihaspathee.zeus.exception.ApiExceptionList;
import com.brihaspathee.zeus.web.response.ZeusApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 24, April 2024
 * Time: 9:53 PM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.web.resource.interfaces
 * To change this template use File | Settings | File and Code Template
 */
@RequestMapping("/api/v1/zeus/premium-billing")
@Validated
public interface PremiumBillingAPI {

    /**
     * Clean up the entire db
     * @return
     */
    @Operation(
            operationId = "Delete all data",
            method = "DELETE",
            description = "Delete all data",
            tags = {"premium-billing"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Data deleted successfully",
                    content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = ZeusApiResponse.class))
                    })
    })
    @DeleteMapping("/delete")
    ResponseEntity<ZeusApiResponse<String>> cleanUp();

    /**
     * Create a new payment for the enrollment span
     * @param premiumPaymentDto
     * @return
     */
    @Operation(
            operationId = "Create a new payemnt",
            method = "POST",
            description = "Create a new payment",
            tags = {"premium-billing"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully created the payment",
                    content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = AccountDto.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = ApiExceptionList.class))
                    }),
            @ApiResponse(responseCode = "409",
                    description = "Conflict",
                    content = {
                            @Content(mediaType = "application/json",schema = @Schema(implementation = ApiExceptionList.class))
                    })
    })
    @PostMapping(path = "/payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ZeusApiResponse<PremiumPaymentDto>> createPayment(@RequestBody @Valid
                                                                     PremiumPaymentDto premiumPaymentDto)
            throws JsonProcessingException;

    /**
     * Get the account related to the account number that is passed in as input
     * @param accountNumber
     * @return
     */
    @Operation(
            operationId = "Get Account by account number",
            method = "GET",
            description = "Get the details of the account by its number",
            tags = {"premium-billing"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the details of the account",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountDto.class))
                            }
                    )
            }
    )
    @GetMapping("/{accountNumber}")
    ResponseEntity<ZeusApiResponse<AccountList>> getAccountByNumber(@PathVariable("accountNumber") String accountNumber);
}
