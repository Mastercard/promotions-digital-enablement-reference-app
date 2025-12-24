package com.mastercard.developer.validator;

import com.mastercard.developer.exception.InvalidRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class ProgressValidatorTest {

    @InjectMocks
    private ProgressValidator validator;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateGetPromotionsByAccountIdNull() throws Exception {
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("Either household_id or account_id or userId is required");
        validator.validateGetProgress(null, null,null);
    }
    
    @Test
    public void testValidateGetForNotEmptyHouseholdIdAndAccountId() throws Exception {
        validator.validateGetProgress("f35e51fe-bc77-432f-b412-3800e3c04e78", "935e51fe-bc77-432f-b412-3800e3c04e90",null);
    }
    
    @Test
    public void testValidateGetForNotEmptyHouseholdIdEmptyAccountId() throws Exception {
        validator.validateGetProgress("a35e51fe-bc77-432f-b412-3800e3c08942", "",null);
    }
    @Test
    public void testValidateGetForEmptyHouseholdIdNotEmptyAccountId() throws Exception {
        validator.validateGetProgress("", "bc5e51fe-bc77-432f-b412-3800e3c04ec2",null);
    }

    @Test
    public void testValidateGetPromotionsByHouseHoldIdAndUserId() throws Exception {
        expectedEx.expect(InvalidRequest.class);
        expectedEx.expectMessage("household_id and user_id cannot be used together");
        validator.validateGetProgress("f35e51fe-bc77-432f-b412-3800e3c04e78", null,"62087a7f-4215-4bfc-b3ee-cc12f085efc5");
    }

    @Test
    public void testValidateGetForNotEmptyUserIdAndAccountId() throws Exception {
        validator.validateGetProgress(null, "935e51fe-bc77-432f-b412-3800e3c04e90","62087a7f-4215-4bfc-b3ee-cc12f085efc5");
    }
}