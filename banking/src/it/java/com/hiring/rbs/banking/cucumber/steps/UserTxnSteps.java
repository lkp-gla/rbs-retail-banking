package com.hiring.rbs.banking.cucumber.steps;

import com.hiring.rbs.banking.AbstractSprintBootIntegrationTest;
import com.hiring.rbs.banking.controller.UserAction;
import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.data.TransactionType;
import com.hiring.rbs.banking.impl.UserInfoImpl;
import com.hiring.rbs.banking.impl.UserTxnImpl;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("integration")
public class UserTxnSteps extends AbstractSprintBootIntegrationTest {

    @Autowired
    private UserInfoImpl userInfo;

    @Autowired
    private UserTxnImpl userTxnImpl;

    @Autowired
    private UserAction userAction;

    private List<Transaction> transactions;

    @Given("^user account$")
    public void user_account(final DataTable usrTable) throws Throwable {
        for (final DataTableRow userDtRow : usrTable.getGherkinRows()) {
            final List<String> userDt = userDtRow.getCells();
            if (!"AccountNumber".equalsIgnoreCase(userDt.get(0))) {
                final RbsCustomer rbsCustomer = new RbsCustomer();
                rbsCustomer.setAccountNumber(userDt.get(0));
                rbsCustomer.setFirstName(userDt.get(1));
                rbsCustomer.setLastName(userDt.get(2));
                userInfo.addUser(rbsCustomer);
            }
        }
    }

    @When("^a deposit is made$")
    public void a_deposit_is_made(final DataTable txnTable) throws Throwable {
        for (final DataTableRow txnRow : txnTable.getGherkinRows()) {
            final List<String> txnDt = txnRow.getCells();
            if (!"AccountNumber".equalsIgnoreCase(txnDt.get(0))) {
                final TransactionRequest txnRequest = new TransactionRequest();
                txnRequest.setAccountNumber(txnDt.get(0));
                final Transaction transaction = new Transaction();
                transaction.setAmount(Double.parseDouble(txnDt.get(1)));
                TransactionType txnType = TransactionType.CREDIT;
                transaction.setDate(new Date());
                transaction.setTxnType(txnType);
                txnRequest.setTransaction(transaction);
                userAction.depositAmount(txnRequest);
            }
        }
    }

    @Then("^transaction is logged and balance updated$")
    public void transaction_is_logged_and_balance_updated(final DataTable txnTable) throws Throwable {
        for (final DataTableRow txnRow : txnTable.getGherkinRows()) {
            final List<String> txnDt = txnRow.getCells();
            if (!"HttpStatus".equalsIgnoreCase(txnDt.get(0))) {
                final ResponseEntity<String> responseEntity = userAction.getCurrentBalance(txnDt.get(1));
                assertEquals(txnDt.get(0), String.valueOf(responseEntity.getStatusCodeValue()));
                if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                    assertEquals(0, Double.compare(Double.parseDouble(txnDt.get(2)), Double.parseDouble(responseEntity.getBody())));
                }
            }
        }
    }

    @When("^withdrawal is made$")
    public void withdrawal_is_made(final DataTable txnTable) throws Throwable {
        for (final DataTableRow txnRow : txnTable.getGherkinRows()) {
            final List<String> txnDt = txnRow.getCells();
            if (!"AccountNumber".equalsIgnoreCase(txnDt.get(0))) {
                final TransactionRequest txnRequest = new TransactionRequest();
                txnRequest.setAccountNumber(txnDt.get(0));
                final Transaction transaction = new Transaction();
                transaction.setAmount(Double.parseDouble(txnDt.get(1)));
                TransactionType txnType = TransactionType.DEBIT;
                transaction.setDate(new Date());
                transaction.setTxnType(txnType);
                txnRequest.setTransaction(transaction);
                userAction.depositAmount(txnRequest);
            }
        }
    }

    @When("^transaction details is requested gives only last twenty$")
    public void transaction_details_is_requested(final DataTable txnTable) throws Throwable {
        for (final DataTableRow txnRow : txnTable.getGherkinRows()) {
            final List<String> txnDt = txnRow.getCells();
            if (!"AccountNumber".equalsIgnoreCase(txnDt.get(0))) {
                transactions = userTxnImpl.getTransaction(txnDt.get(0),20);
                assertTrue(transactions.size() <= 20);
            }
        }
    }

    @Then("^interest is calculated and logged$")
    public void interest_is_calculated_and_logged(final DataTable txnTable) throws Throwable {
        for (final DataTableRow txnRow : txnTable.getGherkinRows()) {
            final List<String> txnDt = txnRow.getCells();
            if (!"AccountNumber".equalsIgnoreCase(txnDt.get(0))) {
                final List<Transaction> transactions = userTxnImpl.getTransaction(txnDt.get(0), 20);
                assertTrue(transactions.stream().anyMatch(transaction -> TransactionType.INTEREST.name().equalsIgnoreCase(transaction.getTxnType().name())));
            }
        }
    }
}
