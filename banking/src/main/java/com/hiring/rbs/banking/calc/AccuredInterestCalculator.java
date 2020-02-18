package com.hiring.rbs.banking.calc;

import com.hiring.rbs.banking.dao.UserTxn;
import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class AccuredInterestCalculator {
    private String accountNumber;
    private Double balance;
    private String tenure;
    private String interestRate;

    @Value("${retail.rbs.interest.rate:#{null}}")
    public void setInterestRate(final String interestRate) {
        this.interestRate = interestRate;
    }

    public AccuredInterestCalculator() {
    }

    public AccuredInterestCalculator(final String accountNumber, final Double balance, final String tenure) {
        this.balance = balance;
        this.tenure = tenure;
        this.accountNumber = accountNumber;
    }

    public Transaction interestTxn() {
        final Transaction transaction;
        transaction = new Transaction();
        transaction.setTxnType(TransactionType.INTEREST);
        transaction.setDate(new Date());
        transaction.setTxnName("Interest after last ten transaction");
        transaction.setReferenceNo("Interest for " + accountNumber);
        transaction.setAmount(getInterest());
        return transaction;
    }

    private Double getInterest() {
        final Integer term = tenure == null ? 1 : Integer.parseInt(tenure);
        final Float interestRt = interestRate == null ? 1 : Float.parseFloat(interestRate);
        return (balance * term * interestRt) / 100;
    }
}
