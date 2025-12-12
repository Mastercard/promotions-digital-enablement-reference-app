package com.mastercard.developer.validator;

import com.mastercard.developer.exception.InvalidRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * Parameterized test for invalid UUID format validation.
 * Tests all three parameter types (householdId, accountId, promotionId) with invalid UUIDs.
 */
@RunWith(Parameterized.class)
public class InvalidUUIDFormatParameterizedTest {

    @InjectMocks
    private EventValidator validator = new EventValidator();

    private final String householdId;
    private final String accountId;
    private final String promotionId;

    public InvalidUUIDFormatParameterizedTest(String householdId, String accountId, String promotionId, String testDescription) {
        this.householdId = householdId;
        this.accountId = accountId;
        this.promotionId = promotionId;
    }

    @Parameterized.Parameters(name = "{3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "invalid-uuid", UUID.randomUUID().toString(), null, "InvalidHouseholdIdFormat" },
                { UUID.randomUUID().toString(), "invalid-uuid", null, "InvalidAccountIdFormat" },
                { UUID.randomUUID().toString(), null, "invalid-uuid", "InvalidPromotionIdFormat" }
        });
    }

    @Test(expected = InvalidRequest.class)
    public void testValidateEvents_WithInvalidUUIDFormat() {
        validator.validateEvents(householdId, accountId, promotionId);
    }
}
