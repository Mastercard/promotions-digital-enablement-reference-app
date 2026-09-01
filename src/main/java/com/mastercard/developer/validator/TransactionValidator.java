package com.mastercard.developer.validator;

import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TransactionValidator {

    private static final Set<String> VALID_STATUS_VALUES = new HashSet<>(Arrays.asList("cleared", "auth"));
    private static final String VALID_EXPAND_VALUE = "AUTH_TRANSACTIONS";
    private static final int MAX_STATUS_TOKENS = 2;

    static Map<String, SimpleDateFormat> sdfMap = new HashMap();

    public void validateTransactionRequest(final String accountId) {
        if (!StringUtils.hasText(accountId)) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, "account_id is missing");
        }
    }

    public void validateStatus(final String status) {
        if (!StringUtils.hasText(status)) {
            return;
        }

        // Split and normalize
        final List<String> tokens = Arrays.stream(status.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        if (tokens.size() > MAX_STATUS_TOKENS) {
            throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(),
                    "Invalid status. Maximum of 2 status values allowed");
        }

        // Validate each token
        tokens.stream()
                .filter(token -> !VALID_STATUS_VALUES.contains(token))
                .findFirst()
                .ifPresent(invalid -> {
                    throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(),
                            "Invalid status value: " + invalid + ". Allowed values are: cleared, auth");
                });

        // Check for duplicates
        if (new HashSet<>(tokens).size() != tokens.size()) {
            throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(),
                    "Duplicate status values are not allowed");
        }
    }

    public void validateExpand(final String expand) {
        if (!StringUtils.hasText(expand)) {
            return;
        }

        if (!VALID_EXPAND_VALUE.equalsIgnoreCase(expand)) {
            throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(),
                    "Invalid expand value: " + expand + ". Allowed value is: AUTH_TRANSACTIONS");
        }
    }

    public void validateTransactionDates(String fromDateAsString, String toDateAsString, int maxDuration) {
        if (fromDateAsString == null && toDateAsString == null) {
            return;
        }
        try {
            Date fromDate = convertStringToDate("yyyy-MM-dd", fromDateAsString);
            Date toDate = convertStringToDate("yyyy-MM-dd", toDateAsString);

            if (fromDate == null) {
                fromDate = getFromDate(fromDateAsString, maxDuration);
            }

            if (fromDate != null && toDate != null && toDate.compareTo(fromDate) < 0) {
                throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(), "Invalid Date Range");
            }
        } catch (ParseException ex) {
            throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(),
                    "Invalid Date Format. Acceptable Date format is YYYY-MM-DD");
        }
    }

    public void validatePaginationParams(int offset, int limit) {
        if (offset < 0 || limit < 1) {
            throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(),
                    "Invalid Offset/limit. Offset index must not be less than zero. \" + \"Limit must not be less than one");
        }
    }

    public static Date getFromDate(String fromDateAsString, int maxDuration) {
        Date fromDate = null;

        try {
            fromDate = convertStringToDate("yyyy-MM-dd", fromDateAsString);
            if (fromDate == null || fromDate.compareTo(Date.from(LocalDate.now().minusMonths((long) maxDuration).atStartOfDay(ZoneId.systemDefault()).toInstant())) < 0) {
                fromDate = Date.from(LocalDate.now().minusMonths((long) maxDuration).atStartOfDay(ZoneId.systemDefault()).toInstant());
            }

            return fromDate;
        } catch (ParseException var4) {
            return null;
        }
    }

    public static Date convertStringToDate(String dateFormat, String date) throws ParseException {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat simpleDateFormat = getSimpleDateFormat(dateFormat);
            return simpleDateFormat.parse(date.replace("Z", "+0000"));
        }
    }

    private static SimpleDateFormat getSimpleDateFormat(String dateFormat) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) sdfMap.get(dateFormat);
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(dateFormat);
            simpleDateFormat.setLenient(false);
            sdfMap.put(dateFormat, simpleDateFormat);
        }

        return simpleDateFormat;
    }

}
