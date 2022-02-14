package com.mastercard.developer.service;


import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PromotionProgressApi;
import org.openapitools.client.model.PromotionProgressList;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProgressService {

    private final PromotionProgressApi promotionProgressApi;

    public ProgressService(ApiClient apiClient) {
        this.promotionProgressApi = new PromotionProgressApi(apiClient);
    }

    public PromotionProgressList getProgress(String householdId, String accountId, String promotionId, Boolean includeHistory) throws ApiException {
        return promotionProgressApi.retrieveUsingGET(householdId, accountId, promotionId, includeHistory);
    }

}
