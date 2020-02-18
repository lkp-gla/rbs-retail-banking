package com.hiring.rbs.banking.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class InvalidAmountExceptionTest {
    private InvalidAmountException invalidAmountException;

    @Test
    public void testErrMsg() {
        invalidAmountException = new InvalidAmountException();
        assertTrue(invalidAmountException instanceof RuntimeException);

        invalidAmountException = new InvalidAmountException("errMsg");
        assertEquals("errMsg", invalidAmountException.getMessage());

        invalidAmountException = new InvalidAmountException("SecondErrMsg", mock(Throwable.class));
        assertEquals("SecondErrMsg", invalidAmountException.getMessage());
    }
}
