package com.hiring.rbs.banking.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class InsufficientFundsExceptionTest {
    private InsufficientFundsException insufficientFundsException;

    @Test
    public void testErrMsg() {
        insufficientFundsException = new InsufficientFundsException();
        assertTrue(insufficientFundsException instanceof RuntimeException);

        insufficientFundsException = new InsufficientFundsException("errMsg");
        assertEquals("errMsg", insufficientFundsException.getMessage());

        insufficientFundsException = new InsufficientFundsException("SecondErrMsg", mock(Throwable.class));
        assertEquals("SecondErrMsg", insufficientFundsException.getMessage());
    }
}
