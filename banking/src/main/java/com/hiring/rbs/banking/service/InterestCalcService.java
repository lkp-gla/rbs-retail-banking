package com.hiring.rbs.banking.service;

import com.hiring.rbs.banking.calc.AccuredInterestCalculator;
import com.hiring.rbs.banking.dao.UserTxn;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class InterestCalcService {
    private Logger LOG = LoggerFactory.getLogger(InterestCalcService.class);
    private UserTxn userTxnImpl;
    private String tenure = "1";

    @Autowired
    public void setUserTxnImpl(final UserTxn userTxnImpl) {
        this.userTxnImpl = userTxnImpl;
    }

    @Value("${rbs.interest.tenure:#{null}}")
    public void setTenure(final String tenure) {
        this.tenure = tenure;
    }

    public void addInterestIfValid(final TransactionRequest transactionRequest) {
        final List<Transaction> transactions = userTxnImpl.getTransaction(transactionRequest.getAccountNumber(), 10);
        if (!CollectionUtils.isEmpty(transactions) && transactions.size() > 5) {
            LOG.info("Adding Accrued Interest for account {}", transactionRequest.getAccountNumber());
            final AccuredInterestCalculator accuredInterestCalculator = new AccuredInterestCalculator(
                    transactionRequest.getAccountNumber(),
                    userTxnImpl.getCurrentBalance(transactionRequest.getAccountNumber()),
                    tenure);
            addInterestTransaction(transactionRequest.getAccountNumber(), accuredInterestCalculator.interestTxn());
        }
    }

    private void addInterestTransaction(final String acctNumber, final Transaction interestTxn) {
        final TransactionRequest intTransactionRequest = new TransactionRequest();
        intTransactionRequest.setAccountNumber(acctNumber);
        intTransactionRequest.setTransaction(interestTxn);
        userTxnImpl.addTransaction(intTransactionRequest);
    }
}
