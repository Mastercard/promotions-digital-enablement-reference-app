package com.mastercard.developer.validator;

import com.mastercard.developer.exception.InvalidRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class EventValidatorTest {

    @InjectMocks
    private EventValidator validator;

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_MissingBothHouseholdAndAccountId() {
        validator.validateEvents(null, null, UUID.randomUUID().toString());
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_MissingBothHouseholdAndAccountId_EmptyStrings() {
        validator.validateEvents("", "", UUID.randomUUID().toString());
    }

    @Test
    public void testValidateEvents_WithValidHouseholdId() {
        String householdId = UUID.randomUUID().toString();
        String promotionId = UUID.randomUUID().toString();
        validator.validateEvents(householdId, null, promotionId);
    }

    @Test
    public void testValidateEvents_WithValidAccountId() {
        String accountId = UUID.randomUUID().toString();
        String promotionId = UUID.randomUUID().toString();
        validator.validateEvents(null, accountId, promotionId);
    }

    @Test
    public void testValidateEvents_WithBothValidIds() {
        String householdId = UUID.randomUUID().toString();
        String accountId = UUID.randomUUID().toString();
        String promotionId = UUID.randomUUID().toString();
        validator.validateEvents(householdId, accountId, promotionId);
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_InvalidHouseholdIdFormat() {
        String accountId = UUID.randomUUID().toString();
        validator.validateEvents("invalid-uuid", accountId, null);
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_InvalidAccountIdFormat() {
        String householdId = UUID.randomUUID().toString();
        validator.validateEvents(householdId, "invalid-uuid", null);
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_InvalidPromotionIdFormat() {
        String householdId = UUID.randomUUID().toString();
        validator.validateEvents(householdId, null, "invalid-uuid");
    }

    @Test
    public void testValidateEvents_WithValidUUIDs() {
        String householdId = UUID.randomUUID().toString();
        String accountId = UUID.randomUUID().toString();
        String promotionId = UUID.randomUUID().toString();
        validator.validateEvents(householdId, accountId, promotionId);
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_WithOnlyHouseholdId_InvalidFormat() {
        validator.validateEvents("not-a-uuid", null, null);
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_WithOnlyAccountId_InvalidFormat() {
        validator.validateEvents(null, "not-a-uuid", null);
    }

    @Test
    public void testValidateEvents_WithValidHouseholdId_NoPromotion() {
        String householdId = UUID.randomUUID().toString();
        validator.validateEvents(householdId, null, null);
    }

    @Test
    public void testValidateEvents_WithValidAccountId_NoPromotion() {
        String accountId = UUID.randomUUID().toString();
        validator.validateEvents(null, accountId, null);
    }

    @Test
    public void testCheckUUIDParameter_Valid() {
        String validUUID = UUID.randomUUID().toString();
        EventValidator.checkUUIDParameter(validUUID, "ERROR", "Invalid UUID");
    }

    @Test(expected = InvalidRequest.class)
    public void testCheckUUIDParameter_Invalid() {
        EventValidator.checkUUIDParameter("invalid-uuid", "ERROR", "Invalid UUID");
    }

    @Test(expected = InvalidRequest.class)
    public void testCheckUUIDParameter_Empty() {
        EventValidator.checkUUIDParameter("", "ERROR", "Invalid UUID");
    }

    @Test(expected = NullPointerException.class)
    public void testCheckUUIDParameter_Null() {
        EventValidator.checkUUIDParameter(null, "ERROR", "Invalid UUID");
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_PartiallyInvalidUUIDs() {
        String validHouseholdId = UUID.randomUUID().toString();
        validator.validateEvents(validHouseholdId, "invalid-account", UUID.randomUUID().toString());
    }

    @Test
    public void testValidateEvents_WithEmptyHouseholdId_ValidAccount() {
        String accountId = UUID.randomUUID().toString();
        validator.validateEvents("", accountId, null);
    }

    @Test
    public void testValidateEvents_WithEmptyAccountId_ValidHousehold() {
        String householdId = UUID.randomUUID().toString();
        validator.validateEvents(householdId, "", null);
    }
}
