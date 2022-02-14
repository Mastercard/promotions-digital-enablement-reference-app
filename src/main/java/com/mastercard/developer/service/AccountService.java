package com.mastercard.developer.service;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountManagementApi;
import org.openapitools.client.model.AccountSearch;
import org.openapitools.client.model.PagedResponseOfAccountSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {

    private final AccountManagementApi accountManagementApi;

    @Autowired
    public AccountService(ApiClient cryptoApiClient) {
        this.accountManagementApi = new AccountManagementApi(cryptoApiClient);
    }

    public PagedResponseOfAccountSearch searchAccount(Integer offset, Integer limit, AccountSearch accountSearch) throws ApiException {
        return this.accountManagementApi.searchAccount(offset, limit, accountSearch);
    }
}
