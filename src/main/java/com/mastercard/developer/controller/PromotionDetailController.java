
package com.mastercard.developer.controller;

import com.mastercard.developer.exception.EntityNotFoundException;
import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.OptInService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.PromotionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "", produces = {"application/json"})
public class PromotionDetailController {

    private OptInService optInService;


    @Autowired
    public PromotionDetailController(OptInService optInService) {
        this.optInService = optInService;
    }

    /**
    * Build call for getPromotionDetails
    *
    * @param promotionId      Promotion Identifier (required)
    * @return Call to execute
    * @throws InvalidRequest If fail to validate input values and serialize the request body object
    */
    @GetMapping(value = "/promotions/{id}/details")
    public PromotionDetail getPromotionDetails(@PathVariable(value = "id") String promotionId)  {
        try {
            log.info("Method : getPromotionDetails, Message : Getting promotions");
            PromotionDetail response = optInService.getPromotionDetail(promotionId);
            if (response != null) {
                log.debug("Method : getPromotionDetails, Message :Successfully got the promotion details" + response);
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : getPromotionDetails, Message : Failed to get the promotions");

            if (HttpStatus.NOT_FOUND.value() == ex.getCode()) {
                throw new EntityNotFoundException(ex.getResponseBody());
            } else {
                throw new InvalidRequest(ex.getResponseBody());
            }
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }
}

