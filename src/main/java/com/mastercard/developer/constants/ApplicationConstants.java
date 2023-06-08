package com.mastercard.developer.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {

    public static final String ACCOUNT_NUMBER_TYPE = "BAN,RANAC,AREF";
    public static final String USER_NUMBER_TYPE = "BCN,RANCU,AREF";
    public static final String COMMA = ",";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ErrorMessages {
        public static final String ACCOUNT_NUMBER_REQUIRED = "Account Number is required.";

        public static final String USER_NUMBER_REQUIRED = "User Number is required.";
        public static final String COMPANY_ID_OR_MEMBER_ICA_REQUIRED = "Either Company ID or Member ICA is required.";

        public static final String INVALID_ACCOUNT_NUMBER_TYPE = "No matching accountNumberType found for [ %s ]";

        public static final String INVALID_USER_NUMBER_TYPE = "No matching userNumberType found for [ %s ]";
    }
}
