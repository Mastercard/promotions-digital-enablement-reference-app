package com.mastercard.developer.validator;

import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class TransactionValidator {

    static Map<String, SimpleDateFormat> sdfMap = new HashMap();

    public void validateTransactionRequest(String accountId) {
        if (!StringUtils.hasText(accountId)) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, "account_id is missing");
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

    private LocalDateTime fromISOToLocalDateTime(String isoDateTime) {
        return isNotBlank(isoDateTime) ?
                ZonedDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME)
                        .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime() : null;
    }

}
