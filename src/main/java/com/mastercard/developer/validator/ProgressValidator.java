package com.mastercard.developer.validator;


import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProgressValidator {

    public void validateGetProgress(String householdId, String accountId) {
        List<String> messages = new ArrayList<>();
        if (!StringUtils.hasText(householdId) && !StringUtils.hasText(accountId)) {
            messages.add("household_id or account_id is missing");
        }
        if (!messages.isEmpty()) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, messages.toString());
        }
    }
}
