package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.AudienceService;
import com.mastercard.developer.validator.AudienceValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AudiencesApi;
import org.openapitools.client.model.Audience;
import org.openapitools.client.model.AudienceUpdate;
import org.openapitools.client.model.PagedResponseAudience;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.mastercard.developer.constants.ApplicationConstants.END_DATE_SHOULD_BE_AFTER_BEGIN_DATE;
import static com.mastercard.developer.constants.ApplicationConstants.END_DATE_SHOULD_BE_AFTER_BEGIN_DATE_ERR_MSG;
import static com.mastercard.developer.constants.ApplicationConstants.INVALID_FIELD_ENTITY_TYPE;
import static com.mastercard.developer.constants.ApplicationConstants.INVALID_FIELD_ENTITY_TYPE_ERR_MSG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AudienceControllerTest {

    @InjectMocks
    AudienceController controller;

    @Mock
    AudienceService audienceService;

    @Mock
    AudienceValidator audienceValidator;

    @Mock
    AudiencesApi audiencesApi;

    private static final String EXTERNAL_TARGET_CODE = RandomStringUtils.randomAlphabetic(10);
    private static final String ENTITY_REFERENCE_ID = RandomStringUtils.randomAlphabetic(10);
    private static final String REFERENCE_ID = RandomStringUtils.randomAlphabetic(10);

    @Test
    public void testGetAudience_Success() throws Exception {
        PagedResponseAudience pagedResponseAudience = getPagedResponseOfAudience();
        when(audienceService.getAudiencePagedExternalTargetRecords(anyString(), anyString(), anyString(), anyBoolean(), anyString(),
                anyString(), anyInt(), anyInt()))
                .thenReturn(pagedResponseAudience);
        PagedResponseAudience response = controller.getAudiences(UUID.randomUUID().toString(), "A", "AC31", true,
                "2025-01-01T02:00:00Z", "2025-01-09T02:00:00Z", 0, 25);
        assertNotNull(response);
        assertEquals(1, response.getCount().intValue());
    }

    @Test(expected = InvalidRequest.class)
    public void testGetTransactions_Exception() throws Exception {
        when(audienceService.getAudiencePagedExternalTargetRecords(anyString(), anyString(), any(String.class), anyBoolean(), any(String.class),
                anyString(), any(Integer.class), any(Integer.class)))
                .thenThrow(new ApiException());
        controller.getAudiences(UUID.randomUUID().toString(), "A", "AC31", true,
                "2025-01-01T02:00:00Z", "2025-01-09T02:00:00Z", 0, 25);
    }

    @Test
    public void testGetTransactions_ExceptionForNullEntityType() {
        String fromDate = "2024-10-21T08:08:08Z";
        String entityId = "id31";
        String code = "AC31";
        boolean includeHistory = true;
        String entityType = null;
        String toDate = "2024-10-12T08:08:08Z";
        doThrow(new InvalidRequest(INVALID_FIELD_ENTITY_TYPE, INVALID_FIELD_ENTITY_TYPE_ERR_MSG))
                .when(audienceValidator).validateAudienceGetDataRequest(fromDate, toDate, entityType, entityId);
        try {
            controller.getAudiences(entityId, entityType, code, includeHistory, fromDate, toDate, 0, 25);
        } catch (InvalidRequest ex) {
            assertEquals(INVALID_FIELD_ENTITY_TYPE_ERR_MSG, ex.getMessage());
        }
    }

    @Test
    public void testSuccessCreateAudience() throws Exception {
        Audience request = getAudienceObject();
        Audience audienceResponse = getAudienceObject();
        when(audienceService.saveAudience(any())).thenReturn(audienceResponse);
        Audience response = controller.createAudience(request);
        assertNotNull(response);
    }

    @Test(expected = InvalidRequest.class)
    public void testCreateAudience_Exception() throws Exception {
        Audience request = getAudienceObject();
        when(audienceService.saveAudience(any())).thenThrow(new ApiException());
        controller.createAudience(request);
    }

    @Test
    public void testCreateAudience_ExceptionForNullEntityType() {
        Audience request = getAudienceObject();
        request.setEntityType(null);
        doThrow(new InvalidRequest(INVALID_FIELD_ENTITY_TYPE, INVALID_FIELD_ENTITY_TYPE_ERR_MSG))
                .when(audienceValidator).validateAudienceCreate(request);
        try {
            controller.createAudience(request);
        } catch (InvalidRequest ex) {
            assertEquals(INVALID_FIELD_ENTITY_TYPE_ERR_MSG, ex.getMessage());
        }
    }

    @Test
    public void testSuccessUpdateAudience() throws Exception {
        String referenceId = "90eb6039-bd49-44ed-835f-62052253b00e";
        AudienceUpdate request = getAudienceUpdateObject();
        Audience audienceResponse = getAudienceObject();
        when(audienceService.updateAudience(anyString(), any())).thenReturn(audienceResponse);
        Audience response = controller.updateAudience(referenceId, request);
        assertNotNull(response);
    }

    @Test(expected = InvalidRequest.class)
    public void testUpdateAudience_Exception() throws Exception {
        String referenceId = "90eb6039-bd49-44ed-835f-62052253b00e";
        AudienceUpdate request = getAudienceUpdateObject();
        when(audienceService.updateAudience(anyString(), any())).thenThrow(new ApiException());
        controller.updateAudience(referenceId, request);
    }

    @Test
    public void testUpdateAudience_ExceptionForIncorrectEndDateTime() {
        String referenceId = "90eb6039-bd49-44ed-835f-62052253b00e";
        AudienceUpdate request = getAudienceUpdateObject();
        request.setEndDateTime(LocalDateTime.now().minusDays(10).toString());
        doThrow(new InvalidRequest(END_DATE_SHOULD_BE_AFTER_BEGIN_DATE, END_DATE_SHOULD_BE_AFTER_BEGIN_DATE_ERR_MSG))
                .when(audienceValidator).validateAudienceUpdateData(request);
        try {
            controller.updateAudience(referenceId, request);
        } catch (InvalidRequest ex) {
            assertEquals(END_DATE_SHOULD_BE_AFTER_BEGIN_DATE_ERR_MSG, ex.getMessage());
        }
    }

    @Test
    public void testSuccessDeleteAudience() throws Exception {
        String referenceId = "90eb6039-bd49-44ed-835f-62052253b00e";
        doNothing().when(audienceService).deleteAudience(referenceId);
        ResponseEntity response = controller.deleteAudience(referenceId);
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    }

    @Test
    public void testDeleteAudience_Exception() throws Exception {
        String referenceId = "abc123";
        doThrow(new ApiException(400, null, "Invalid audienceId"))
                .when(audienceService).deleteAudience(referenceId);
        try {
            controller.deleteAudience(referenceId);
        } catch (InvalidRequest ex) {
            assertEquals("Invalid audienceId", ex.getMessage());
        }
    }

    private Audience getAudienceObject() {
        Audience audience = new Audience();
        audience.setCode(EXTERNAL_TARGET_CODE);
        audience.setEntityId(ENTITY_REFERENCE_ID);
        audience.setEntityType("A");
        audience.setId(REFERENCE_ID);
        audience.setBeginDateTime(LocalDateTime.now().toString());
        audience.setEndDateTime(LocalDateTime.now().plusDays(10).toString());
        return audience;
    }

    private AudienceUpdate getAudienceUpdateObject() {
        AudienceUpdate audienceUpdate = new AudienceUpdate();
        audienceUpdate.setBeginDateTime(LocalDateTime.now().toString());
        audienceUpdate.setEndDateTime(LocalDateTime.now().plusDays(10).toString());
        return audienceUpdate;
    }

    private PagedResponseAudience getPagedResponseOfAudience() {
        PagedResponseAudience searchResponse = new PagedResponseAudience();
        searchResponse.setCount(1);
        searchResponse.addItemsItem(new Audience());
        return searchResponse;
    }
}