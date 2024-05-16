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
import java.util.UUID;

public class OptInValidatorTest {
    private static final UUID PROMOTION_REFERENCE_ID = UUID.fromString("fc8bd7d2-77a6-4630-97b3-0a52aedce29f");
    private static final UUID ACCOUNT_ID = UUID.fromString("cdd343ea-63ce-4346-b33e-5da5d7222bfc");

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
        expectedEx.expectMessage("Either the rewardsCompanyId or programId or promotionId or accountId must be provided");
        validator.validateGetPromotions(null, null, null, null);
    }

    @Test
    public void testValidateGetPromotionsCompanyIdProgramReferenceIdBothNullAndUserIdNotNull() throws Exception {
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("Either the rewardsCompanyId, programId or promotionId must be provided");
        validator.validateGetPromotions(null, null, null, UUID.fromString("c873c981-ae0d-4d42-8783-196455c34f45"));
    }

    @Test
    public void testValidateGetPromotionsCompanyIdProgramReferenceIdEmptyAndUserIdNotNull() throws Exception {
        validator.validateGetPromotions(UUID.fromString("aafd11e2-d1b8-4787-b848-5fc1d4533988"), null, null,  UUID.fromString("6e6c0c0b-7961-4d07-a7ce-738e9ba1ebe4"));
    }

    @Test
    public void testValidateGetPromotionsCompanyIdNullProgramReferenceIdNotNullAndUserIdNotNull() throws Exception {
        validator.validateGetPromotions(null, UUID.fromString("34dd11e2-d1b8-4787-b848-5fc1d453ac42"), null,  UUID.fromString("017f394d-cdd7-41db-b3fb-d46eae003f24"));
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

