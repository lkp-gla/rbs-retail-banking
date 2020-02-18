package com.hiring.rbs.banking.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class InactiveAccountExceptionTest {
    private InactiveAccountException inactiveAccountException;

    @Test
    public void testErrMsg() {
        inactiveAccountException = new InactiveAccountException();
        assertTrue(inactiveAccountException instanceof RuntimeException);

        inactiveAccountException = new InactiveAccountException("errMsg");
        assertEquals("errMsg", inactiveAccountException.getMessage());

        inactiveAccountException = new InactiveAccountException("SecondErrMsg", mock(Throwable.class));
        assertEquals("SecondErrMsg", inactiveAccountException.getMessage());
    }
}
