package com.mastercard.developer.service;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AudiencesApi;
import org.openapitools.client.model.Audience;
import org.openapitools.client.model.AudienceUpdate;
import org.openapitools.client.model.PagedResponseAudience;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AudienceService {

    private final AudiencesApi audiencesApi;

    public AudienceService(ApiClient apiClient) {
        this.audiencesApi = new AudiencesApi(apiClient);
    }

    public PagedResponseAudience getAudiencePagedExternalTargetRecords(String audienceCode, String entityId, String entityType, Boolean includeHistory,
                                                                       String fromDateTime, String toDateTime, Integer offset, Integer limit) throws ApiException {
        return audiencesApi.getAudiences(entityId, entityType, audienceCode, includeHistory, fromDateTime, toDateTime, offset, limit);
    }

    public Audience saveAudience(Audience audience) throws ApiException {
        return audiencesApi.createAudiences(audience);
    }

    public Audience updateAudience(String referenceId, AudienceUpdate audienceUpdate) throws ApiException {
        return audiencesApi.updateAudiences(referenceId, audienceUpdate);
    }

    public void deleteAudience(String referenceId) throws ApiException {
        audiencesApi.deleteAudiences(referenceId);
    }
}