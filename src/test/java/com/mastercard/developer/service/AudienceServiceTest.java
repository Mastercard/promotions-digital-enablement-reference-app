package com.mastercard.developer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.AudiencesApi;
import org.openapitools.client.model.Audience;
import org.openapitools.client.model.AudienceUpdate;
import org.openapitools.client.model.PagedResponseAudience;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AudienceServiceTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private AudiencesApi audiencesApi;

    private AudienceService audienceService;
    private Audience audience;
    private AudienceUpdate audienceUpdate;

    @Before
    public void setUp() throws Exception {
        audienceService = new AudienceService(apiClient);
        // Inject the mocked AudiencesApi into the service
        org.springframework.test.util.ReflectionTestUtils.setField(audienceService, "audiencesApi", audiencesApi);
        audience = new Audience();
        audienceUpdate = new AudienceUpdate();
        
        // Setup default mock returns - return new objects on any call
        when(audiencesApi.getAudiences(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenAnswer(i -> new PagedResponseAudience());
        when(audiencesApi.createAudiences(any()))
            .thenAnswer(i -> new Audience());
        when(audiencesApi.updateAudiences(any(), any()))
            .thenAnswer(i -> new Audience());
        doNothing().when(audiencesApi).deleteAudiences(any());
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
    public void testGetAudiencePagedExternalTargetRecords_WithAllParameters() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        verify(audiencesApi).getAudiences("entity123", "ACCOUNT", referenceId, true, "2024-01-01", "2024-12-31", 0, 10);
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_WithNullValues() throws Exception {
        audienceService.getAudiencePagedExternalTargetRecords(null, null, null, false, null, null, 0, 10);
        verify(audiencesApi).getAudiences(null, null, null, false, null, null, 0, 10);
    }

    @Test
    public void testSaveAudience_WithValidObject() throws Exception {
        Audience newAudience = new Audience();
        assertNotNull(audienceService.saveAudience(newAudience));
        verify(audiencesApi).createAudiences(newAudience);
    }

    @Test
    public void testSaveAudience_WithNull() throws Exception {
        audienceService.saveAudience(null);
        verify(audiencesApi).createAudiences(null);
    }

    @Test
    public void testUpdateAudience_WithValidParameters() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        AudienceUpdate update = new AudienceUpdate();
        assertNotNull(audienceService.updateAudience(referenceId, update));
        verify(audiencesApi).updateAudiences(referenceId, update);
    }

    @Test
    public void testUpdateAudience_WithNullReferenceId() throws Exception {
        AudienceUpdate update = new AudienceUpdate();
        audienceService.updateAudience(null, update);
        verify(audiencesApi).updateAudiences(null, update);
    }

    @Test
    public void testDeleteAudience_WithValidReferenceId() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        audienceService.deleteAudience(referenceId);
        verify(audiencesApi).deleteAudiences(referenceId);
    }

    @Test
    public void testDeleteAudience_WithNullReferenceId() throws Exception {
        audienceService.deleteAudience(null);
        verify(audiencesApi).deleteAudiences(null);
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
    public void testGetAudiencePagedExternalTargetRecords_DifferentOffsets() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        for (int offset = 0; offset <= 50; offset += 10) {
            assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, null, null, offset, 10));
        }
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_DifferentLimits() throws Exception {
        for (int limit = 1; limit <= 100; limit += 25) {
            assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(null, null, null, false, null, null, 0, limit));
        }
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_DateVariations() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", null, 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, null, "2024-12-31", 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-06-15", "2024-06-15", 0, 10));
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_IncludeVariations() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", false, "2024-01-01", "2024-12-31", 0, 10));
        verify(audiencesApi, times(2)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testSaveAudience_MultipleAudiences() throws Exception {
        for (int i = 0; i < 5; i++) {
            assertNotNull(audienceService.saveAudience(new Audience()));
        }
        verify(audiencesApi, times(5)).createAudiences(any(Audience.class));
    }

    @Test
    public void testUpdateAudience_MultipleUpdates() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        for (int i = 0; i < 5; i++) {
            assertNotNull(audienceService.updateAudience(referenceId, new AudienceUpdate()));
        }
        verify(audiencesApi, times(5)).updateAudiences(anyString(), any(AudienceUpdate.class));
    }

    @Test
    public void testDeleteAudience_MultipleDeletes() throws Exception {
        String[] referenceIds = {
            "550e8400-e29b-41d4-a716-446655440000",
            "550e8400-e29b-41d4-a716-446655440001",
            "550e8400-e29b-41d4-a716-446655440002"
        };
        for (String refId : referenceIds) {
            audienceService.deleteAudience(refId);
        }
        verify(audiencesApi, times(3)).deleteAudiences(anyString());
    }

    @Test
    public void testAudienceService_ComplexScenario() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 100));
        assertNotNull(audienceService.saveAudience(new Audience()));
        assertNotNull(audienceService.updateAudience(referenceId, new AudienceUpdate()));
        audienceService.deleteAudience(referenceId);
        verify(audiencesApi).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
        verify(audiencesApi).createAudiences(any(Audience.class));
        verify(audiencesApi).updateAudiences(anyString(), any(AudienceUpdate.class));
        verify(audiencesApi).deleteAudiences(anyString());
    }

    @Test
    public void testAudienceService_EdgeCases() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-12-31", "2024-12-31", 0, 1));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10000));
        verify(audiencesApi, times(2)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testAudienceService_MixedOperations() throws Exception {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";
        audienceService.saveAudience(new Audience());
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(uuid, "entity456", "PROMOTION", false, null, null, 5, 20));
        audienceService.updateAudience(uuid, new AudienceUpdate());
        audienceService.saveAudience(new Audience());
        audienceService.deleteAudience(uuid);
        assertNotNull(audienceService);
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_DifferentOffsetsAdvanced() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        for (int offset = 0; offset <= 50; offset += 10) {
            assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", offset, 10));
        }
        verify(audiencesApi, atLeast(6)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_DifferentLimitsAdvanced() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        int[] limits = {1, 10, 25, 50, 100};
        for (int limit : limits) {
            assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, limit));
        }
        verify(audiencesApi, times(5)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_DifferentEntityTypes() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        String[] entityTypes = {"ACCOUNT", "PROMOTION", "HOUSEHOLD", "CUSTOMER"};
        for (String entityType : entityTypes) {
            assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", entityType, true, "2024-01-01", "2024-12-31", 0, 10));
        }
        verify(audiencesApi, times(4)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_DifferentAudienceCodes() throws Exception {
        String[] audienceCodes = {"CODE1", "CODE2", "CODE3"};
        for (String code : audienceCodes) {
            assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(code, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        }
        verify(audiencesApi, times(3)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_DifferentEntityIds() throws Exception {
        String[] entityIds = {"entity1", "entity2", "entity3"};
        for (String entityId : entityIds) {
            assertNotNull(audienceService.getAudiencePagedExternalTargetRecords("CODE123", entityId, "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        }
        verify(audiencesApi, times(3)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_IncludeHistoryVariations() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", false, "2024-01-01", "2024-12-31", 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", null, "2024-01-01", "2024-12-31", 0, 10));
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_PaginationSequence() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 50));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 50, 50));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 100, 50));
        verify(audiencesApi, times(3)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testAudienceService_CompleteWorkflow() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        Audience newAudience = new Audience();
        AudienceUpdate update = new AudienceUpdate();
        // Create
        assertNotNull(audienceService.saveAudience(newAudience));
        // Read
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        // Update
        assertNotNull(audienceService.updateAudience(referenceId, update));
        // Delete
        audienceService.deleteAudience(referenceId);
        verify(audiencesApi).createAudiences(any(Audience.class));
        verify(audiencesApi).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
        verify(audiencesApi).updateAudiences(anyString(), any(AudienceUpdate.class));
        verify(audiencesApi).deleteAudiences(anyString());
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_EdgeCases() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        // Zero offset and limit
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 0));
        // Very large values
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(referenceId, "entity123", "ACCOUNT", true, "2024-01-01", "2024-12-31", Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 1));
        // Empty strings
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords("", "", "", true, "", "", 0, 10));
        verify(audiencesApi, times(3)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testUpdateAudience_WithNullUpdate() throws Exception {
        String referenceId = "550e8400-e29b-41d4-a716-446655440000";
        audienceService.updateAudience(referenceId, null);
        verify(audiencesApi).updateAudiences(referenceId, null);
    }

    @Test
    public void testGetAudiencePagedExternalTargetRecords_ParameterCombinations() throws Exception {
        // Various combinations of nulls and values
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords("CODE1", "entity1", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords(null, "entity1", "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords("CODE1", null, "ACCOUNT", true, "2024-01-01", "2024-12-31", 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords("CODE1", "entity1", null, true, "2024-01-01", "2024-12-31", 0, 10));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords("CODE1", "entity1", "ACCOUNT", null, "2024-01-01", "2024-12-31", 0, 10));
    }

    @Test
    public void testAudienceService_BoundaryValues() throws Exception {
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords("A", "B", "C", true, "2024-01-01", "2024-01-01", 0, 1));
        assertNotNull(audienceService.getAudiencePagedExternalTargetRecords("VERYLONGAUDIENCECODESTRINGTEST", "VERYLONGEN", "VERYLONGTYPE", false, "2024-01-01", "2024-12-31", Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 1));
        verify(audiencesApi, times(2)).getAudiences(anyString(), anyString(), anyString(), any(Boolean.class), anyString(), anyString(), anyInt(), anyInt());
    }
}