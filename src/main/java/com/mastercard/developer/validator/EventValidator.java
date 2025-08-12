package com.mastercard.developer.validator;

import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventValidator {

    private static final String MISSING_REQUIRED_FIELDS = "Either householdId or accountId is required.";
    private static final String INVALID_HOUSEHOLD_ID = "householdId must be a valid UUID.";
    private static final String INVALID_ACCOUNT_ID = "accountId must be a valid UUID.";
    private static final String INVALID_PROMOTION_ID = "promotionId must be a valid UUID.";

    public void validateEvents(String householdId, String accountId, String promotionId) {
        if (StringUtils.isEmpty(accountId) && StringUtils.isEmpty(householdId)) {
            throw new InvalidRequest(ErrorCodes.VALIDATION_ERROR.name(), MISSING_REQUIRED_FIELDS);
        }
        if (!StringUtils.isEmpty(householdId)) {
            checkUUIDParameter(householdId, ErrorCodes.INVALID_INPUT.name(), INVALID_HOUSEHOLD_ID);
        }
        if (!StringUtils.isEmpty(accountId)) {
            checkUUIDParameter(accountId, ErrorCodes.INVALID_INPUT.name(), INVALID_ACCOUNT_ID);
        }
        if (!StringUtils.isEmpty(promotionId)) {
            checkUUIDParameter(promotionId, ErrorCodes.INVALID_INPUT.name(), INVALID_PROMOTION_ID);
        }
    }

    public static void checkUUIDParameter(String param, String err, String mes) {
        try {
            UUID.fromString(param);
        } catch(IllegalArgumentException ex) {
            throw new InvalidRequest(String.valueOf(err), mes);
        }
    }

}
