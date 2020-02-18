package com.hiring.rbs.banking.dataStore;

import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.data.TransactionType;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a dummy class to store user transaction details in JVM.
 * It is just a data store for all operations we do via api
 */
@Scope("singleton")
@Component
public class CoreTransactions {

    private Logger LOG = LoggerFactory.getLogger(CoreTransactions.class);
    private Map<String, List<Transaction>> userTransaction = new HashMap<>();
    private Map<String, Double> userBalance = new HashMap<>();

    public void addTransaction(final TransactionRequest transactionRequest) {
        addTxnToCoreTransaction(transactionRequest);
    }

    public List<Transaction> getUserTxn(final String accountNumber, final Integer txnCount) {
        final List<Transaction> allUserTxn = getExistingUserTxn(accountNumber);
        final List<Transaction> lastTransactions;
        if (!CollectionUtils.isEmpty(allUserTxn)) {
            lastTransactions = allUserTxn.size() < txnCount ? allUserTxn : allUserTxn.subList(allUserTxn.size() - txnCount, allUserTxn.size());
        } else {
            lastTransactions = new ArrayList<>();
        }
        return lastTransactions;
    }

    public List<Transaction> getExistingUserTxn(final String accountNumber) {
        return userTransaction.get(accountNumber);
    }

    public void addTxnToCoreTransaction(final TransactionRequest transactionRequest) {
        final List<Transaction> existingTxn = getExistingUserTxn(transactionRequest.getAccountNumber());
        synchronized (userTransaction) {
            if (CollectionUtils.isEmpty(existingTxn)) {
                LOG.info("First Txn added {}/{}", transactionRequest.getAccountNumber(), transactionRequest.getTransaction());
                userTransaction.put(transactionRequest.getAccountNumber(), Arrays.asList(transactionRequest.getTransaction()));
            } else {
                LOG.info("Additional Txn added {}/{}", transactionRequest.getAccountNumber(), transactionRequest.getTransaction());
                final List<Transaction> revisedTransactions = new ArrayList<>();
                Stream.of(existingTxn,Arrays.asList(transactionRequest.getTransaction())).forEach(revisedTransactions::addAll);
                userTransaction.replace(transactionRequest.getAccountNumber(), revisedTransactions);
            }
            hydrateUserBalance(transactionRequest.getAccountNumber());
        }
    }

    private void hydrateUserBalance(final String accountNumber) {
        LOG.info("Hydrating account Balance {}", accountNumber);
        final Double creditAmount = ObjectUtils.defaultIfNull(getTotalAmount(filterUserTxn(accountNumber, TransactionType.CREDIT)), 0.0);
        final Double debitAmount = ObjectUtils.defaultIfNull(getTotalAmount(filterUserTxn(accountNumber, TransactionType.DEBIT)), 0.0);
        final Double intAmount = ObjectUtils.defaultIfNull(getTotalAmount(filterUserTxn(accountNumber, TransactionType.INTEREST)), 0.0);
        final Double netAmount = creditAmount + intAmount - debitAmount;
        LOG.info("Hydrating netAmount {} / {}", accountNumber, netAmount);
        synchronized (userBalance) {
            if (userBalance.containsKey(accountNumber)) {
                LOG.info("Balance updated");
                userBalance.replace(accountNumber, netAmount);
            } else {
                LOG.info("Balance entered");
                userBalance.put(accountNumber, netAmount);
            }
        }
    }

    private List<Transaction> filterUserTxn(final String accountNumber, final TransactionType txnType) {
        return userTransaction.get(accountNumber)
                .stream()
                .filter(transaction -> txnType.name().equalsIgnoreCase(transaction.getTxnType().name()))
                .collect(Collectors.toList());
    }

    private Double getTotalAmount(final List<Transaction> transactions) {
        Double totalAmount = 0.0;
        if (!CollectionUtils.isEmpty(transactions)) {
            for (final Transaction transaction : transactions) {
                totalAmount += transaction.getAmount();
            }
        }
        return totalAmount;
    }

    public Double getAccountBalance(final String accountNumber) {
        synchronized (userBalance) {
            LOG.info("Getting UserBalance for account {}", accountNumber);
            final Double accountBalance = ObjectUtils.defaultIfNull(userBalance.get(accountNumber), 0.0);
            LOG.info("Account Balance {}", accountBalance);
            return accountBalance;
        }
    }

    public Map<String, Double> readAllUserBalance(){
        return userBalance;
    }

    public Map<String, List<Transaction>> readAllTransaction(){
        return userTransaction;
    }
}
