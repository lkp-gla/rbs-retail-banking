package com.hiring.rbs.banking.dao;

import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;

import java.util.List;

public interface UserTxn {
    List<Transaction> getTransaction(final String acctNumber, final Integer txnCount);

    void addTransaction(final TransactionRequest currentTxn);

    Double getCurrentBalance(final String acctNumber);
}
