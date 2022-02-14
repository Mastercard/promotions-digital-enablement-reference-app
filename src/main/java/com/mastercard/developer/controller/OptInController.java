package com.mastercard.developer.controller;

import com.mastercard.developer.exception.EntityNotFoundException;
import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.OptInService;
import com.mastercard.developer.validator.OptInValidator;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.OptIn;
import org.openapitools.client.model.Promotions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "", produces = {"application/json"})
public class OptInController {

    OptInValidator optInValidator;
    private OptInService optInService;

    @Autowired
    public OptInController(OptInValidator optInValidator, OptInService optInService) {
        this.optInValidator = optInValidator;
        this.optInService = optInService;
    }

    /*---------------------------------------------------------------- Post PromotionOptIn API -------------------------------------------------------*/

    /**
     * promotionOptIn API Call
     * Post promotionOptIn for Cardholder.
     *
     * @param optInCreateValueBeanList request (required)
     * @return ApiResponse;
     * @throws InvalidRequest If fail to parse input payload and fails to validate input values
     */

    @PostMapping(consumes = {"application/json"}, value = {"/promotion-activations"})
    public ResponseEntity<Void> promotionOptIn(@RequestBody List<OptIn> optInCreateValueBeanList) {
        optInValidator.validateCreate(optInCreateValueBeanList);
        try {
            log.info("Method : promotionOptIn, Message : Opting into promotions");
            optInService.optIn(optInCreateValueBeanList);
            log.debug("Method : postAuthorizations, Message :Successfully opted in to promotion");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ApiException ex) {
            log.error("Method : promotionOptIn, Message : Failed to opt in to promotion");
            throw new InvalidRequest(ex.getResponseBody());
        }
    }

    /*---------------------------------------------------------------- GET ActivePromotion API -------------------------------------------------------*/

    /**
     * Build call for getActivePromotion
     *
     * @param rewardsCompanyId Rewards Company Identifier (required)
     * @param programId        Program Identifier (required)
     * @param accountId        Cardholder Identifier such as BCN, BAN (optional)
     * @param promotionState   Promotion State such as all, current, future, expired, future+current (optional)
     * @param offset           The number of items to offset the start of the list from (optional, default to 0)
     * @param limit            The number of items you want the list to be limited to (optional, default to 25)
     * @return Call to execute
     * @throws InvalidRequest If fail to validate input values and serialize the request body object
     */
    @GetMapping(value = "/promotions")
    public Promotions getPromotions(@RequestParam(name = "rewardsCompanyId", required = false) String rewardsCompanyId, @RequestParam(name = "programId", required = false) String programId, @RequestParam(name = "accountId", required = false) String accountId, @RequestParam(name = "promotion_state", required = false) String promotionState, @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset, @RequestParam(name = "limit", required = false, defaultValue = "25") Integer limit) {
        optInValidator.validateGetPromotions(rewardsCompanyId, programId, accountId);
        try {
            log.info("Method : getActivePromotions, Message : Getting promotions");
            Promotions response = optInService.getActivePromotions(rewardsCompanyId, programId, accountId, promotionState, offset, limit);
            if (response != null) {
                log.debug("Method : getPromotions, Message :Successfully got the promotions" + response);
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : getPromotions, Message : Failed to get the promotions");

            if (HttpStatus.NOT_FOUND.value() == ex.getCode()) {
                throw new EntityNotFoundException(ex.getResponseBody());
            } else {
                throw new InvalidRequest(ex.getResponseBody());
            }
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }
}
