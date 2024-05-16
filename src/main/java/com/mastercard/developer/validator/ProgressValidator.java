package com.mastercard.developer.validator;


import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProgressValidator {

    public void validateGetProgress(UUID householdId, UUID accountId) {
        List<String> messages = new ArrayList<>();
        if (null == householdId && null == accountId) {
            messages.add("household_id or account_id is missing");
        }
        if (!messages.isEmpty()) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, messages.toString());
        }
    }
}
