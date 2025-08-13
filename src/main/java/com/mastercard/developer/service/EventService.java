package com.mastercard.developer.service;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.EventsApi;
import org.openapitools.client.model.Event;
import org.openapitools.client.model.PagedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class EventService {

    private final EventsApi eventsApi;

    public EventService(ApiClient apiClient) {
        this.eventsApi = new EventsApi(apiClient);
    }

    public PagedEvent getEvents(String houseHoldId, String accountId, String fromDate, String toDate, String promotionId, Integer offset, Integer limit) throws ApiException {
        LocalDate localFromDate = (fromDate != null) ? LocalDate.parse(fromDate) : null;
        LocalDate localToDate = (toDate != null) ? LocalDate.parse(toDate) : null;
        UUID houseHoldUUID = (houseHoldId != null) ? UUID.fromString(houseHoldId) : null;
        UUID accountUUID = (accountId != null) ? UUID.fromString(accountId) : null;
        UUID promotionUUID = (promotionId != null) ? UUID.fromString(promotionId) : null;
        return eventsApi.getEvents(houseHoldId, accountId, fromDate, toDate, promotionId, offset, limit);
    }

    public void saveEvent(Event event) throws ApiException {
         eventsApi.createEvents(event);
    }

}
