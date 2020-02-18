package com.hiring.rbs.banking.controller;

import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.exception.InactiveAccountException;
import com.hiring.rbs.banking.exception.InsufficientFundsException;
import com.hiring.rbs.banking.exception.NoDataFoundException;
import com.hiring.rbs.banking.service.UserTxnService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserActionTest {
    private UserAction userAction;
    private UserTxnService userTxnService;
    private TransactionRequest txnRequest;
    private Transaction transaction;

    @Before
    public void setUp() throws Exception {
        userAction = new UserAction();
        userTxnService = mock(UserTxnService.class);
        transaction = mock(Transaction.class);
        userAction.setUserTxnService(userTxnService);
        txnRequest = new TransactionRequest();
    }

    @Test
    public void testWithdrawal() {
        txnRequest.setAccountNumber("1234");
        txnRequest.setTransaction(transaction);
        when(transaction.getAmount()).thenReturn(20.0);
        when(userTxnService.withDrawAmount(any(TransactionRequest.class))).thenReturn("Amount Withdrawn");
        final ResponseEntity<String> responseEntity = userAction.withdrawAmount(txnRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Amount Withdrawn", responseEntity.getBody());
    }

    @Test
    public void testZeroWithdrawal() {
        txnRequest.setAccountNumber("1234");
        txnRequest.setTransaction(transaction);
        when(transaction.getAmount()).thenReturn(0.0);
        final ResponseEntity<String> responseEntity = userAction.withdrawAmount(txnRequest);
        assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
        assertEquals("Transaction amount cannot be zero", responseEntity.getBody());
    }

    @Test
    public void testAccountDoestExist() {
        txnRequest.setAccountNumber("1234");
        txnRequest.setTransaction(transaction);
        when(transaction.getAmount()).thenReturn(20.0);
        when(userTxnService.withDrawAmount(any(TransactionRequest.class))).thenThrow(new NoDataFoundException("Account doesnt exist"));
        final ResponseEntity<String> responseEntity = userAction.withdrawAmount(txnRequest);
        assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
        assertEquals("Account doesnt exist", responseEntity.getBody());
    }

    @Test
    public void testInsufficientFunds() {
        txnRequest.setAccountNumber("1234");
        txnRequest.setTransaction(transaction);
        when(transaction.getAmount()).thenReturn(20.0);
        when(userTxnService.withDrawAmount(any(TransactionRequest.class))).thenThrow(new InsufficientFundsException("Insufficient funds"));
        final ResponseEntity<String> responseEntity = userAction.withdrawAmount(txnRequest);
        assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
        assertEquals("Insufficient funds", responseEntity.getBody());
    }

    @Test
    public void testDepositZeroAmount() {
        txnRequest.setAccountNumber("1234");
        txnRequest.setTransaction(transaction);
        when(transaction.getAmount()).thenReturn(0.0);
        final ResponseEntity<String> responseEntity = userAction.depositAmount(txnRequest);
        assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
        assertEquals("Transaction amount cannot be zero", responseEntity.getBody());
    }

    @Test
    public void testDepositOnAccountDoesntExist() {
        txnRequest.setAccountNumber("1234");
        txnRequest.setTransaction(transaction);
        when(transaction.getAmount()).thenReturn(20.0);
        when(userTxnService.logTransaction(any(TransactionRequest.class))).thenThrow(new NoDataFoundException("Account doesnt exist"));
        final ResponseEntity<String> responseEntity = userAction.depositAmount(txnRequest);
        assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
        assertEquals("Account doesnt exist", responseEntity.getBody());
    }

    @Test
    public void testDeposit() {
        txnRequest.setAccountNumber("1234");
        txnRequest.setTransaction(transaction);
        when(transaction.getAmount()).thenReturn(20.0);
        when(userTxnService.logTransaction(any(TransactionRequest.class))).thenReturn("Amount Deposited");
        final ResponseEntity<String> responseEntity = userAction.depositAmount(txnRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Amount Deposited", responseEntity.getBody());
    }

    @Test
    public void testGetLastTxn() {
        when(userTxnService.getTransaction(anyString(), anyInt())).thenReturn(new ArrayList<>());
        final ResponseEntity<Object> responseEntity = userAction.getLastTwentyTransactions("1234");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetTxnsAccountdoesntExist() {
        when(userTxnService.getTransaction(anyString(), anyInt())).thenThrow(new NoDataFoundException("Account doest exist"));
        final ResponseEntity<Object> responseEntity = userAction.getLastTwentyTransactions("1234");
        assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
        assertEquals("Account doest exist", responseEntity.getBody());
    }

    @Test
    public void testGetTxnsInactiveAccount() {
        when(userTxnService.getTransaction(anyString(), anyInt())).thenThrow(new InactiveAccountException("Account is inactive"));
        final ResponseEntity<Object> responseEntity = userAction.getLastTwentyTransactions("1234");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Account is inactive", responseEntity.getBody());
    }

    @Test
    public void testCurrentBalanceAccountDoesntExist() {
        when(userTxnService.getCurrentBalance(anyString())).thenThrow(new NoDataFoundException("Account doest exist"));
        final ResponseEntity<String> responseEntity = userAction.getCurrentBalance("1234");
        assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
        assertEquals("Account doest exist", responseEntity.getBody());
    }

    @Test
    public void testCurrentBalance() {
        when(userTxnService.getCurrentBalance(anyString())).thenReturn(20.0);
        final ResponseEntity<String> responseEntity = userAction.getCurrentBalance("1234");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("20.0", responseEntity.getBody());
    }
}
