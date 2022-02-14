package com.mastercard.developer.exception;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class EntityNotFoundExceptionTest {
    static final String EXCEPTION_MSG = RandomStringUtils.randomAlphanumeric(10);
    static EntityNotFoundException EXCEPTION = new EntityNotFoundException(EXCEPTION_MSG);

    @Test
    public void testException() {
        assertThat(EXCEPTION, is(notNullValue()));
        assertThat(EXCEPTION.getMessage(), is(EXCEPTION_MSG));
    }

    @Test
    public void testExceptionWithMessages() {
        EntityNotFoundException exception = new EntityNotFoundException(ErrorCodes.INVALID_INPUT.code, EXCEPTION_MSG);
        assertThat(exception, is(notNullValue()));
        assertThat(exception.getCode(), is(notNullValue()));
    }
}