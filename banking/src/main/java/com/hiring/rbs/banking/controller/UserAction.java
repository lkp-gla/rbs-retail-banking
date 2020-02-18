package com.hiring.rbs.banking.controller;

import com.hiring.rbs.banking.data.Transaction;
import com.hiring.rbs.banking.data.TransactionRequest;
import com.hiring.rbs.banking.data.TransactionType;
import com.hiring.rbs.banking.exception.InactiveAccountException;
import com.hiring.rbs.banking.exception.InsufficientFundsException;
import com.hiring.rbs.banking.exception.InvalidAmountException;
import com.hiring.rbs.banking.exception.NoDataFoundException;
import com.hiring.rbs.banking.service.UserTxnService;
import com.hiring.rbs.banking.validator.TransactionValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UserAction {
    private UserTxnService userTxnService;
    private Integer DEFAULT_TXN_HISTORY = 20;
    private Logger LOG = LoggerFactory.getLogger(UserAction.class);

    @Autowired
    public void setUserTxnService(final UserTxnService userTxnService) {
        this.userTxnService = userTxnService;
    }

    @RequestMapping(value = "/withdrawAmount", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(value = "Withdraw amount from account",
            notes = "Transaction type in request will be ignored and logged as Debit. " +
                    "Add User via /api/addNewUser. " +
                    "If Account does not exist or if there are insufficient funds then will get a 412 response")
    public ResponseEntity<String> withdrawAmount(@RequestBody final TransactionRequest transactionRequest) {
        transactionRequest.getTransaction().setTxnType(TransactionType.DEBIT);
        HttpStatus httpStatus;
        String msg;
        try {
            TransactionValidator.prevalidateRequest(transactionRequest);
            msg = userTxnService.withDrawAmount(transactionRequest);
            httpStatus = HttpStatus.OK;
        } catch (final NoDataFoundException | InvalidAmountException | InsufficientFundsException exception) {
            msg = exception.getMessage();
            httpStatus = HttpStatus.PRECONDITION_FAILED;
        }
        return new ResponseEntity<>(msg, httpStatus);
    }

    @RequestMapping(value = "/depositAmount", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Deposit into existing account",
            notes = "Transaction type in request will be ignored and logged as Credit by default. " +
                    "Add User via /api/addNewUser. " +
                    "Account should already exist else deposit will fail with error message")
    public ResponseEntity<String> depositAmount(@RequestBody final TransactionRequest transactionRequest) {
        transactionRequest.getTransaction().setTxnType(TransactionType.CREDIT);
        HttpStatus httpStatus;
        String msg;
        try {
            TransactionValidator.prevalidateRequest(transactionRequest);
            msg = userTxnService.logTransaction(transactionRequest);
            httpStatus = HttpStatus.OK;
        } catch (final NoDataFoundException | InvalidAmountException exception) {
            msg = exception.getMessage();
            httpStatus = HttpStatus.PRECONDITION_FAILED;
        }
        return new ResponseEntity<>(msg, httpStatus);
    }

    @RequestMapping(value = "/getLastTwentyTransactions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Transaction History",
            notes = "Transaction History will only restrict to last twenty transactions. If there are no transactions then will give a user readable error message")
    public ResponseEntity<Object> getLastTwentyTransactions(final String accountNumber) {
        ResponseEntity<Object> responseEntity;
        try {
            List<Transaction> transactions = userTxnService.getTransaction(accountNumber, DEFAULT_TXN_HISTORY);
            responseEntity = new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (final NoDataFoundException | InactiveAccountException exception) {
            LOG.error("Exception occurred {}", exception.getMessage());
            HttpStatus httpStatus = HttpStatus.PRECONDITION_FAILED;
            if (exception instanceof InactiveAccountException) {
                LOG.error("Inactive Account setting No content http response");
                httpStatus = HttpStatus.NOT_FOUND;
            }
            responseEntity = new ResponseEntity<>(exception.getMessage(), httpStatus);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/getCurrentBalance", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getCurrentBalance(final String accountNumber) {
        HttpStatus httpStatus;
        String msg;
        try {
            msg = String.valueOf(userTxnService.getCurrentBalance(accountNumber));
            httpStatus = HttpStatus.OK;
        } catch (final NoDataFoundException exception) {
            msg = exception.getMessage();
            httpStatus = HttpStatus.PRECONDITION_FAILED;
        }
        return new ResponseEntity<>(msg, httpStatus);
    }
}
