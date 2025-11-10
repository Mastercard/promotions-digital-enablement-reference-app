package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.ProgressService;
import com.mastercard.developer.validator.ProgressValidator;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.PromotionProgressList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "", produces = {"application/json"})
public class ProgressController {

    private ProgressValidator progressValidator;
    private ProgressService progressService;

    public ProgressController(ProgressValidator progressValidator, ProgressService progressService) {
        this.progressValidator = progressValidator;
        this.progressService = progressService;
    }

    /*---------------------------------------------------------------- GET Progress API -------------------------------------------------------*/

    /**
     * @param householdId
     * @param accountId
     * @param userId
     * @param promotionID
     * @param includeHistory
     * @return
     */
    @GetMapping(value = "/promotion-progresses")
    public PromotionProgressList getProgress(@RequestParam(value = "household_id", required = false) String householdId,
                                             @RequestParam(value = "account_id", required = false) String accountId,
                                             @RequestParam(value = "user_id", required = false) String userId,
                                             @RequestParam(value = "promotion_id", required = false) String promotionID,
                                             @RequestParam(value = "include_history", required = false, defaultValue = "false") boolean includeHistory) {
        progressValidator.validateGetProgress(householdId, accountId);
        try {
            log.info("Method : getProgress, Message : Getting progress towards rewards");
            PromotionProgressList response = progressService.getProgress(householdId, accountId, userId, promotionID, includeHistory);
            if (response != null) {
                log.debug("Method : getProgress, Message :Successfully got the progress" + response);
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : getProgress, Message : Failed to get the getProgress");
            throw new InvalidRequest(ex.getResponseBody());
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }
}
