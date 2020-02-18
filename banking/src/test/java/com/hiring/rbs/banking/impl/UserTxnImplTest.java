package com.hiring.rbs.banking.impl;

import com.hiring.rbs.banking.TestUtility;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.data.TransactionType;
import com.hiring.rbs.banking.dataStore.CoreTransactions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserTxnImplTest {
    private UserTxnImpl userTxnImpl;
    private CoreTransactions coreTransactions;

    @Before
    public void setUp() throws Exception {
        userTxnImpl = new UserTxnImpl();
        coreTransactions = mock(CoreTransactions.class);
        userTxnImpl.setCoreTransactions(coreTransactions);
    }

    @Test
    public void testGetTransaction() {
        when(coreTransactions.getUserTxn(anyString(), anyInt())).thenReturn(
                Arrays.asList(
                        TestUtility.createTxn(TransactionType.CREDIT, 10.0)
                )
        );
        assertEquals(1, userTxnImpl.getTransaction("123", 10).size());
    }

    @Test
    public void testAddTxn() {
        Mockito.doNothing().when(coreTransactions).addTransaction(any(TransactionRequest.class));
        userTxnImpl.addTransaction(new TransactionRequest());
    }

    @Test
    public void testCurrentBalance() {
        when(coreTransactions.getAccountBalance(anyString())).thenReturn(20.0);
        assertEquals(0, Double.compare(20.0, userTxnImpl.getCurrentBalance("123")));
    }
}
