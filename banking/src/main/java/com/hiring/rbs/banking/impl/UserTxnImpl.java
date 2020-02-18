package com.hiring.rbs.banking.impl;

import com.hiring.rbs.banking.dao.UserTxn;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.dataStore.CoreTransactions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTxnImpl implements UserTxn {
    private Logger LOG = LoggerFactory.getLogger(UserTxnImpl.class);
    private CoreTransactions coreTransactions;

    @Autowired
    public void setCoreTransactions(final CoreTransactions coreTransactions) {
        this.coreTransactions = coreTransactions;
    }

    @Override
    public List<Transaction> getTransaction(final String acctNumber, final Integer txnCount) {
        return coreTransactions.getUserTxn(acctNumber, txnCount);
    }

    @Override
    public void addTransaction(final TransactionRequest transactionRequest) {
        coreTransactions.addTransaction(transactionRequest);
    }

    @Override
    public Double getCurrentBalance(final String acctNumber) {
        return coreTransactions.getAccountBalance(acctNumber);
    }


}
