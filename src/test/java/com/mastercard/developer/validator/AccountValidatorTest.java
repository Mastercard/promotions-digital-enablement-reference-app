package com.mastercard.developer.validator;

import com.mastercard.developer.exception.InvalidRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.model.AccountSearch;

import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.*;
import static java.lang.String.format;

@RunWith(MockitoJUnitRunner.class)
public class AccountValidatorTest {

    private static final String COMPANY_ID = "123456";
    private static final String ACCOUNT_NUMBER = "5330333671236516";
    private static final String USER_NUMBER = "533033367";
    private static final String BAN = "BAN";
    private static final String BCN = "BCN";
    private static final String INVALID_BAN = "BAN1";
    private static final String INVALID_BCN = "BCN1";

    @InjectMocks
    private AccountValidator validator;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateSearchAccountByAccountNumberAndUserNumberNull() {
        AccountSearch accountSearch = new AccountSearch();
        accountSearch.setCompanyId(COMPANY_ID);
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage(ACCOUNT_NUMBER_REQUIRED);
        validator.validateSearchAccount(accountSearch);
    }

    @Test
    public void testValidateGetPromotionsByCompanyIdIdNull() {
        AccountSearch accountSearch = new AccountSearch();
        accountSearch.setAccountNumber(ACCOUNT_NUMBER);
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage(COMPANY_ID_OR_MEMBER_ICA_REQUIRED);
        validator.validateSearchAccount(accountSearch);
    }

    @Test
    public void testValidateSearchAccountByAccountNumberNull() {
        AccountSearch accountSearch = new AccountSearch();
        accountSearch.setCompanyId(COMPANY_ID);
        accountSearch.setAccountNumberType(BAN);
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage(ACCOUNT_NUMBER_REQUIRED);
        validator.validateSearchAccount(accountSearch);
    }

    @Test
    public void testValidateSearchAccountByUserNumberNull() {
        AccountSearch accountSearch = new AccountSearch();
        accountSearch.setCompanyId(COMPANY_ID);
        accountSearch.setUserNumberType(BCN);
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage(USER_NUMBER_REQUIRED);
        validator.validateSearchAccount(accountSearch);
    }

    @Test
    public void testValidateSearchWithInvalidAccountNumberType() {
        AccountSearch accountSearch = new AccountSearch();
        accountSearch.setCompanyId(COMPANY_ID);
        accountSearch.setAccountNumberType(INVALID_BAN);
        accountSearch.setAccountNumber(ACCOUNT_NUMBER);
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage(format(INVALID_ACCOUNT_NUMBER_TYPE, INVALID_BAN));
        validator.validateSearchAccount(accountSearch);
    }

    @Test
    public void testValidateSearchWithInvalidUserNumberType() {
        AccountSearch accountSearch = new AccountSearch();
        accountSearch.setCompanyId(COMPANY_ID);
        accountSearch.setUserNumberType(INVALID_BCN);
        accountSearch.setUserNumber(USER_NUMBER);
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage(format(INVALID_USER_NUMBER_TYPE, INVALID_BCN));
        validator.validateSearchAccount(accountSearch);
    }
}
