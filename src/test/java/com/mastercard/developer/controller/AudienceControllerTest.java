package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.AudienceService;
import com.mastercard.developer.validator.AudienceValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.Audience;
import org.openapitools.client.model.AudienceUpdate;
import org.openapitools.client.model.PagedResponseAudience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AudienceControllerTest {

    @InjectMocks
    AudienceController controller;

    @Mock
    AudienceService audienceService;

    @Mock
    AudienceValidator audienceValidator;

    @Autowired
    private MockMvc mockMvc;

    private static final String EXTERNAL_TARGET_CODE = RandomStringUtils.randomAlphabetic(10);
    private static final String ENTITY_REFERENCE_ID = RandomStringUtils.randomAlphabetic(10);
    private static final String REFERENCE_ID = RandomStringUtils.randomAlphabetic(10);

    @Before
    public void setUp() {
        controller = spy(new AudienceController(audienceValidator, audienceService));
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    public void testGetAudience_Success() throws Exception {
        when(audienceService.getAudiencePagedExternalTargetRecords(anyString(), anyString(), any(String.class), any(String.class),
                anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(new PagedResponseAudience());
        PagedResponseAudience response = controller.getAudiences(UUID.randomUUID().toString(), "A", "AC31",
                "2025-01-01T02:00:00Z", "2025-01-09T02:00:00Z", 0, 25);
        assertNotNull(response);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetTransactions_Exception() throws Exception {
        when(audienceService.getAudiencePagedExternalTargetRecords(anyString(), anyString(), any(String.class), any(String.class),
                anyString(), any(Integer.class), any(Integer.class)))
                .thenThrow(new ApiException());
        controller.getAudiences(UUID.randomUUID().toString(), "A", "AC31",
                "2025-01-01T02:00:00Z", "2025-01-09T02:00:00Z", 0, 25);
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
}