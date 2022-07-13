package com.mastercard.developer.service;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PromotionManagementApi;
import org.openapitools.client.model.OptIn;
import org.openapitools.client.model.PromotionDetail;
import org.openapitools.client.model.Promotions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OptInService {

    private final PromotionManagementApi promotionManagementApi;

    @Autowired
    public OptInService(ApiClient apiClient) {
        this.promotionManagementApi = new PromotionManagementApi(apiClient);
    }

    public void optIn(List<OptIn> optInCreateValueBeanList) throws ApiException {
        promotionManagementApi.promotionOptInUsingPOST(optInCreateValueBeanList);
    }

    public Promotions getActivePromotions(String businessPartnerReferenceId, String programReferenceId, String promotionId, String accountId, String promotionState, Integer offset, Integer limit) throws ApiException {
        return promotionManagementApi.getPromotionsUsingGET(businessPartnerReferenceId, programReferenceId, promotionId, accountId, promotionState, offset, limit);
    }

    public PromotionDetail getPromotionDetail(String promotionId) throws ApiException {
        return promotionManagementApi.getPromotionDetailsUsingGET(promotionId);
    }
}

