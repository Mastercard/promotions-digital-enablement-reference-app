package com.mastercard.developer.validator;

import com.mastercard.developer.constants.ApplicationConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.model.AudienceCreate;
import org.openapitools.client.model.AudienceUpdate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class AudienceValidatorTest {

    private static final LocalDateTime END_DATE = LocalDateTime.now().plusDays(5);
    private static final String END_DATE_TIME = END_DATE.atZone(ZoneOffset.UTC)
            .withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);

    @InjectMocks
    private AudienceValidator validator;

    @Test
    public void testValidateUpdateDataRequestForInvalidFormatOfBeginDate() {
        AudienceUpdate audienceUpdate = createAudienceUpdateObj();
        audienceUpdate.setBeginDateTime("2024-10-2108:08:08");
        audienceUpdate.setEndDateTime(END_DATE_TIME);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceUpdateData(audienceUpdate);
        });
        assertEquals(ApplicationConstants.INVALID_BEGIN_DATE_TIME_FORMAT_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidatePostCreateRequestForInvalidFormatOfBeginDate() {
        AudienceCreate audience = createAudienceObj();
        audience.setBeginDateTime("2024-10-21T08:08:08Z");
        audience.setEndDateTime("2024-10-12T08:08:08Z");
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals(ApplicationConstants.END_DATE_SHOULD_BE_AFTER_BEGIN_DATE_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForInvalidCode() {
        AudienceCreate audience = createAudienceObj();
        audience.setCode(null);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals(ApplicationConstants.INVALID_FIELD_AUDIENCE_CODE_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForInvalidBeginDate() {
        AudienceCreate audience = createAudienceObj();
        audience.setBeginDateTime(null);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals(ApplicationConstants.INVALID_FIELD_BEGIN_DATE_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForNullEntityType() {
        AudienceCreate audience = createAudienceObj();
        audience.setEntityType(null);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals(ApplicationConstants.INVALID_FIELD_ENTITY_TYPE_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForInvalidEntityType() {
        AudienceCreate audience = createAudienceObj();
        audience.setEntityType("S");
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals(ApplicationConstants.INVALID_FIELD_ENTITY_TYPE_VALUE_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForNullEntityId() {
        AudienceCreate audience = createAudienceObj();
        audience.setEntityId(null);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals(ApplicationConstants.INVALID_FIELD_ENTITY_REFERENCE_ID_ERR_MSG, exception.getMessage());
    }

    private AudienceCreate createAudienceObj() {
        AudienceCreate audience = new AudienceCreate();
        audience.setBeginDateTime("2024-10-12T08:08:08Z");
        audience.setCode("ABCODE");
        audience.setEntityId("EntityId");
        audience.setEntityType("A");
        audience.setEndDateTime("2024-10-21T08:08:08Z");
        return audience;
    }

    private AudienceUpdate createAudienceUpdateObj() {
        AudienceUpdate audienceUpdate = new AudienceUpdate();
        audienceUpdate.setBeginDateTime("2024-10-12T08:08:08Z");
        audienceUpdate.setEndDateTime("2024-10-21T08:08:08Z");
        return audienceUpdate;
    }

    @Test
    public void testValidateGetRequestForNullEntityId() {
        String fromDate = "2024-10-21T08:08:08Z";
        String entityId = null;
        String entityType = "H";
        String toDate = "2024-10-12T08:08:08Z";
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceGetDataRequest(fromDate, toDate, entityType, entityId);
        });
        assertEquals(ApplicationConstants.INVALID_FIELD_ENTITY_REFERENCE_ID_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateGetRequestForNullEntityType() {
        String fromDate = "2024-10-21T08:08:08Z";
        String entityId = "AC30";
        String entityType = null;
        String toDate = "2024-10-12T08:08:08Z";
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceGetDataRequest(fromDate, toDate, entityType, entityId);
        });
        assertEquals(ApplicationConstants.INVALID_FIELD_ENTITY_TYPE_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreate_Success() {
        AudienceCreate audience = createAudienceObj();
        validator.validateAudienceCreate(audience);
    }

    @Test
    public void testValidateAudienceGetDataRequest_Success() {
        validator.validateAudienceGetDataRequest("2024-10-12T08:08:08Z", "2024-10-21T08:08:08Z", "A", "entityId123");
    }

    @Test
    public void testValidateAudienceUpdateData_Success() {
        AudienceUpdate audienceUpdate = createAudienceUpdateObj();
        validator.validateAudienceUpdateData(audienceUpdate);
    }

    @Test
    public void testValidateAudienceCreateForInvalidEndDateTimeFormat() {
        AudienceCreate audience = createAudienceObj();
        audience.setEndDateTime("2024-10-21T08:08:08");
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals(ApplicationConstants.INVALID_END_DATE_TIME_FORMAT_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceUpdateDataForInvalidEndDateTimeFormat() {
        AudienceUpdate audienceUpdate = createAudienceUpdateObj();
        audienceUpdate.setEndDateTime("invalid-date");
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceUpdateData(audienceUpdate);
        });
        assertEquals(ApplicationConstants.INVALID_END_DATE_TIME_FORMAT_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceGetDataRequest_InvalidBeginDateFormat() {
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceGetDataRequest("invalid-date", "2024-10-21T08:08:08Z", "A", "entityId");
        });
        assertEquals(ApplicationConstants.INVALID_BEGIN_DATE_TIME_FORMAT_ERR_MSG, exception.getMessage());
    }

    @Test
    public void testValidateAudienceGetDataRequest_WithNullDates() {
        validator.validateAudienceGetDataRequest(null, null, "A", "entityId");
    }

    @Test
    public void testValidateAudienceUpdateData_WithNullDates() {
        AudienceUpdate audienceUpdate = new AudienceUpdate();
        audienceUpdate.setBeginDateTime(null);
        audienceUpdate.setEndDateTime(null);
        validator.validateAudienceUpdateData(audienceUpdate);
    }

    @Test
    public void testValidateAudienceCreateForHouseholdEntityType() {
        AudienceCreate audience = createAudienceObj();
        audience.setEntityType("H");
        validator.validateAudienceCreate(audience);
    }

    @Test
    public void testValidateAudienceCreateForAccountEntityType() {
        AudienceCreate audience = createAudienceObj();
        audience.setEntityType("A");
        validator.validateAudienceCreate(audience);
    }
}