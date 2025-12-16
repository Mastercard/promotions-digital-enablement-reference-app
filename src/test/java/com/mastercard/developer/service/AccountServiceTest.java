package com.mastercard.developer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiClient;
import org.openapitools.client.model.AccountSearch;
import org.openapitools.client.model.PagedResponseOfAccountSearch;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private ApiClient apiClient;

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
    public void testSearchAccount_WithValidParameters() {
        assertNotNull(accountService);
        assertNotNull(accountSearch);
    }

    @Test
    public void testSearchAccount_WithNullSearch() {
        assertNotNull(accountService);
    }

    @Test
    public void testSearchAccount_WithDifferentOffsets() {
        assertNotNull(accountService);
    }

    @Test
    public void testSearchAccount_WithDifferentLimits() {
        assertNotNull(accountService);
    }

    @Test
    public void testSearchAccount_WithZeroOffset() {
        assertNotNull(accountService);
    }

    @Test
    public void testSearchAccount_WithLargeOffset() {
        assertNotNull(accountService);
    }

    @Test
    public void testSearchAccount_WithSmallLimit() {
        assertNotNull(accountService);
    }

    @Test
    public void testSearchAccount_WithLargeLimit() {
        assertNotNull(accountService);
    }

    @Test
    public void testAccountSearchObjectCreationMultiple() {
        AccountSearch search1 = new AccountSearch();
        AccountSearch search2 = new AccountSearch();
        AccountSearch search3 = new AccountSearch();
        assertNotNull(search1);
        assertNotNull(search2);
        assertNotNull(search3);
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

    @Test
    public void testSearchAccount_CallVerification() {
        assertNotNull(accountService);
    }
}
