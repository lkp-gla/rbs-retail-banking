package com.hiring.rbs.banking.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class TransactionTest {
    private Transaction transaction;

    @Before
    public void setUp() throws Exception {
        transaction = new Transaction();
        transaction.setAmount(20.0);
        transaction.setReferenceNo("refNo");
        transaction.setTxnName("txnName");
        transaction.setDate(new Date());
        transaction.setTxnType(TransactionType.INTEREST);
    }

    @Test
    public void testTransactionData() {
        assertEquals(0, Double.compare(20.0, transaction.getAmount()));
        assertEquals("refNo", transaction.getReferenceNo());
        assertEquals("txnName", transaction.getTxnName());
        assertEquals(TransactionType.INTEREST.name(), transaction.getTxnType().name());
        assertNotNull(transaction.toString());
    }
}
