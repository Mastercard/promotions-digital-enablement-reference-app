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

    private static final String REWARDS_COMPANY_ID = RandomStringUtils.randomAlphabetic(10);
    private static final String PROGRAM_REF_ID = RandomStringUtils.randomAlphabetic(10);
    private static final String PROMOTION_REFERENCE_ID = "PROMOTION_REFERENCE_ID";
    private static final String PROMOTION_REFERENCE_ID_INVALID = "PROMOTION_REFERENCE_ID_INVALID";
    private static final String ACCOUNT_ID = "ACCOUNT_ID";
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
    private static final String PROMOTION_REF_ID = RandomStringUtils.randomAlphanumeric(10);


    @Test
    public void testGetPromotionsByCompanyId() throws Exception {
        Promotion activePromotionValueBean = getEligiblePromotionValueBean();
        Promotions pagedResponseActivePromotionValueBean = new Promotions();
        pagedResponseActivePromotionValueBean.setItems(Arrays.asList(activePromotionValueBean));
        Mockito.when(optInService.getActivePromotions(REWARDS_COMPANY_ID, null, null, null, 0, 25)).thenReturn(pagedResponseActivePromotionValueBean);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTIONS);

        Promotions response = controller.getPromotions(REWARDS_COMPANY_ID, null, null, null, 0, 25);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getItems().get(0));
    }

    @Test(expected = InvalidRequest.class)
    public void testGetPromotionsByCompanyIdFailure() throws Exception {
        doThrow(ApiException.class).when(optInService).getActivePromotions(REWARDS_COMPANY_ID, null, null, null, 0, 25);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTIONS);

        controller.getPromotions(REWARDS_COMPANY_ID, null, null, null, 0, 25);
    }

    @Test
    public void testGetPromotionsByProgramReferenceId() throws Exception {
        Promotion activePromotionValueBean = getEligiblePromotionValueBean();
        Promotions pagedResponseActivePromotionValueBean = new Promotions();
        pagedResponseActivePromotionValueBean.setItems(Arrays.asList(activePromotionValueBean));
        Mockito.when(optInService.getActivePromotions(null, PROGRAM_REF_ID, null, null, 0, 25)).thenReturn(pagedResponseActivePromotionValueBean);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTIONS);

        Promotions response = controller.getPromotions(null, PROGRAM_REF_ID, null, null, 0, 25);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getItems().get(0));
    }

    @Test(expected = InvalidRequest.class)
    public void testGetPromotionsByProgramReferenceIdNFailure() throws Exception {
        doThrow(ApiException.class).when(optInService).getActivePromotions(null, PROGRAM_REF_ID, null, null, 0, 25);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROMOTIONS);

        controller.getPromotions(null, PROGRAM_REF_ID, null, null, 0, 25);
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
