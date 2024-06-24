package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.ProgressService;
import com.mastercard.developer.validator.ProgressValidator;
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
import org.openapitools.client.model.Cap;
import org.openapitools.client.model.ProgressSummary;
import org.openapitools.client.model.PromotionProgress;
import org.openapitools.client.model.PromotionProgressList;
import org.openapitools.client.model.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class ProgressControllerTest {

    @InjectMocks
    ProgressController controller;

    @Mock
    ProgressService progressService;

    @Mock
    ProgressValidator progressValidator;

    @Autowired
    private MockMvc mockMvc;

    private static final String PROGRESS = "/loyalty/rewards/promotion-progresses";

    @Before
    public void setUp() {
        controller = spy(new ProgressController(progressValidator, progressService));
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    public void testGetProgressByHouseholdIdAndAccountId() throws Exception {
        PromotionProgressList promotionProgressResponseBean = getResponse();
        Mockito.when(progressService.getProgress(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(promotionProgressResponseBean);

        PromotionProgressList response = controller.getProgress("5283da1c-2bdf-40cc-a0e1-781fe30eab13", "9583da1c-2bdf-40cc-a0e1-781fe30eab12", null, false);
        Assert.assertNotNull(response);
        Assert.assertTrue("5283da1c-2bdf-40cc-a0e1-781fe30eab13".equalsIgnoreCase(response.getPromotionProgresses().get(0).getProgresses().get(0).getEntityId()));
        Assert.assertTrue("9583da1c-2bdf-40cc-a0e1-781fe30eab12".equalsIgnoreCase(response.getPromotionProgresses().get(0).getCaps().get(0).getEntityId()));
    }

    @Test(expected = InvalidRequest.class)
    public void testGetProgressByAccountIdFailure() throws Exception {
        doThrow(ApiException.class).when(progressService).getProgress(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROGRESS);
        controller.getProgress(null, "9583da1c-2bdf-40cc-a0e1-781fe30eab12", null, false);
    }

    @Test
    public void testGetProgressByAccountIdAndPromotionId() throws Exception {
        PromotionProgressList promotionProgressResponseBean = getResponse();
        Mockito.when(progressService.getProgress(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(promotionProgressResponseBean);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(PROGRESS);

        PromotionProgressList response = controller.getProgress(null, "9583da1c-2bdf-40cc-a0e1-781fe30eab12", "6683aa1c-2bdf-40cc-a0e1-781fe30eab19", false);
        Assert.assertNotNull(response);
        Assert.assertTrue("9583da1c-2bdf-40cc-a0e1-781fe30eab12".equalsIgnoreCase(response.getPromotionProgresses().get(0).getCaps().get(0).getEntityId()));
        Assert.assertTrue("6683aa1c-2bdf-40cc-a0e1-781fe30eab19".equalsIgnoreCase(response.getPromotionProgresses().get(0).getPromotionId()));
    }

    private PromotionProgress getPromotion() {
        List<ProgressSummary> progressSummaryBeanList = new ArrayList<>();
        List<Rule> ruleList = new ArrayList<>();
        List<Cap> capList = new ArrayList<>();

        PromotionProgress promotion = new PromotionProgress();

        progressSummaryBeanList.add(getProgress("HOUSEHOLD", "5283da1c-2bdf-40cc-a0e1-781fe30eab13", 5));

        ruleList.add(getRule("HOUSEHOLD", "5283da1c-2bdf-40cc-a0e1-781fe30eab13", "6cef30ef-bcfc-429f-9aa5-b7bfc0a108f5", 5L, "COUNT", 4L, "M", "202006"));

        capList.add(getCap("ACCOUNT", "9583da1c-2bdf-40cc-a0e1-781fe30eab12", "1afd11e2-d1b8-4787-b848-5fc1d4533986", "M", "202005", 5L, 3L));

        promotion.setPromotionId("6683aa1c-2bdf-40cc-a0e1-781fe30eab19");
        promotion.setActive(true);
        promotion.setProgramId("36a28951-86bf-45bc-bcf1-a89aa8ffdff0");
        promotion.setProgresses(progressSummaryBeanList);
        promotion.setRules(ruleList);
        promotion.setCaps(capList);
        return promotion;
    }

    private ProgressSummary getProgress(String entityType, String entityId, Integer totalRewardsReceived) {
        ProgressSummary progress = new ProgressSummary();
        progress.setEntityType(entityType);
        progress.setEntityId(entityId);
        progress.setTotalRewardsReceived(totalRewardsReceived);
        return progress;
    }

    private Rule getRule(String entityType, String entityId, String ruleId, Long ruleThreshold, String ruleType, Long ruleValue, String rulePeriod, String rulePeriodValue) {
        Rule rule = new Rule();
        rule.setEntityType(entityType);
        rule.setEntityId(entityId);
        rule.setRuleId(ruleId);
        rule.setRuleThreshold(ruleThreshold);
        rule.setRuleType(ruleType);
        rule.setRuleValue(ruleValue);
        rule.setRulePeriod(rulePeriod);
        rule.setRulePeriodValue(rulePeriodValue);
        return rule;
    }

    private Cap getCap(String entityType, String entityId, String capId, String capPeriod, String capPeriodValue, Long capThreshold, Long capValue) {
        Cap cap = new Cap();
        cap.setEntityType(entityType);
        cap.setEntityId(entityId);
        cap.setCapId(capId);
        cap.setCapPeriod(capPeriod);
        cap.setCapPeriodValue(capPeriodValue);
        cap.setCapThreshold(capThreshold);
        cap.setCapValue(capValue);
        return cap;
    }

    public PromotionProgressList getResponse() {
        PromotionProgressList promotionProgressResponseBean = new PromotionProgressList();
        List<PromotionProgress> promotionList = new ArrayList<>();
        promotionList.add(getPromotion());
        promotionProgressResponseBean.setPromotionProgresses(promotionList);
        return promotionProgressResponseBean;
    }
}
