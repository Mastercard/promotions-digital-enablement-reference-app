package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.ProgressService;
import com.mastercard.developer.service.TransactionService;
import com.mastercard.developer.validator.ProgressValidator;
import com.mastercard.developer.validator.TransactionValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.assertEquals;
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
        when(transactionService.getTransactions(anyString(), any(String.class), any(String.class),
                anyString(), any(Integer.class),any(Integer.class) ))
                .thenReturn(new PagedResponseGetTransactionDto());
        PagedResponseGetTransactionDto response = controller.getTransactions(UUID.randomUUID().toString(), "2020-03-10",
                "2020-05-20", UUID.randomUUID().toString(), 0, 1);
        assertNotNull(response);
    }

    @Test (expected = InvalidRequest.class)
    public void testGetTransactions_Exception() throws Exception {
        when(transactionService.getTransactions(anyString(), any(String.class), any(String.class),
                anyString(), any(Integer.class),any(Integer.class) ))
                .thenThrow(new ApiException());
        controller.getTransactions(UUID.randomUUID().toString(), "2020-03-10",
                "2020-05-20", UUID.randomUUID().toString(), 0, 1);

    }
}
