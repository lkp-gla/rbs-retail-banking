package com.hiring.rbs.banking.exception;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class ActiveAccountExceptionTest {
    private ActiveAccountException activeAccountException;

    @Test
    public void testErrMsg() {
        activeAccountException = new ActiveAccountException();
        assertTrue(activeAccountException instanceof RuntimeException);

        activeAccountException = new ActiveAccountException("errMsg");
        assertEquals("errMsg",activeAccountException.getMessage());

        activeAccountException = new ActiveAccountException("SecondErrMsg", mock(Throwable.class));
        assertEquals("SecondErrMsg",activeAccountException.getMessage());
    }
}
