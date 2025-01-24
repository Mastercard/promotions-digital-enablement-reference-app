package com.mastercard.developer.validator;

import com.mastercard.developer.constants.ApplicationConstants;
import com.mastercard.developer.exception.InvalidRequest;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.client.model.Audience;
import org.openapitools.client.model.AudienceUpdate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class AudienceValidator {
    private static final String ACCOUNT = "A";
    private static final String HOUSEHOLD = "H";
    private static final String UTC = "UTC";

    private static void validateRequestFields(Audience audience, Map<String, String> errorMap) {
        if (isBlank(audience.getCode())) {
            errorMap.put(
                    ApplicationConstants.INVALID_FIELD_EXTERNAL_TARGET_CODE,
                    ApplicationConstants.INVALID_FIELD_EXTERNAL_TARGET_CODE_ERR_MSG);
        }
        if (isBlank(audience.getBeginDateTime())) {
            errorMap.put(
                    ApplicationConstants.INVALID_FIELD_BEGIN_DATE,
                    ApplicationConstants.INVALID_FIELD_BEGIN_DATE_ERR_MSG);
        }
        if (isBlank(audience.getEntityType())) {
            errorMap.put(
                    ApplicationConstants.INVALID_FIELD_ENTITY_TYPE,
                    ApplicationConstants.INVALID_FIELD_ENTITY_TYPE_ERR_MSG);
        }
        if (audience.getEntityType() != null && !ACCOUNT.equalsIgnoreCase(audience.getEntityType())
                && !HOUSEHOLD.equalsIgnoreCase(audience.getEntityType())) {
            errorMap.put(
                    ApplicationConstants.INVALID_FIELD_ENTITY_TYPE_VALUE,
                    ApplicationConstants.INVALID_FIELD_ENTITY_TYPE_VALUE_ERR_MSG);
        }
        if (isBlank(audience.getEntityId())) {
            errorMap.put(
                    ApplicationConstants.INVALID_FIELD_ENTITY_REFERENCE_ID,
                    ApplicationConstants.INVALID_FIELD_ENTITY_REFERENCE_ID_ERR_MSG);
        }
    }

    private void validateDateFields(String beginDateTime, String endDateTime, Map<String, String> errorMap) {
        checkForBeginDateTimeFormat(beginDateTime, errorMap);
        checkForEndDateTimeFormat(endDateTime, errorMap);
        validateDateRange(beginDateTime, endDateTime, errorMap);
    }

    private static void validateDateRange(String beginDateTime, String endDateTime, Map<String, String> errorMap) {
        if (errorMap.isEmpty() && !isBlank(beginDateTime) && !isBlank(endDateTime)) {
            LocalDateTime beginDate = getDateValue(beginDateTime);
            LocalDateTime endDate = getDateValue(endDateTime);
            if (beginDate != null && endDate != null && beginDate.isAfter(endDate)) {
                errorMap.put(ApplicationConstants.END_DATE_SHOULD_BE_AFTER_BEGIN_DATE,
                        ApplicationConstants.END_DATE_SHOULD_BE_AFTER_BEGIN_DATE_ERR_MSG);
            }
        }
    }

    private static LocalDateTime getDateValue(String date) {
        DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of(UTC)))
                .appendOptional(DateTimeFormatter.ISO_DATE.withZone(ZoneId.of(UTC)))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-M-d").withZone(ZoneId.of(UTC)))
                .appendOptional(DateTimeFormatter.ofPattern("M/d/yyyy").withZone(ZoneId.of(UTC)))
                .appendOptional(DateTimeFormatter.ofPattern("M/d/yy").withZone(ZoneId.of(UTC)))
                .appendOptional(DateTimeFormatter.ofPattern("M-d-yyyy").withZone(ZoneId.of(UTC)))
                .appendOptional(DateTimeFormatter.ofPattern("d-MMM-yyyy").withZone(ZoneId.of(UTC)))
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter().withZone(ZoneId.of(UTC)) ;
        return StringUtils.isNotBlank(date) ? LocalDateTime.parse(date, dtf).atZone(ZoneId.of(UTC)).toLocalDateTime() : null;
    }

    public void validateAudienceCreate(Audience audience) {
        Map<String, String> errorMap = new HashMap<>();
        validateRequestFields(audience, errorMap);
        validateDateFields(audience.getBeginDateTime(), audience.getEndDateTime(), errorMap);
        collectAndThrowErrors(errorMap);
    }

    public void validateAudienceGetDataRequest(String fromDateTime, String toDateTime, String entityType, String entityId) {
        Map<String, String> errorMap = new HashMap<>();
        if (isBlank(entityType)) {
            errorMap.put(
                    ApplicationConstants.INVALID_FIELD_ENTITY_TYPE,
                    ApplicationConstants.INVALID_FIELD_ENTITY_TYPE_ERR_MSG);
        }
        if (isBlank(entityId)) {
            errorMap.put(
                    ApplicationConstants.INVALID_FIELD_ENTITY_REFERENCE_ID,
                    ApplicationConstants.INVALID_FIELD_ENTITY_REFERENCE_ID_ERR_MSG);
        }
        validateDateFields(fromDateTime, toDateTime, errorMap);
        collectAndThrowErrors(errorMap);
    }

    public void validateAudienceUpdateData(AudienceUpdate audienceUpdate) {
        Map<String, String> errorMap = new HashMap<>();
        validateDateFields(audienceUpdate.getBeginDateTime(), audienceUpdate.getEndDateTime(), errorMap);
        collectAndThrowErrors(errorMap);
    }

    private void checkForBeginDateTimeFormat(String beginDateTime, Map<String, String> errorMap) {
        if (!isBlank(beginDateTime)) {
            try {
                fromISOToLocalDateTime(beginDateTime);
            } catch (Exception e) {
                errorMap.put(
                        ApplicationConstants.INVALID_BEGIN_DATE_TIME_FORMAT,
                        ApplicationConstants.INVALID_BEGIN_DATE_TIME_FORMAT_ERR_MSG);
            }
        }
    }

    private void checkForEndDateTimeFormat(String endDateTime, Map<String, String> errorMap) {
        if (!isBlank(endDateTime)) {
            try {
                fromISOToLocalDateTime(endDateTime);
            } catch (Exception e) {
                errorMap.put(
                        ApplicationConstants.INVALID_END_DATE_TIME_FORMAT,
                        ApplicationConstants.INVALID_END_DATE_TIME_FORMAT_ERR_MSG);
            }
        }
    }

    private LocalDateTime fromISOToLocalDateTime(String isoDateTime) {
        return isNotBlank(isoDateTime) ?
                ZonedDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME)
                        .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime() : null;
    }

    private void collectAndThrowErrors(Map<String, String> errorMap) {
        if (!errorMap.isEmpty()) {
            final String[] mapKey = new String[1];
            errorMap.forEach((key, value) -> mapKey[0] = key);
            throw new InvalidRequest(mapKey[0], errorMap.get(mapKey[0]));
        }
    }
}