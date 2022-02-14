package com.mastercard.developer.validator;

import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import org.openapitools.client.model.AccountSearch;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.ACCOUNT_NUMBER_REQUIRED;
import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.COMPANY_ID_OR_MEMBER_ICA_REQUIRED;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

@Component
public class AccountValidator {

    public void validateSearchAccount(AccountSearch accountSearch) {
        List<String> messages = new ArrayList<>();
        if (isNull(accountSearch.getMemberICA()) && isNull(accountSearch.getCompanyId())) {
            messages.add(COMPANY_ID_OR_MEMBER_ICA_REQUIRED);
        }
        if (!hasText(accountSearch.getAccountNumber())) {
            messages.add(ACCOUNT_NUMBER_REQUIRED);
        }
        if (!messages.isEmpty()) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, messages.toString());
        }
    }
}
