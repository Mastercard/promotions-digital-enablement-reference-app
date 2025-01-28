package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.AudienceService;
import com.mastercard.developer.validator.AudienceValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.Audience;
import org.openapitools.client.model.AudienceUpdate;
import org.openapitools.client.model.PagedResponseAudience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(
        value = "",
        produces = {"application/json"})
public class AudienceController {

    private final AudienceValidator audienceValidator;
    private final AudienceService audienceService;

    @Autowired
    public AudienceController(AudienceValidator audienceValidator, AudienceService audienceService) {
        this.audienceValidator = audienceValidator;
        this.audienceService = audienceService;
    }

    /*---------------------------------------------------------------- GET Audience API -------------------------------------------------------*/

    @GetMapping(path = "/audiences")
    public PagedResponseAudience getAudiences(
            @RequestParam(name = "entity_id") String entityId,
            @RequestParam(name = "entity_type") String entityType,
            @RequestParam(name = "code", required = false) String audienceCode,
            @RequestParam(name = "from_date_time", required = false) String fromDateTime,
            @RequestParam(name = "to_date_time", required = false) String toDateTime,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
        audienceValidator.validateAudienceGetDataRequest(fromDateTime, toDateTime, entityType, entityId);
        try {
            log.info("Method : getAudiences, Message : Getting Audiences");
            PagedResponseAudience response = audienceService.getAudiencePagedExternalTargetRecords(audienceCode, entityId, entityType, fromDateTime, toDateTime, offset, limit);
            if (response != null) {
                log.debug("Method : getAudiences, Message :Successfully got the Audiences" + response);
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : getAudiences, Message : Failed to get the Audiences");
            throw new InvalidRequest(ex.getResponseBody());
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }

    /*---------------------------------------------------------------- POST Audience API -------------------------------------------------------*/

    @PostMapping(
            consumes = {"application/json"},
            path = "/audiences")
    public Audience createAudience(@RequestBody Audience audience) {
        audienceValidator.validateAudienceCreate(audience);
        try {
            Audience response = audienceService.saveAudience(audience);
            if (response != null && response.getId() != null) {
                log.debug("Method : createAudience, Message :Successfully created the audience" + response);
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : createAudience, Message : Failed to created the audience");
            throw new InvalidRequest(ex.getResponseBody());
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }

    /*---------------------------------------------------------------- PUT Audience API -------------------------------------------------------*/

    @PutMapping(
            path = "/audiences/{id}",
            consumes = {"application/json"})
    public Audience updateAudience(
            @PathVariable(value = "id") String referenceId,
            @RequestBody AudienceUpdate audienceUpdate) {
        audienceValidator.validateAudienceUpdateData(audienceUpdate);
        try {
            Audience response = audienceService.updateAudience(referenceId, audienceUpdate);
            if (response != null && response.getId() != null) {
                log.debug("Method : updateAudience, Message :Successfully updated the audience" + response);
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : updateAudience, Message : Failed to update the audience");
            throw new InvalidRequest(ex.getResponseBody());
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }
}