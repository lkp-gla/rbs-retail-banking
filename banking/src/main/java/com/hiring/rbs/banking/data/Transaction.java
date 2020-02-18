package com.hiring.rbs.banking.data;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    private static final long serialVersionUID = -7315751773421349130L;
    private Date date;
    private Double amount;
    private TransactionType txnType;
    private String referenceNo;
    private String txnName;

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public TransactionType getTxnType() {
        return txnType;
    }

    public void setTxnType(final TransactionType txnType) {
        this.txnType = txnType;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(final String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getTxnName() {
        return txnName;
    }

    public void setTxnName(final String txnName) {
        this.txnName = txnName;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date='" + date + '\'' +
                ", amount=" + amount +
                ", txnType=" + txnType +
                ", referenceNo='" + referenceNo + '\'' +
                ", txnName='" + txnName + '\'' +
                '}';
    }
}
