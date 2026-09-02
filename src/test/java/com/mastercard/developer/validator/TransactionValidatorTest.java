package com.mastercard.developer.validator;

import com.mastercard.developer.exception.InvalidRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class TransactionValidatorTest {

    @InjectMocks
    private TransactionValidator validator;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateGetTransactionPromotionsByAccountIdNull() throws Exception {
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("account_id is missing");
        validator.validateTransactionRequest(null);
    }

    @Test
    public void testValidateGetForNotEmptyAccountId() throws Exception {
        validator.validateTransactionRequest("935e51fe-bc77-432f-b412-3800e3c04e90");
    }

    // Status validation tests

    @Test
    public void testValidateStatusCleared() {
        validator.validateStatus("cleared");
    }

    @Test
    public void testValidateStatusAuth() {
        validator.validateStatus("auth");
    }

    @Test
    public void testValidateStatusCombined() {
        validator.validateStatus("auth,cleared");
    }

    @Test
    public void testValidateStatusNull() {
        validator.validateStatus(null);
    }

    @Test
    public void testValidateStatusEmpty() {
        validator.validateStatus("");
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateStatusInvalid() {
        validator.validateStatus("invalid");
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateStatusTooManyValues() {
        validator.validateStatus("auth,cleared,pending");
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateStatusDuplicate() {
        validator.validateStatus("auth,auth");
    }

    // Expand validation tests

    @Test
    public void testValidateExpandAuthTransactions() {
        validator.validateExpand("authTransactions");
    }

    @Test
    public void testValidateExpandNull() {
        validator.validateExpand(null);
    }

    @Test
    public void testValidateExpandEmpty() {
        validator.validateExpand("");
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateExpandInvalid() {
        validator.validateExpand("invalidExpand");
    }

    // Existing date and pagination tests

    @Test
    public void testValidatorSuccess() {
        validator.validateTransactionDates("2020-05-01", "2020-05-29", 13);
    }

    @Test
    public void testValidatorToDateLessThanFromDate() {
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("Invalid Date Range");
        validator.validateTransactionDates("2020-05-20", "2020-03-20", 13);
    }

    @Test
    public void testValidatorInvalidDateFormat() {
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("Invalid Date Format. Acceptable Date format is YYYY-MM-DD");
        validator.validateTransactionDates("02-20-2020", "03-20-2020", 13);
    }

    @Test
    public void testValidatorToDateLessThanFromDateIfFromDateIsNull() {
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("Invalid Date Range");
        validator.validateTransactionDates(null, "2020-03-16", 13);
    }

    @Test(expected = InvalidRequest.class)
    public void testValidatorParse() {
        validator.validateTransactionDates("", "2020-03-20", 13);
    }

    @Test()
    public void testValidatorBothEmpty() {
        validator.validateTransactionDates(null, null, 0);
    }

}
