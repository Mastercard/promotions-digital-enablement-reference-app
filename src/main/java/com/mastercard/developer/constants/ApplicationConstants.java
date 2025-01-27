package com.mastercard.developer.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {

    public static final String ACCOUNT_NUMBER_TYPE = "BAN,RANAC,AREF";
    public static final String USER_NUMBER_TYPE = "BCN,RANCU,AREF";
    public static final String COMMA = ",";

    public static final String INVALID_FIELD_EXTERNAL_TARGET_CODE = "INVALID_FIELD_EXTERNAL_TARGET_CODE";
    public static final String INVALID_FIELD_EXTERNAL_TARGET_CODE_ERR_MSG = "External Target Code cannot be null";
    public static final String INVALID_FIELD_BEGIN_DATE = "INVALID_FIELD_BEGIN_DATE";
    public static final String INVALID_FIELD_BEGIN_DATE_ERR_MSG = "External Target Record Begin Date cannot be null";
    public static final String INVALID_FIELD_ENTITY_TYPE = "INVALID_FIELD_ENTITY_TYPE";
    public static final String INVALID_FIELD_ENTITY_TYPE_ERR_MSG = "External Target Record Entity Type cannot be null";
    public static final String INVALID_FIELD_ENTITY_REFERENCE_ID = "INVALID_FIELD_ENTITY_REFERENCE_ID";
    public static final String INVALID_FIELD_ENTITY_REFERENCE_ID_ERR_MSG = "External Target Record Entity Reference Id cannot be null";
    public static final String INVALID_FIELD_ENTITY_TYPE_VALUE = "INVALID_FIELD_ENTITY_TYPE_VALUE";
    public static final String INVALID_FIELD_ENTITY_TYPE_VALUE_ERR_MSG = "External Target Record Entity Type must be either A or H";
    public static final String END_DATE_SHOULD_BE_AFTER_BEGIN_DATE = "END_DATE_SHOULD_BE_AFTER_BEGIN_DATE";
    public static final String END_DATE_SHOULD_BE_AFTER_BEGIN_DATE_ERR_MSG = "The End Date Time should be after the Begin Date Time";
    public static final String INVALID_BEGIN_DATE_TIME_FORMAT = "INVALID_BEGIN_DATE_TIME_FORMAT";
    public static final String INVALID_BEGIN_DATE_TIME_FORMAT_ERR_MSG = "Invalid Begin Date Time Format";
    public static final String INVALID_END_DATE_TIME_FORMAT = "INVALID_END_DATE_TIME_FORMAT";
    public static final String INVALID_END_DATE_TIME_FORMAT_ERR_MSG = "Invalid End Date Time Format";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ErrorMessages {
        public static final String ACCOUNT_NUMBER_REQUIRED = "Account Number is required.";

        public static final String USER_NUMBER_REQUIRED = "User Number is required.";
        public static final String COMPANY_ID_OR_MEMBER_ICA_REQUIRED = "Either Company ID or Member ICA is required.";

        public static final String INVALID_ACCOUNT_NUMBER_TYPE = "No matching accountNumberType found for [ %s ]";

        public static final String INVALID_USER_NUMBER_TYPE = "No matching userNumberType found for [ %s ]";
    }
}
