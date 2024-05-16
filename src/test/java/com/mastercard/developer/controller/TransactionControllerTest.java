package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.TransactionService;
import com.mastercard.developer.validator.TransactionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.PagedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    @InjectMocks
    TransactionController controller;

    @Mock
    TransactionService transactionService;

    @Mock
    TransactionValidator transactionValidator;

    @Autowired
    private MockMvc mockMvc;

    private static final String PROGRESS = "/loyalty/rewards/transactions";

    @Before
    public void setUp() {
        controller = spy(new TransactionController(transactionValidator, transactionService));
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    public void testGetTransactions_Success() throws Exception {
        when(transactionService.getTransactions(any(UUID.class), any(LocalDate.class), any(LocalDate.class),
                any(UUID.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new PagedTransaction());
        PagedTransaction response = controller.getTransactions(UUID.fromString("51fd0695-a467-4541-b6a7-172b03e01c71"),
                LocalDate.of(2020, 3, 10), LocalDate.of(2020, 5, 20),
                UUID.fromString("be6cb686-20cd-4941-822c-c767d59876f7"), 0, 1);
        assertNotNull(response);
    }

    @Test(expected = InvalidRequest.class)
    public void testGetTransactions_Exception() throws Exception {
        when(transactionService.getTransactions(any(UUID.class), any(LocalDate.class), any(LocalDate.class),
                any(UUID.class), any(Integer.class), any(Integer.class)))
                .thenThrow(new ApiException());
        controller.getTransactions(UUID.fromString("885ae1a8-832a-4aec-99c8-0b15110c0917"),
                LocalDate.of(2020, 3, 10),LocalDate.of(2020, 5, 20),
                UUID.fromString("02e41612-0319-4e25-92ed-154ca8e67765"), 0, 1);

    }
}
