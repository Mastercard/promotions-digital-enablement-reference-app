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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TransactionValidator {

    static Map<String, SimpleDateFormat> sdfMap = new HashMap();

    public void validateTransactionRequest(UUID accountId) {
        if (null == accountId) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, "account_id is missing");
        }

    }

    public void validateTransactionDates(LocalDate fromDate, LocalDate toDate, int maxDuration) {
        if (fromDate == null && toDate == null) {
            return;
        }

        if (fromDate == null) {
            fromDate = getFromDate(fromDate, maxDuration);
        }

        if (fromDate != null && toDate != null && toDate.compareTo(fromDate) < 0) {
            throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(), "Invalid Date Range");
        }

        if (fromDate != null && toDate != null && toDate.isBefore(fromDate)) {
            throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(), "Invalid Date Range");
        }
    }

    public void validatePaginationParams(int offset, int limit) {
        if (offset < 0 || limit < 1) {
            throw new InvalidRequest(HttpStatus.BAD_REQUEST.toString(),
                    "Invalid Offset/limit. Offset index must not be less than zero. \" + \"Limit must not be less than one");
        }
    }

    public static LocalDate getFromDate(LocalDate fromDate, int maxDuration) {

        fromDate = LocalDate.now().minusMonths(maxDuration);

        return fromDate;
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
