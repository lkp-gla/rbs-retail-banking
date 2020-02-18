package com.hiring.rbs.banking.service;

import com.hiring.rbs.banking.TestUtility;
import com.hiring.rbs.banking.dao.UserTxn;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.data.TransactionType;
import com.hiring.rbs.banking.impl.UserTxnImpl;
import io.swagger.models.auth.In;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class InterestCalcServiceTest {
    private InterestCalcService interestCalcService;
    private UserTxn userTxnImpl;

    @Before
    public void setUp() throws Exception {
        interestCalcService = new InterestCalcService();
        userTxnImpl = mock(UserTxnImpl.class);
        interestCalcService.setUserTxnImpl(userTxnImpl);
        interestCalcService.setTenure("1");
    }

    @Test
    public void testInterestAdded() {
        final TransactionRequest transactionRequest = mock(TransactionRequest.class);
        when(userTxnImpl.getTransaction(anyString(), anyInt())).thenReturn(
                Arrays.asList(
                        TestUtility.createTxn(TransactionType.CREDIT,10.0),
                        TestUtility.createTxn(TransactionType.CREDIT,10.0),
                        TestUtility.createTxn(TransactionType.CREDIT,10.0),
                        TestUtility.createTxn(TransactionType.CREDIT,10.0),
                        TestUtility.createTxn(TransactionType.CREDIT,10.0),
                        TestUtility.createTxn(TransactionType.CREDIT,10.0)));
        when(userTxnImpl.getCurrentBalance(anyString())).thenReturn(60.0);
        when(transactionRequest.getAccountNumber()).thenReturn("123");
        interestCalcService.addInterestIfValid(transactionRequest);
        verify(userTxnImpl,times(1)).addTransaction(any(TransactionRequest.class));
    }

    @Test
    public void testInterestNotAdded() {
        final TransactionRequest transactionRequest = mock(TransactionRequest.class);
        when(userTxnImpl.getTransaction(anyString(), anyInt())).thenReturn(
                Arrays.asList(
                        TestUtility.createTxn(TransactionType.CREDIT,10.0),
                        TestUtility.createTxn(TransactionType.CREDIT,10.0),
                        TestUtility.createTxn(TransactionType.CREDIT,10.0)
                       ));
        when(userTxnImpl.getCurrentBalance(anyString())).thenReturn(60.0);
        when(transactionRequest.getAccountNumber()).thenReturn("123");
        interestCalcService.addInterestIfValid(transactionRequest);
        verify(userTxnImpl,never()).addTransaction(any(TransactionRequest.class));
    }

}
