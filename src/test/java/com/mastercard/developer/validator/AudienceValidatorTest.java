package com.mastercard.developer.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.model.Audience;
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
        assertEquals("Invalid Begin Date Time Format", exception.getMessage());
    }

    @Test
    public void testValidatePostCreateRequestForInvalidFormatOfBeginDate() {
        Audience audience = createAudienceObj();
        audience.setBeginDateTime("2024-10-21T08:08:08Z");
        audience.setEndDateTime("2024-10-12T08:08:08Z");
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals("The End Date Time should be after the Begin Date Time", exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForInvalidCode() {
        Audience audience = createAudienceObj();
        audience.setCode(null);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals("External Target Code cannot be null", exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForInvalidBeginDate() {
        Audience audience = createAudienceObj();
        audience.setBeginDateTime(null);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals("External Target Record Begin Date cannot be null", exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForNullEntityType() {
        Audience audience = createAudienceObj();
        audience.setEntityType(null);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals("External Target Record Entity Type cannot be null", exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForInvalidEntityType() {
        Audience audience = createAudienceObj();
        audience.setEntityType("S");
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals("External Target Record Entity Type must be either A or H", exception.getMessage());
    }

    @Test
    public void testValidateAudienceCreateForNullEntityId() {
        Audience audience = createAudienceObj();
        audience.setEntityId(null);
        Exception exception = assertThrows(Exception.class, () -> {
            validator.validateAudienceCreate(audience);
        });
        assertEquals("External Target Record Entity Reference Id cannot be null", exception.getMessage());
    }

    private Audience createAudienceObj() {
        Audience audience = new Audience();
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
        assertEquals("External Target Record Entity Reference Id cannot be null", exception.getMessage());
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
        assertEquals("External Target Record Entity Type cannot be null", exception.getMessage());
    }
}