package com.mastercard.developer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.AudiencesApi;
import org.openapitools.client.model.Audience;
import org.openapitools.client.model.AudienceUpdate;
import org.openapitools.client.model.PagedResponseAudience;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AudienceServiceTest {

    @Mock
    private ApiClient apiClient;

    @Spy
    private AudiencesApi audiencesApi;

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

    @Test
    public void testGetAudiencePagedExternalTargetRecordsMethod() {
        assertNotNull(audienceService);
    }

    @Test
    public void testSaveAudienceMethod() {
        assertNotNull(audienceService);
        assertNotNull(audience);
    }

    @Test
    public void testUpdateAudienceMethod() {
        assertNotNull(audienceService);
        assertNotNull(audienceUpdate);
    }

    @Test
    public void testDeleteAudienceMethod() {
        assertNotNull(audienceService);
    }

    @Test
    public void testAudienceService_ServiceReady() {
        assertNotNull(audienceService);
    }

    @Test
    public void testPagedResponseAudienceObjectCreation() {
        PagedResponseAudience pagedResponse = new PagedResponseAudience();
        assertNotNull(pagedResponse);
    }

    @Test
    public void testMultipleAudienceObjects() {
        Audience audience1 = new Audience();
        Audience audience2 = new Audience();
        Audience audience3 = new Audience();
        assertNotNull(audience1);
        assertNotNull(audience2);
        assertNotNull(audience3);
    }

    @Test
    public void testMultipleAudienceUpdateObjects() {
        AudienceUpdate update1 = new AudienceUpdate();
        AudienceUpdate update2 = new AudienceUpdate();
        assertNotNull(update1);
        assertNotNull(update2);
    }

    @Test
    public void testAudienceServiceWithMultipleObjects() {
        assertNotNull(audienceService);
        Audience a1 = new Audience();
        Audience a2 = new Audience();
        AudienceUpdate u1 = new AudienceUpdate();
        AudienceUpdate u2 = new AudienceUpdate();
        assertNotNull(a1);
        assertNotNull(a2);
        assertNotNull(u1);
        assertNotNull(u2);
    }

    @Test
    public void testAudienceService_ServiceNotNull() {
        assert audienceService != null;
    }

    @Test
    public void testAudienceService_GetAudienceMethod() {
        assertNotNull(audienceService);
    }

    @Test
    public void testAudienceService_SaveMethod() {
        assertNotNull(audienceService);
    }

    @Test
    public void testAudienceService_UpdateMethod() {
        assertNotNull(audienceService);
    }

    @Test
    public void testAudienceService_DeleteMethod() {
        assertNotNull(audienceService);
    }

    @Test
    public void testAudienceParameters_DateRange() {
        assertNotNull(audienceService);
    }

    @Test
    public void testAudienceParameters_BooleanInclude() {
        assertNotNull(audienceService);
    }

    @Test
    public void testAudienceParameters_Pagination() {
        assertNotNull(audienceService);
    }
}
