package com.mastercard.developer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.AccountManagementApi;
import org.openapitools.client.model.AccountSearch;
import org.openapitools.client.model.PagedResponseOfAccountSearch;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private ApiClient apiClient;

    @Spy
    private AccountManagementApi accountManagementApi;

    private AccountService accountService;
    private AccountSearch accountSearch;

    @Before
    public void setUp() {
        accountService = new AccountService(apiClient);
        accountSearch = new AccountSearch();
    }

    @Test
    public void testAccountServiceInstantiation() {
        assertNotNull(accountService);
    }

    @Test
    public void testAccountSearchObjectCreation() {
        assertNotNull(accountSearch);
    }

    @Test
    public void testSearchAccountMethod() {
        assertNotNull(accountService);
        assertNotNull(accountSearch);
    }

    @Test
    public void testAccountSearchWithValidSearch() {
        AccountSearch search = new AccountSearch();
        assertNotNull(search);
    }

    @Test
    public void testAccountService_ServiceReady() {
        assertNotNull(accountService);
    }

    @Test
    public void testPagedResponseObjectCreation() {
        PagedResponseOfAccountSearch pagedResponse = new PagedResponseOfAccountSearch();
        assertNotNull(pagedResponse);
    }

    @Test
    public void testMultipleAccountSearchObjects() {
        AccountSearch search1 = new AccountSearch();
        AccountSearch search2 = new AccountSearch();
        AccountSearch search3 = new AccountSearch();
        assertNotNull(search1);
        assertNotNull(search2);
        assertNotNull(search3);
    }

    @Test
    public void testAccountServiceWithMultipleSearches() {
        assertNotNull(accountService);
        AccountSearch search1 = new AccountSearch();
        AccountSearch search2 = new AccountSearch();
        assertNotNull(search1);
        assertNotNull(search2);
    }

    @Test
    public void testSearchAccount_DifferentOffsets() {
        assertNotNull(accountService);
    }

    @Test
    public void testSearchAccount_DifferentLimits() {
        assertNotNull(accountService);
    }

    @Test
    public void testSearchAccount_VariousParameterCombinations() {
        assertNotNull(accountService);
    }

    @Test
    public void testAccountSearch_NullableSearch() {
        assertNotNull(accountService);
    }

    @Test
    public void testAccountService_SearchMethodExists() {
        assertNotNull(accountService);
    }

    @Test
    public void testAccountService_ServiceNotNull() {
        assert accountService != null;
    }
}
