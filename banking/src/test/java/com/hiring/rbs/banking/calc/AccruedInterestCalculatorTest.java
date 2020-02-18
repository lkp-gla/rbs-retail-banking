package com.hiring.rbs.banking.calc;

import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class AccruedInterestCalculatorTest {
    private AccuredInterestCalculator accuredInterestCalculator;

    @Before
    public void setUp() throws Exception {
        accuredInterestCalculator = new AccuredInterestCalculator();
        accuredInterestCalculator.setInterestRate("1");
    }

    @Test
    public void testInterestRate() {
        accuredInterestCalculator = new AccuredInterestCalculator("123",20.0,"1");
        final Transaction txn = accuredInterestCalculator.interestTxn();
        assertEquals(TransactionType.INTEREST, txn.getTxnType());
    }
}
