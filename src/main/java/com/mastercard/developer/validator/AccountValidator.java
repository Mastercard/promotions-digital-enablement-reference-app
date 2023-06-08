package com.mastercard.developer.validator;

import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import org.openapitools.client.model.AccountSearch;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mastercard.developer.constants.ApplicationConstants.ACCOUNT_NUMBER_TYPE;
import static com.mastercard.developer.constants.ApplicationConstants.COMMA;
import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.ACCOUNT_NUMBER_REQUIRED;
import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.COMPANY_ID_OR_MEMBER_ICA_REQUIRED;
import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.INVALID_ACCOUNT_NUMBER_TYPE;
import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.INVALID_USER_NUMBER_TYPE;
import static com.mastercard.developer.constants.ApplicationConstants.ErrorMessages.USER_NUMBER_REQUIRED;
import static com.mastercard.developer.constants.ApplicationConstants.USER_NUMBER_TYPE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class AccountValidator {

    public void validateSearchAccount(AccountSearch accountSearch) {
        List<String> messages = new ArrayList<>();
        List<String> accountNumberType = Arrays.asList(ACCOUNT_NUMBER_TYPE.split(COMMA));
        List<String> userNumberType = Arrays.asList(USER_NUMBER_TYPE.split(COMMA));

        if (isNull(accountSearch.getMemberICA()) && isNull(accountSearch.getCompanyId())) {
            messages.add(COMPANY_ID_OR_MEMBER_ICA_REQUIRED);
        }
        if(isNotBlank(accountSearch.getAccountNumberType()) && isBlank(accountSearch.getAccountNumber())) {
            messages.add(ACCOUNT_NUMBER_REQUIRED);
        }
        if(isNotBlank(accountSearch.getUserNumberType()) && isBlank(accountSearch.getUserNumber())) {
            messages.add(USER_NUMBER_REQUIRED);
        }
        if (isBlank(accountSearch.getAccountNumberType()) && isBlank(accountSearch.getAccountNumber()) &&
                isBlank(accountSearch.getUserNumberType()) && isBlank(accountSearch.getUserNumber())) {
            messages.add(ACCOUNT_NUMBER_REQUIRED);
        }
        if (nonNull(accountSearch.getAccountNumber())
                && nonNull(accountSearch.getAccountNumberType())
                && !accountNumberType.contains(accountSearch.getAccountNumberType())) {
            String message = String.format(INVALID_ACCOUNT_NUMBER_TYPE, accountSearch.getAccountNumberType());
            messages.add(message);
        }
        if (nonNull(accountSearch.getUserNumber())
                && nonNull(accountSearch.getUserNumberType())
                && !userNumberType.contains(accountSearch.getUserNumberType())) {
            String message = String.format(INVALID_USER_NUMBER_TYPE, accountSearch.getUserNumberType());
            messages.add(message);
        }
        if (!messages.isEmpty()) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, messages.toString());
        }
    }
}
