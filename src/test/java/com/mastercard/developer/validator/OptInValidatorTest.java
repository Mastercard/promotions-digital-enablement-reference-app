package com.mastercard.developer.validator;

import com.mastercard.developer.exception.InvalidRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openapitools.client.model.OptIn;

import java.util.ArrayList;
import java.util.List;

public class OptInValidatorTest {
    private static final String PROMOTION_REFERENCE_ID = "PROMOTION_REFERENCE_ID";
    private static final String ACCOUNT_ID = "ACCOUNT_ID";

    @InjectMocks
    private OptInValidator validator;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateGetPromotionsBothNull() throws Exception {
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("Either the rewardsCompanyId or programId or accountId must be provided");
        validator.validateGetPromotions(null, null, null);
    }

    @Test
    public void testValidateCreateActivationValueNull() {
        List<OptIn> OptInCreateValueBeanList = new ArrayList<>();
        OptIn optInCreateValueBean = getNewOptInCreateValueBean();
        optInCreateValueBean.setActivation(null);
        OptInCreateValueBeanList.add(optInCreateValueBean);

        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("activation cannot be null");
        validator.validateCreate(OptInCreateValueBeanList);
    }

    @Test
    public void testValidateCreatePromotionReferenceIdNull() {
        List<OptIn> OptInCreateValueBeanList = new ArrayList<>();
        OptIn optInCreateValueBean = getNewOptInCreateValueBean();
        optInCreateValueBean.setPromotionId(null);
        OptInCreateValueBeanList.add(optInCreateValueBean);

        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("promotionId cannot be null");
        validator.validateCreate(OptInCreateValueBeanList);
    }

    private OptIn getNewOptInCreateValueBean() {
        OptIn optInCreateValueBean = new OptIn();
        optInCreateValueBean.setPromotionId(PROMOTION_REFERENCE_ID);
        optInCreateValueBean.setActivation(true);
        optInCreateValueBean.setAccountId(ACCOUNT_ID);
        return optInCreateValueBean;
    }
}
