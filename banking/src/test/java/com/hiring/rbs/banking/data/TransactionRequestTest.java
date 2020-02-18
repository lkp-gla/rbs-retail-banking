package com.hiring.rbs.banking.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class TransactionRequestTest {
    private TransactionRequest txnRequest;

    @Before
    public void setUp() throws Exception {
        txnRequest = new TransactionRequest();
        txnRequest.setTransaction(mock(Transaction.class));
        txnRequest.setAccountNumber("1234");
    }

    @Test
    public void testTxnData() {
        assertNotNull(txnRequest.toString());
        assertEquals("1234", txnRequest.getAccountNumber());
    }

}
