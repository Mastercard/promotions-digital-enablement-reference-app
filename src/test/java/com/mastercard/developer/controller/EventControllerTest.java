package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.EventService;
import com.mastercard.developer.validator.EventValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.Event;
import org.openapitools.client.model.PagedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventControllerTest {

    @InjectMocks
    EventController controller;

    @Mock
    EventService eventService;

    @Mock
    EventValidator eventValidator;

    @Test
    public void testGetEvents_Success() throws Exception {
        PagedEvent pagedEvent = getPagedEvent();
        when(eventService.getEvents(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(pagedEvent);
        PagedEvent response = controller.getEvents(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                "2025-01-01", "2025-01-09", UUID.randomUUID().toString(), 0, 25);
        assertNotNull(response);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetEvents_Exception() throws Exception {
        when(eventService.getEvents(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new ApiException());
        controller.getEvents(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                "2025-01-01", "2025-01-09", UUID.randomUUID().toString(), 0, 25);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetEvents_NullResponse() throws Exception {
        when(eventService.getEvents(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(null);
        controller.getEvents(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                "2025-01-01", "2025-01-09", UUID.randomUUID().toString(), 0, 25);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetEvents_ValidatorException() throws Exception {
        doThrow(new InvalidRequest("INVALID_PARAM", "Invalid parameter"))
                .when(eventValidator).validateEvents(anyString(), anyString(), anyString());
        controller.getEvents(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                "2025-01-01", "2025-01-09", UUID.randomUUID().toString(), 0, 25);
    }

    @Test
    public void testCreateEvent_Success() throws Exception {
        Event event = getEventObject();
        doNothing().when(eventService).saveEvent(event);
        ResponseEntity<String> response = controller.createEvent(event);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }

    @Test(expected = InvalidRequest.class)
    public void testCreateEvent_Exception() throws Exception {
        Event event = getEventObject();
        doThrow(new ApiException()).when(eventService).saveEvent(event);
        controller.createEvent(event);
    }

    @Test(expected = InvalidRequest.class)
    public void testCreateEvent_ApiExceptionWithBody() throws Exception {
        Event event = getEventObject();
        doThrow(new ApiException(400, null, "Invalid event data"))
                .when(eventService).saveEvent(event);
        controller.createEvent(event);
    }

    @Test
    public void testGetEvents_WithPagination() throws Exception {
        PagedEvent pagedEvent = getPagedEvent();
        when(eventService.getEvents(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(pagedEvent);
        PagedEvent response = controller.getEvents(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                "2025-01-01", "2025-01-09", UUID.randomUUID().toString(), 50, 100);
        assertNotNull(response);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetEvents_DefaultValues() throws Exception {
        when(eventService.getEvents(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(null);
        controller.getEvents("HH123", "ACC456", "2025-01-01", "2025-01-09", "PROMO001", 0, 25);
    }

    private Event getEventObject() {
        Event event = new Event();
        return event;
    }

    private PagedEvent getPagedEvent() {
        PagedEvent pagedEvent = new PagedEvent();
        pagedEvent.setCount(1);
        return pagedEvent;
    }
}
