package com.mastercard.developer.controller;

import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.AccountService;
import com.mastercard.developer.validator.AccountValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.AccountSearch;
import org.openapitools.client.model.AccountSearchItem;
import org.openapitools.client.model.PagedResponseOfAccountSearch;

import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.ACCOUNT_NUMBER_REQUIRED;
import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.COMPANY_ID_OR_MEMBER_ICA_REQUIRED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @InjectMocks
    AccountController controller;

    @Mock
    AccountService accountService;

    @Mock
    AccountValidator accountValidator;

    private static final Long ICA = 123456L;
    private static final String COMPANY_ID = "123456";
    private static final String PROGRAM_ENROLLMENT_CODE = "ZXSzM";
    private static final String ACCOUNT_NUMBER = "5330333671236516";

    @Test
    public void testAccountsSearches() throws ApiException {
        AccountSearch searchRequest = getSearchRequest();
        PagedResponseOfAccountSearch searchOutDto = getPagedResponseOfAccountSearch();
        when(accountService.searchAccount(anyInt(), anyInt(), any(AccountSearch.class))).thenReturn(searchOutDto);

        PagedResponseOfAccountSearch response = controller.searchAccount(0, 25, searchRequest);

        assertNotNull(response);
        assertEquals(1, response.getCount().intValue());
    }

    @Test
    public void testAccountsSearchesWithProgramEnrollmentAndMemberIcaIsNull() throws ApiException {
        AccountSearch searchRequest = getSearchRequest();
        searchRequest.setMemberICA(null);
        searchRequest.setProgramEnrollmentCode(null);
        PagedResponseOfAccountSearch searchOutDto = getPagedResponseOfAccountSearch();
        when(accountService.searchAccount(0, 25, searchRequest)).thenReturn(searchOutDto);

        PagedResponseOfAccountSearch response = controller.searchAccount(0, 25, searchRequest);

        assertNotNull(response);
        assertEquals(1, response.getCount().intValue());
    }

    @Test
    public void testAccountsSearchesWithCompanyIdIsNull() {
        AccountSearch searchRequest = getSearchRequest();
        searchRequest.setCompanyId(null);
        searchRequest.setMemberICA(null);

        doThrow(new InvalidRequest(ErrorCodes.INVALID_INPUT.code, COMPANY_ID_OR_MEMBER_ICA_REQUIRED)).when(accountValidator).validateSearchAccount(any(AccountSearch.class));

        try {
            controller.searchAccount(0, 25, searchRequest);
        } catch (InvalidRequest ex) {
            assertEquals(COMPANY_ID_OR_MEMBER_ICA_REQUIRED, ex.getMessage());
        }
    }

    @Test
    public void testAccountsSearchesWithAccountNumberIsNull() {
        AccountSearch searchRequest = getSearchRequest();
        searchRequest.setAccountNumber(null);
        doThrow(new InvalidRequest(ErrorCodes.INVALID_INPUT.code, ACCOUNT_NUMBER_REQUIRED)).when(accountValidator).validateSearchAccount(searchRequest);

        try {
            controller.searchAccount(0, 25, searchRequest);
        } catch (InvalidRequest ex) {
            assertEquals(ACCOUNT_NUMBER_REQUIRED, ex.getMessage());
        }
    }

    private AccountSearch getSearchRequest() {
        AccountSearch searchRequest = new AccountSearch();
        searchRequest.setCompanyId(COMPANY_ID);
        searchRequest.setAccountNumber(ACCOUNT_NUMBER);
        searchRequest.setMemberICA(ICA);
        searchRequest.setProgramEnrollmentCode(PROGRAM_ENROLLMENT_CODE);
        return searchRequest;
    }

    private PagedResponseOfAccountSearch getPagedResponseOfAccountSearch() {
        PagedResponseOfAccountSearch searchResponse = new PagedResponseOfAccountSearch();
        searchResponse.setCount(1);
        searchResponse.addItemsItem(new AccountSearchItem());
        return searchResponse;
    }
}
