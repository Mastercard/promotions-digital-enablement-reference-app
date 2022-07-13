package com.mastercard.developer.validator;

import com.mastercard.developer.exception.ErrorCodes;
import com.mastercard.developer.exception.InvalidRequest;
import org.openapitools.client.model.OptIn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class OptInValidator {
    public void validateCreate(List<OptIn> optIns) {
        List<String> messages = new ArrayList<>();
        for (OptIn optIn : optIns) {
            if (optIn.getActivation() == (null)) {
                messages.add("activation cannot be null");
            }
            if (messages.isEmpty() && !StringUtils.hasText(optIn.getPromotionId())) {
                messages.add("promotionId cannot be null");
            }
        }
        if (!messages.isEmpty()) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, messages.toString());
        }
    }

    public void validateGetPromotions(String rewardsCompanyId, String programReferenceId, String promotionId,  String userId) {
        List<String> messages = new ArrayList<>();
        if (!StringUtils.hasText(rewardsCompanyId) && !StringUtils.hasText(programReferenceId) && !StringUtils.hasText(promotionId)) {
            if (!StringUtils.hasText(userId)) {
                messages.add("Either the rewardsCompanyId or programId or promotionId or accountId must be provided");
            } else {
                messages.add("Either the rewardsCompanyId, programId or promotionId must be provided");
            }
        }
        if (!messages.isEmpty()) {
            throw new InvalidRequest(ErrorCodes.INVALID_INPUT.code, messages.toString());
        }
    }


}

