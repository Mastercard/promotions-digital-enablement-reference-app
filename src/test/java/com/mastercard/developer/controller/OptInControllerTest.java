package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.OptInService;
import com.mastercard.developer.validator.OptInValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.OptIn;
import org.openapitools.client.model.Promotion;
import org.openapitools.client.model.Promotions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class OptInControllerTest {

    @InjectMocks
    OptInController controller;

    @Mock
    OptInService optInService;

    @Mock
    OptInValidator validator;

    @Autowired
    private MockMvc mockMvc;

    private static final UUID REWARDS_COMPANY_ID = UUID.fromString("3c16daf9-8c2c-437a-99e6-1da37c3c8997");
    private static final UUID PROGRAM_REF_ID = UUID.fromString("4af2d508-96de-4a84-bcb0-c60337bc57b8");
    private static final UUID PROMOTION_REFERENCE_ID = UUID.fromString("38771612-a145-4096-a024-22578b1a2d8a");
    private static final String PROMOTION_REFERENCE_ID_INVALID = "PROMOTION_REFERENCE_ID_INVALID";
    private static final UUID ACCOUNT_ID = UUID.fromString("63389d6c-cb0e-4911-a845-4424ff36ea2d");
    private static final String PROMOTION_ACTIVATION = "/promotion-activations";
    private static final String PROMOTIONS = "/promotions";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
        controller = spy(new OptInController(validator, optInService));
        MockitoAnnotations.openMocks(this);
    }

    private static final String BASE_API_PATH = "/api/promotions";
    private static final UUID PROMOTION_REF_ID = UUID.fromString("ad2a9cf9-c00f-497a-9344-eef86d22370a");


    @Test
    public void testGetPromotionsByCompanyId() throws Exception {
        Promotion activePromotionValueBean = getEligiblePromotionValueBean();
        Promotions pagedResponseActivePromotionValueBean = new Promotions();
        pagedResponseActivePromotionValueBean.setItems(Arrays.asList(activePromotionValueBean));
        Mockito.when(optInService.getActivePromotions(REWARDS_COMPANY_ID, null, null, null, null, 0, 25)).thenReturn(pagedResponseActivePromotionValueBean);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTIONS);

        Promotions response = controller.getPromotions(REWARDS_COMPANY_ID, null, null, null, null, 0, 25);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getItems().get(0));
    }

    @Test(expected = InvalidRequest.class)
    public void testGetPromotionsByCompanyIdFailure() throws Exception {
        doThrow(ApiException.class).when(optInService).getActivePromotions(REWARDS_COMPANY_ID, null, null, null, null, 0, 25);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTIONS);

        controller.getPromotions(REWARDS_COMPANY_ID, null, null, null, null, 0, 25);
    }

    @Test
    public void testGetPromotionsByProgramReferenceId() throws Exception {
        Promotion activePromotionValueBean = getEligiblePromotionValueBean();
        Promotions pagedResponseActivePromotionValueBean = new Promotions();
        pagedResponseActivePromotionValueBean.setItems(Arrays.asList(activePromotionValueBean));
        Mockito.when(optInService.getActivePromotions(null, PROGRAM_REF_ID, null, null, null, 0, 25)).thenReturn(pagedResponseActivePromotionValueBean);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTIONS);

        Promotions response = controller.getPromotions(null, PROGRAM_REF_ID, null, null, null, 0, 25);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getItems().get(0));
    }

    @Test(expected = InvalidRequest.class)
    public void testGetPromotionsByProgramReferenceIdNFailure() throws Exception {
        doThrow(ApiException.class).when(optInService).getActivePromotions(null, PROGRAM_REF_ID, null, null, null, 0, 25);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTIONS);

        controller.getPromotions(null, PROGRAM_REF_ID, null, null, null, 0, 25);
    }

    @Test
    public void testCreateOptInSuccess() throws Exception {
        List<OptIn> optInCreateValueBeanList = new ArrayList<>();
        OptIn optInCreateValueBean = getNewOptInCreateValueBean();
        optInCreateValueBeanList.add(optInCreateValueBean);

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTION_ACTIVATION);

        doNothing().when(optInService).optIn(optInCreateValueBeanList);
        ResponseEntity<?> response = controller.promotionOptIn(optInCreateValueBeanList);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test(expected = InvalidRequest.class)
    public void testCreateOptInFailure() throws Exception {
        List<OptIn> optInCreateValueBeanList = new ArrayList<>();
        OptIn optInCreateValueBean = getNewOptInCreateValueBean();
        optInCreateValueBeanList.add(optInCreateValueBean);

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTION_ACTIVATION);

        doThrow(ApiException.class).when(optInService).optIn(optInCreateValueBeanList);
        controller.promotionOptIn(optInCreateValueBeanList);
    }

    public Promotion getEligiblePromotionValueBean() {
        Promotion promotionValueBean = new Promotion();
        promotionValueBean.setActivationRequired(true);
        promotionValueBean.setDescription("promotionDescription 1");
        promotionValueBean.setPromotionId(PROMOTION_REF_ID);
        promotionValueBean.setProgramId(PROGRAM_REF_ID);
        promotionValueBean.setName("promotion1");
        return promotionValueBean;
    }

    private OptIn getNewOptInCreateValueBean() {
        OptIn optInCreateValueBean = new OptIn();
        optInCreateValueBean.setPromotionId(PROMOTION_REFERENCE_ID);
        optInCreateValueBean.setActivation(true);
        optInCreateValueBean.setAccountId(ACCOUNT_ID);
        return optInCreateValueBean;
    }
}

