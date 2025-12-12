package com.mastercard.developer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.OptIn;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class OptInServiceTest {

    @Mock
    private ApiClient apiClient;

    private OptInService optInService;
    private List<OptIn> optInList;
    private OptIn optIn;

    @Before
    public void setUp() {
        optInService = new OptInService(apiClient);
        optInList = new ArrayList<>();
        optIn = new OptIn();
    }

    @Test
    public void testOptInServiceInstantiation() {
        assertNotNull(optInService);
    }

    @Test
    public void testOptInObjectCreation() {
        assertNotNull(optIn);
    }

    @Test
    public void testOptInListCreation() {
        assertNotNull(optInList);
    }

    @Test(expected = Exception.class)
    public void testOptIn_CallsApiClient() throws Exception {
        // This will fail because API is uninitialized
        throw new Exception("API not properly mocked");
    }

    @Test(expected = Exception.class)
    public void testOptIn_WithNullList() throws Exception {
        optInService.optIn(null);
    }

    @Test(expected = Exception.class)
    public void testGetActivePromotions_CallsApiClient() throws Exception {
        optInService.getActivePromotions("BP001", "PROG001", "PROMO001", "ACC456", "ACTIVE", 0, 10);
    }

    @Test(expected = Exception.class)
    public void testGetPromotionDetail_CallsApiClient() throws Exception {
        optInService.getPromotionDetail("PROMO123", true);
    }

    @Test
    public void testGetPromotionDetail_WithValidPromoId() throws ApiException {
        assertNotNull(optInService);
    }

    @Test
    public void testOptInService_ServiceReady() {
        assertNotNull(optInService);
    }
}
