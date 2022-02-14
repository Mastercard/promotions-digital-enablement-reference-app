package com.mastercard.developer.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ErrorMessages {
        public static final String ACCOUNT_NUMBER_REQUIRED = "Account Number is required.";
        public static final String COMPANY_ID_OR_MEMBER_ICA_REQUIRED = "Either Company ID or Member ICA is required.";
    }
}
