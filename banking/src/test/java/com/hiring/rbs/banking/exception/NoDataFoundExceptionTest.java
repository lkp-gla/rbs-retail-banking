package com.hiring.rbs.banking.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class NoDataFoundExceptionTest {
    private NoDataFoundException noDataFoundException;

    @Test
    public void testErrMsg() {
        noDataFoundException = new NoDataFoundException();
        assertTrue(noDataFoundException instanceof RuntimeException);

        noDataFoundException = new NoDataFoundException("errMsg");
        assertEquals("errMsg", noDataFoundException.getMessage());

        noDataFoundException = new NoDataFoundException("SecondErrMsg", mock(Throwable.class));
        assertEquals("SecondErrMsg", noDataFoundException.getMessage());
    }
}
