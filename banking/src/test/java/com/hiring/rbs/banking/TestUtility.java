package com.hiring.rbs.banking;

import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionType;

public class TestUtility {

    public static Transaction createTxn(final TransactionType txnType, final Double amount){
        final Transaction txn = new Transaction();
        txn.setTxnType(txnType);
        txn.setAmount(amount);
        return txn;
    }
}
