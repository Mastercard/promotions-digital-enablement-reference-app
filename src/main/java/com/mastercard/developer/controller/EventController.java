package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.EventService;
import com.mastercard.developer.validator.EventValidator;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.Event;
import org.openapitools.client.model.PagedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(
        value = "",
        produces = {"application/json"})
public class EventController {

    private final EventValidator eventValidator;
    private final EventService eventService;

    public EventController(EventValidator eventValidator, EventService eventService) {
        this.eventValidator = eventValidator;
        this.eventService = eventService;
    }

    /**
     * @param accountId
     * @param fromDate
     * @param toDate
     * @param promotionId
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping(value = "/events")
    public PagedEvent getEvents(@RequestParam(name = "household_id", required = false) String householdId,
                                            @RequestParam(name = "account_id", required = false) String accountId,
                                            @RequestParam(name = "from_date", required = false) String fromDate,
                                            @RequestParam(name = "to_date", required = false) String toDate,
                                            @RequestParam(name = "promotion_id", required = false) String promotionId,
                                            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                            @RequestParam(value = "limit", required = false, defaultValue = "25") int limit) {
        eventValidator.validateEvents(householdId, accountId, promotionId);
        try {
            log.info("Method : getEvents, Message : Getting events");
            PagedEvent response = eventService.getEvents(householdId, accountId, fromDate, toDate, promotionId, offset, limit);
            if (response != null) {
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : getEvents, Message : Failed to get the events");
            throw new InvalidRequest(ex.getResponseBody());
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(
            consumes = {"application/json"},
            path = "/events")
    public ResponseEntity<String> createEvent(@RequestBody Event event) {
        try {
            eventService.saveEvent(event);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ApiException ex) {
            log.error("Method : createEvent, Message : Failed to created the event");
            throw new InvalidRequest(ex.getResponseBody());
        }
    }
}
