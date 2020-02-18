package com.hiring.rbs.banking.validator;

import com.hiring.rbs.banking.TestUtility;
import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.data.TransactionType;
import com.hiring.rbs.banking.dataStore.CoreDetails;
import com.hiring.rbs.banking.dataStore.CoreTransactions;
import com.hiring.rbs.banking.exception.ActiveAccountException;
import com.hiring.rbs.banking.exception.InactiveAccountException;
import com.hiring.rbs.banking.exception.InsufficientFundsException;
import com.hiring.rbs.banking.exception.NoDataFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class TransactionValidatorTest {
    private TransactionValidator txnValidator;
    private CoreDetails coreDetails;
    private CoreTransactions coreTransactions;

    @Before
    public void setUp() throws Exception {
        txnValidator = new TransactionValidator();
        coreDetails = mock(CoreDetails.class);
        coreTransactions = mock(CoreTransactions.class);
        txnValidator.setCoreDetails(coreDetails);
        txnValidator.setCoreTransactions(coreTransactions);
    }

    @Test(expected = NoDataFoundException.class)
    public void testVerifyFundIsAvailableAccountNotAvailableException() {
        final TransactionRequest txnRequest = mock(TransactionRequest.class);
        when(txnRequest.getAccountNumber()).thenReturn("123");
        when(coreDetails.findUserByAccountId(anyString())).thenReturn(null);
        txnValidator.verifyFundIsAvailable(20.0, txnRequest);
    }

    @Test(expected = InsufficientFundsException.class)
    public void testVerifyFundIsAvailableInsufficientFunds() {
        final TransactionRequest txnRequest = mock(TransactionRequest.class);
        final Transaction txn = mock(Transaction.class);
        when(txnRequest.getAccountNumber()).thenReturn("123");
        when(txnRequest.getTransaction()).thenReturn(txn);
        when(txn.getAmount()).thenReturn(35.0);
        when(coreDetails.findUserByAccountId(anyString())).thenReturn(mock(RbsCustomer.class));
        txnValidator.verifyFundIsAvailable(20.0, txnRequest);
    }

    @Test
    public void testVerifyFundIsAvailableSucess() {
        final TransactionRequest txnRequest = mock(TransactionRequest.class);
        final Transaction txn = mock(Transaction.class);
        when(txnRequest.getAccountNumber()).thenReturn("123");
        when(txnRequest.getTransaction()).thenReturn(txn);
        when(txn.getAmount()).thenReturn(10.0);
        when(coreDetails.findUserByAccountId(anyString())).thenReturn(mock(RbsCustomer.class));
        Assertions.assertThatCode(() -> txnValidator.verifyFundIsAvailable(20.0, txnRequest)).doesNotThrowAnyException();
    }

    @Test(expected = InactiveAccountException.class)
    public void testHasAnyTransactionInactiveAccount() {
        when(coreTransactions.getExistingUserTxn(anyString())).thenReturn(new ArrayList<>());
        when(coreDetails.findUserByAccountId(anyString())).thenReturn(mock(RbsCustomer.class));
        txnValidator.hasAnyTransactions("123");
    }

    @Test
    public void testHasAnyTransaction() {
        when(coreTransactions.getExistingUserTxn(anyString())).thenReturn(Arrays.asList(
                TestUtility.createTxn(TransactionType.CREDIT, 20.0)
        ));
        when(coreDetails.findUserByAccountId(anyString())).thenReturn(mock(RbsCustomer.class));
        Assertions.assertThatCode(() -> txnValidator.hasAnyTransactions("123")).doesNotThrowAnyException();
    }

    @Test(expected = ActiveAccountException.class)
    public void testPreCheckAccountDeleteThrowsActiveAccountException() {
        when(coreTransactions.getAccountBalance(anyString())).thenReturn(20.0);
        final RbsCustomer rbsCustomer = mock(RbsCustomer.class);
        when(rbsCustomer.getAccountNumber()).thenReturn("123");
        txnValidator.preCheckAccountDelete(rbsCustomer);
    }

    @Test
    public void testPrecheckAccountDeleteSuccess() {
        when(coreTransactions.getAccountBalance(anyString())).thenReturn(0.0);
        final RbsCustomer rbsCustomer = mock(RbsCustomer.class);
        when(rbsCustomer.getAccountNumber()).thenReturn("123");
        Assertions.assertThatCode(() -> txnValidator.preCheckAccountDelete(rbsCustomer)).doesNotThrowAnyException();
    }
}
