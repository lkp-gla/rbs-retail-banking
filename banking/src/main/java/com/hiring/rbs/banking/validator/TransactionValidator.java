package com.hiring.rbs.banking.validator;

import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.dataStore.CoreDetails;
import com.hiring.rbs.banking.dataStore.CoreTransactions;
import com.hiring.rbs.banking.exception.*;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class TransactionValidator {
    private static Logger LOG = LoggerFactory.getLogger(TransactionValidator.class);
    private CoreTransactions coreTransactions;
    private CoreDetails coreDetails;

    @Autowired
    public void setCoreTransactions(final CoreTransactions coreTransactions) {
        this.coreTransactions = coreTransactions;
    }

    @Autowired
    public void setCoreDetails(final CoreDetails coreDetails) {
        this.coreDetails = coreDetails;
    }

    public static void prevalidateRequest(final TransactionRequest transactionRequest) throws InvalidAmountException {
        if (Double.compare(0.0, transactionRequest.getTransaction().getAmount()) == 0) {
            throw new InvalidAmountException("Transaction amount cannot be zero");
        }
    }

    public void verifyFundIsAvailable(final Double currentBalance, final TransactionRequest withDrawalRequest) throws InsufficientFundsException, NoDataFoundException {
        validateAccount(withDrawalRequest.getAccountNumber());
        if (currentBalance < withDrawalRequest.getTransaction().getAmount()) {
            final String errMsg = new StringBuilder("Account ")
                    .append(withDrawalRequest.getAccountNumber())
                    .append(" has low funds to process this Transaction Request.").toString();
            LOG.error("Throwing error {}", errMsg);
            throw new InsufficientFundsException(errMsg);
        }
    }

    public void validateAccount(final String accountNumber) throws NoDataFoundException {
        final RbsCustomer customer = coreDetails.findUserByAccountId(accountNumber);
        if (ObjectUtils.isEmpty(customer)) {
            final String errMsg = new StringBuilder(accountNumber)
                    .append(" does not exist").toString();
            LOG.error("Throwing error {}", errMsg);
            throw new NoDataFoundException(errMsg);
        }
    }

    public void hasAnyTransactions(final String accountNumber) throws NoDataFoundException, InactiveAccountException {
        validateAccount(accountNumber);
        final List<Transaction> allTransactions = coreTransactions.getExistingUserTxn(accountNumber);
        if (CollectionUtils.isEmpty(allTransactions)) {
            final String errMsg = new StringBuilder("Account ")
                    .append(accountNumber)
                    .append(" has no transactions").toString();
            LOG.error("Throwing error {}", errMsg);
            throw new InactiveAccountException(errMsg);
        }
    }

    public void preCheckAccountDelete(final RbsCustomer customer) throws ActiveAccountException {
        final Double balance = coreTransactions.getAccountBalance(customer.getAccountNumber());
        if (Double.compare(0.0, balance) != 0) {
            final String errMsg = new StringBuilder("Account ")
                    .append(customer.getAccountNumber())
                    .append(" has balance ")
                    .append(balance)
                    .append(". Account cannot be deleted").toString();
            LOG.error("Throwing error {}", errMsg);
            throw new ActiveAccountException(errMsg);
        }
    }
}
