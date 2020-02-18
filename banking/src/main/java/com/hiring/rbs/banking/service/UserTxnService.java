package com.hiring.rbs.banking.service;

import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.exception.InactiveAccountException;
import com.hiring.rbs.banking.exception.InsufficientFundsException;
import com.hiring.rbs.banking.dao.UserTxn;
import com.hiring.rbs.banking.exception.NoDataFoundException;
import com.hiring.rbs.banking.validator.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTxnService {
    private Logger LOG = LoggerFactory.getLogger(UserTxnService.class);
    private UserTxn userTxnImpl;
    private TransactionValidator transactionValidator;
    private InterestCalcService interestCalcService;

    @Autowired
    public void setUserTxnImpl(final UserTxn userTxnImpl) {
        this.userTxnImpl = userTxnImpl;
    }

    @Autowired
    public void setTransactionValidator(final TransactionValidator transactionValidator) {
        this.transactionValidator = transactionValidator;
    }

    @Autowired
    public void setInterestCalcService(final InterestCalcService interestCalcService) {
        this.interestCalcService = interestCalcService;
    }

    public List<Transaction> getTransaction(final String acctNumber, final Integer txnCount) throws NoDataFoundException, InactiveAccountException {
        transactionValidator.hasAnyTransactions(acctNumber);
        return userTxnImpl.getTransaction(acctNumber, txnCount);
    }

    public String logTransaction(final TransactionRequest transactionRequest) throws NoDataFoundException {
        transactionValidator.validateAccount(transactionRequest.getAccountNumber());
        userTxnImpl.addTransaction(transactionRequest);
        interestCalcService.addInterestIfValid(transactionRequest);
        final String responseMsg = new StringBuilder("Amount ")
                .append(transactionRequest.getTransaction().getAmount())
                .append(" processed for transaction type ")
                .append(transactionRequest.getTransaction().getTxnType().name())
                .append(" against account ")
                .append(transactionRequest.getAccountNumber())
                .append(" and balance updated").toString();
        return responseMsg;
    }

    public String withDrawAmount(final TransactionRequest withDrawalRequest) throws NoDataFoundException, InsufficientFundsException {
        transactionValidator.verifyFundIsAvailable(getCurrentBalance(withDrawalRequest.getAccountNumber()), withDrawalRequest);
        LOG.info("Funds Available. Progressing with withdrawal for {}", withDrawalRequest.getAccountNumber());
        return logTransaction(withDrawalRequest);
    }

    public Double getCurrentBalance(final String acctNumber) throws NoDataFoundException {
        transactionValidator.validateAccount(acctNumber);
        return userTxnImpl.getCurrentBalance(acctNumber);
    }
}
