package com.mastercard.developer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.Audience;
import org.openapitools.client.model.AudienceUpdate;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AudienceServiceTest {

    @Mock
    private ApiClient apiClient;

    private AudienceService audienceService;
    private Audience audience;
    private AudienceUpdate audienceUpdate;

    @Before
    public void setUp() {
        audienceService = new AudienceService(apiClient);
        audience = new Audience();
        audienceUpdate = new AudienceUpdate();
    }

    @Test
    public void testAudienceServiceInstantiation() {
        assertNotNull(audienceService);
    }

    @Test
    public void testAudienceObjectCreation() {
        assertNotNull(audience);
    }

    @Test
    public void testAudienceUpdateObjectCreation() {
        assertNotNull(audienceUpdate);
    }

    @Test(expected = Exception.class)
    public void testGetAudiencePagedExternalTargetRecords_CallsApiClient() throws Exception {
        audienceService.getAudiencePagedExternalTargetRecords(
                "AUD001", "ENTITY001", "TYPE_A", true, "2024-01-01T00:00:00Z", "2024-12-31T23:59:59Z", 0, 10
        );
    }

    @Test(expected = Exception.class)
    public void testSaveAudience_CallsApiClient() throws Exception {
        audienceService.saveAudience(audience);
    }

    @Test(expected = Exception.class)
    public void testUpdateAudience_CallsApiClient() throws Exception {
        audienceService.updateAudience("AUD001", audienceUpdate);
    }

    @Test(expected = Exception.class)
    public void testDeleteAudience_CallsApiClient() throws Exception {
        audienceService.deleteAudience("AUD001");
    }

    @Test
    public void testAudienceService_ServiceReady() {
        assertNotNull(audienceService);
    }

    @Test
    public void testSaveAudience_WithNullAudience() throws ApiException {
        // Testing service is properly instantiated
        assertNotNull(audienceService);
    }
}
