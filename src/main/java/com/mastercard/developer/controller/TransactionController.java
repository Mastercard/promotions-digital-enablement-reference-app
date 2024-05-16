package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.TransactionService;
import com.mastercard.developer.validator.TransactionValidator;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.PagedTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "", produces = {"application/json"})
public class TransactionController {

    private TransactionValidator transactionValidator;
    private TransactionService transactionService;

    public TransactionController(TransactionValidator transactionValidator, TransactionService transactionService) {
        this.transactionValidator = transactionValidator;
        this.transactionService = transactionService;
    }

    /*---------------------------------------------------------------- GET Transaction API -------------------------------------------------------*/

    /**
     * @param accountId
     * @param fromDate
     * @param toDate
     * @param promotionId
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping(value = "/transactions")
    public PagedTransaction getTransactions(@RequestParam(name = "account_id", required = false) UUID accountId,
                                                          @RequestParam(name = "from_date", required = false) LocalDate fromDate,
                                                          @RequestParam(name = "to_date", required = false) LocalDate toDate,
                                                          @RequestParam(name = "promotion_id", required = false) UUID promotionId,
                                                          @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                          @RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
        transactionValidator.validateTransactionRequest(accountId);
        transactionValidator.validateTransactionDates(fromDate, toDate, 13);
        transactionValidator.validatePaginationParams(offset, limit);
        try {
            log.info("Method : getTransactions, Message : Getting transactions");
            PagedTransaction response = transactionService.getTransactions(accountId, fromDate, toDate, promotionId, offset, limit);
            if (response != null) {
                log.debug("Method : getTransactions, Message :Successfully got the transactions" + response);
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : getTransactions, Message : Failed to get the transactions");
            throw new InvalidRequest(ex.getResponseBody());
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }
}
