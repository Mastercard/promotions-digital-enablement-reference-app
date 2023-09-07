package com.mastercard.developer.service;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TransactionApi;
import org.openapitools.client.model.PagedResponseGetTransactionDto;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionService {
    private final TransactionApi transactionApi;

    public TransactionService(ApiClient apiClient) {
        this.transactionApi = new TransactionApi(apiClient);
    }

    public PagedResponseGetTransactionDto getTransactions(String accountId, String fromDate, String toDate, String promotionId, Integer offset, Integer limit) throws ApiException {
        return transactionApi.getTransactionsUsingGET(accountId, fromDate, toDate, promotionId, offset, limit);
    }

}
