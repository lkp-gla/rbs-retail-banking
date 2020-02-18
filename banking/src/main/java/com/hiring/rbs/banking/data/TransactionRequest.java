package com.hiring.rbs.banking.data;

import java.io.Serializable;

public class TransactionRequest implements Serializable {

    private static final long serialVersionUID = -5480711370663799736L;
    private String accountNumber;
    private Transaction transaction;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(final Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "accountNumber='" + accountNumber + '\'' +
                ", transaction=" + transaction +
                '}';
    }
}
