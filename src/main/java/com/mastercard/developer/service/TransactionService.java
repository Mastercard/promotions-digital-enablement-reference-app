package com.mastercard.developer.service;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TransactionsApi;
import org.openapitools.client.model.PagedTransaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class TransactionService {
    private final TransactionsApi transactionApi;

    public TransactionService(ApiClient apiClient) {
        this.transactionApi = new TransactionsApi(apiClient);
    }

    public PagedTransaction getTransactions(UUID accountId, LocalDate fromDate, LocalDate toDate, UUID promotionId, Integer offset, Integer limit) throws ApiException {
        return transactionApi.getTransactions(accountId, fromDate, toDate, promotionId, offset, limit);
    }

}
