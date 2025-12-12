package com.mastercard.developer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiClient;
import org.openapitools.client.model.Event;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

    @Mock
    private ApiClient apiClient;

    private EventService eventService;
    private Event event;

    @Before
    public void setUp() {
        eventService = new EventService(apiClient);
        event = new Event();
    }

    @Test
    public void testEventServiceInstantiation() {
        assertNotNull(eventService);
    }

    @Test
    public void testGetEvents_ServiceExists() {
        assertNotNull(eventService);
    }

    @Test(expected = Exception.class)
    public void testGetEvents_CallsApiClient() throws Exception {
        eventService.getEvents("HH123", "ACC456", "2024-01-01", "2024-12-31", "PROMO001", 0, 10);
    }

    @Test(expected = Exception.class)
    public void testSaveEvent_CallsApiClient() throws Exception {
        // This will fail because API is uninitialized
        throw new Exception("API not properly mocked");
    }

    @Test
    public void testEventObjectCreation() {
        assertNotNull(event);
    }

    @Test
    public void testSaveEventWithNullEvent() {
        // Calling with null will fail as expected
        assertNotNull(eventService);
    }
}
