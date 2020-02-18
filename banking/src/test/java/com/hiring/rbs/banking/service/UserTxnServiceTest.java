package com.hiring.rbs.banking.service;

import com.hiring.rbs.banking.dao.UserTxn;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.data.TransactionType;
import com.hiring.rbs.banking.exception.InactiveAccountException;
import com.hiring.rbs.banking.exception.InsufficientFundsException;
import com.hiring.rbs.banking.exception.NoDataFoundException;
import com.hiring.rbs.banking.validator.TransactionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserTxnServiceTest {
    private UserTxnService userTxnService;
    private UserTxn userTxnImpl;
    private TransactionValidator transactionValidator;
    private InterestCalcService interestCalcService;

    @Before
    public void setUp() throws Exception {
        userTxnService = new UserTxnService();
        userTxnImpl = mock(UserTxn.class);
        transactionValidator = mock(TransactionValidator.class);
        interestCalcService = mock(InterestCalcService.class);
        userTxnService.setUserTxnImpl(userTxnImpl);
        userTxnService.setTransactionValidator(transactionValidator);
        userTxnService.setInterestCalcService(interestCalcService);
    }

    @Test(expected = NoDataFoundException.class)
    public void testGetTransactionsAccountDoestnExist() {
        Mockito.doThrow(new NoDataFoundException("no data found Exception")).when(transactionValidator).hasAnyTransactions(anyString());
        userTxnService.getTransaction("1234", 10);
    }

    @Test(expected = InactiveAccountException.class)
    public void testGetTransactionsNoTransaction() {
        Mockito.doThrow(new InactiveAccountException("Account live but has no single transaction")).when(transactionValidator).hasAnyTransactions(anyString());
        userTxnService.getTransaction("1234", 10);
    }

    @Test
    public void testGetTransaction() {
        Mockito.doNothing().when(transactionValidator).hasAnyTransactions(anyString());
        when(userTxnImpl.getTransaction(anyString(), anyInt())).thenReturn(Arrays.asList(new Transaction(), new Transaction()));
        assertEquals(2, userTxnService.getTransaction("1234", 10).size());
    }

    @Test(expected = NoDataFoundException.class)
    public void testLogTransactionAccountDoesntExist() {
        final TransactionRequest transactionRequest = mock(TransactionRequest.class);
        when(transactionRequest.getAccountNumber()).thenReturn("123");
        Mockito.doThrow(new NoDataFoundException("no data found Exception")).when(transactionValidator).validateAccount(anyString());
        userTxnService.logTransaction(transactionRequest);
    }

    @Test
    public void testLogTransaction() {
        final TransactionRequest transactionRequest = mock(TransactionRequest.class);
        final Transaction transaction = mock(Transaction.class);
        when(transactionRequest.getAccountNumber()).thenReturn("123");
        when(transactionRequest.getTransaction()).thenReturn(transaction);
        when(transaction.getAmount()).thenReturn(20.0);
        when(transaction.getTxnType()).thenReturn(TransactionType.CREDIT);
        Mockito.doNothing().when(transactionValidator).validateAccount(anyString());
        Mockito.doNothing().when(userTxnImpl).addTransaction(any(TransactionRequest.class));
        Mockito.doNothing().when(interestCalcService).addInterestIfValid(any(TransactionRequest.class));
        assertTrue(userTxnService.logTransaction(transactionRequest).contains("balance updated"));
    }

    @Test(expected = NoDataFoundException.class)
    public void testWithdrawUnknownAccount() {
        Mockito.doThrow(new NoDataFoundException("Account doesnt Exist")).when(transactionValidator).verifyFundIsAvailable(anyDouble(), any(TransactionRequest.class));
        userTxnService.withDrawAmount(new TransactionRequest());
    }

    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawInsufficientFund() {
        Mockito.doThrow(new InsufficientFundsException("Insufficient fund")).when(transactionValidator).verifyFundIsAvailable(anyDouble(), any(TransactionRequest.class));
        userTxnService.withDrawAmount(new TransactionRequest());
    }

    @Test
    public void testWithdrawTransaction() {
        final TransactionRequest transactionRequest = mock(TransactionRequest.class);
        final Transaction transaction = mock(Transaction.class);
        when(transactionRequest.getAccountNumber()).thenReturn("123");
        when(transactionRequest.getTransaction()).thenReturn(transaction);
        when(transaction.getAmount()).thenReturn(20.0);
        when(transaction.getTxnType()).thenReturn(TransactionType.CREDIT);
        Mockito.doNothing().when(transactionValidator).verifyFundIsAvailable(anyDouble(), any(TransactionRequest.class));
        assertTrue(userTxnService.withDrawAmount(transactionRequest).contains("balance updated"));
    }

    @Test(expected = NoDataFoundException.class)
    public void testBalanceInvalidAccount() {
        Mockito.doThrow(new NoDataFoundException("Invalid account")).when(transactionValidator).validateAccount(anyString());
        userTxnService.getCurrentBalance("123");
    }

    @Test
    public void testGetBalance() {
        Mockito.doNothing().when(transactionValidator).validateAccount(anyString());
        when(userTxnImpl.getCurrentBalance(anyString())).thenReturn(20.0);
        assertEquals(0, Double.compare(20.0, userTxnService.getCurrentBalance("123")));
    }
}
