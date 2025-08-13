package com.mastercard.developer.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {

    public static final String ACCOUNT_NUMBER_TYPE = "BAN,RANAC,AREF";
    public static final String USER_NUMBER_TYPE = "BCN,RANCU,AREF";
    public static final String COMMA = ",";

    public static final String INVALID_FIELD_AUDIENCE_CODE = "INVALID_FIELD_AUDIENCE_CODE";
    public static final String INVALID_FIELD_AUDIENCE_CODE_ERR_MSG = "Audience Code cannot be null";
    public static final String INVALID_FIELD_BEGIN_DATE = "INVALID_FIELD_BEGIN_DATE";
    public static final String INVALID_FIELD_BEGIN_DATE_ERR_MSG = "Begin Date cannot be null";
    public static final String INVALID_FIELD_ENTITY_TYPE = "INVALID_FIELD_ENTITY_TYPE";
    public static final String INVALID_FIELD_ENTITY_TYPE_ERR_MSG = "Entity Type cannot be null";
    public static final String INVALID_FIELD_ENTITY_REFERENCE_ID = "INVALID_FIELD_ENTITY_REFERENCE_ID";
    public static final String INVALID_FIELD_ENTITY_REFERENCE_ID_ERR_MSG = "Entity Reference Id cannot be null";
    public static final String INVALID_FIELD_ENTITY_TYPE_VALUE = "INVALID_FIELD_ENTITY_TYPE_VALUE";
    public static final String INVALID_FIELD_ENTITY_TYPE_VALUE_ERR_MSG = "Entity Type must be either A or H";
    public static final String END_DATE_SHOULD_BE_AFTER_BEGIN_DATE = "END_DATE_SHOULD_BE_AFTER_BEGIN_DATE";
    public static final String END_DATE_SHOULD_BE_AFTER_BEGIN_DATE_ERR_MSG = "The End Date Time should be after the Begin Date Time";
    public static final String INVALID_BEGIN_DATE_TIME_FORMAT = "INVALID_BEGIN_DATE_TIME_FORMAT";
    public static final String INVALID_BEGIN_DATE_TIME_FORMAT_ERR_MSG = "Invalid Begin Date Time Format";
    public static final String INVALID_END_DATE_TIME_FORMAT = "INVALID_END_DATE_TIME_FORMAT";
    public static final String INVALID_END_DATE_TIME_FORMAT_ERR_MSG = "Invalid End Date Time Format";
    public static final String EVENT_CREATED = "{\"success\": \"Event is successfully posted for scoring\"}";
    public static final String EVENT_HEADER_NAME = "ENTITY_DETAIL";
    public static final String ENTITY_ID_REQUIRED = "EntityId is required";
    public static final String ENTITY_TYPE_IS_REQUIRED = "EntityType is required";
    public static final String EVENT_TYPE_IS_REQUIRED = "EventType is required";
    public static final String EVENT_TOTAL_INVALID = "Invalid event total";
    public static final String EVENT_DATETIME_INVALID = "EventDateTime is required and should have valid date format";
    public static final String INVALID_PAYLOAD_RECEIVED_FOR = "Invalid payload : ";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";
    public static final String REWARDS_BANK_SOURCE = "REWARDS_BANK";


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ErrorMessages {
        public static final String ACCOUNT_NUMBER_REQUIRED = "Account Number is required.";

        public static final String USER_NUMBER_REQUIRED = "User Number is required.";
        public static final String COMPANY_ID_OR_MEMBER_ICA_REQUIRED = "Either Company ID or Member ICA is required.";

        public static final String INVALID_ACCOUNT_NUMBER_TYPE = "No matching accountNumberType found for [ %s ]";

        public static final String INVALID_USER_NUMBER_TYPE = "No matching userNumberType found for [ %s ]";
    }
}
