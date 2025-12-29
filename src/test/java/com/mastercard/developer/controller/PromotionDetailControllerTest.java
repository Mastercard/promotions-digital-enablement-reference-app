package com.mastercard.developer.controller;

import com.mastercard.developer.exception.EntityNotFoundException;
import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.OptInService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.PromotionDetail;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PromotionDetailControllerTest {

    @InjectMocks
    PromotionDetailController controller;

    @Mock
    OptInService optInService;

    @Test
    public void testGetPromotionDetails_Success() throws Exception {
        PromotionDetail promotionDetail = getPromotionDetail();
        when(optInService.getPromotionDetail(anyString(), anyBoolean()))
                .thenReturn(promotionDetail);
        PromotionDetail response = controller.getPromotionDetails(UUID.randomUUID().toString(), false);
        assertNotNull(response);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetPromotionDetails_NullResponse() throws Exception {
        when(optInService.getPromotionDetail(anyString(), anyBoolean()))
                .thenReturn(null);
        controller.getPromotionDetails(UUID.randomUUID().toString(), false);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPromotionDetails_NotFound() throws Exception {
        when(optInService.getPromotionDetail(anyString(), anyBoolean()))
                .thenThrow(new ApiException(HttpStatus.NOT_FOUND.value(), null, "Promotion not found"));
        controller.getPromotionDetails(UUID.randomUUID().toString(), false);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetPromotionDetails_BadRequest() throws Exception {
        when(optInService.getPromotionDetail(anyString(), anyBoolean()))
                .thenThrow(new ApiException(HttpStatus.BAD_REQUEST.value(), null, "Invalid request"));
        controller.getPromotionDetails(UUID.randomUUID().toString(), false);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetPromotionDetails_ApiException() throws Exception {
        when(optInService.getPromotionDetail(anyString(), anyBoolean()))
                .thenThrow(new ApiException(500, null, "Internal server error"));
        controller.getPromotionDetails(UUID.randomUUID().toString(), false);
    }

    @Test
    public void testGetPromotionDetails_WithAudience() throws Exception {
        PromotionDetail promotionDetail = getPromotionDetail();
        when(optInService.getPromotionDetail(anyString(), anyBoolean()))
                .thenReturn(promotionDetail);
        PromotionDetail response = controller.getPromotionDetails(UUID.randomUUID().toString(), true);
        assertNotNull(response);
    }

    private PromotionDetail getPromotionDetail() {
        PromotionDetail promotionDetail = new PromotionDetail();
        promotionDetail.setPromotionId(UUID.randomUUID().toString());
        return promotionDetail;
    }
}
