package com.hiring.rbs.banking.controller;

import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.dao.UserInfo;
import com.hiring.rbs.banking.exception.ActiveAccountException;
import com.hiring.rbs.banking.exception.InsufficientFundsException;
import com.hiring.rbs.banking.exception.NoDataFoundException;
import com.hiring.rbs.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/")
public class UserDetails {
    private UserService userService;

    @Autowired
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "addNewUser", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    public void addNewUser(@RequestBody final RbsCustomer customer) {
        userService.addUser(customer);
    }

    @RequestMapping(value = "deleteUser", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteUser(@RequestBody final RbsCustomer customer) {
        HttpStatus httpStatus;
        String msg;
        try {
            userService.removeUser(customer);
            httpStatus = HttpStatus.OK;
            msg = new StringBuilder(customer.getAccountNumber()).append(" has been deleted").toString();
        } catch (final ActiveAccountException exception) {
            msg = exception.getMessage();
            httpStatus = HttpStatus.PRECONDITION_FAILED;
        }
        return new ResponseEntity<>(msg, httpStatus);
    }

    @RequestMapping(value = "updateUser", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateUser(@RequestBody final RbsCustomer customer) {
        return String.valueOf(userService.updateUser(customer));
    }

    @RequestMapping(value = "findUserByAccountNumber", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findUserByAccountNumber(final String accountNumber) {
        ResponseEntity<Object> responseEntity;
        final RbsCustomer rbsCustomer = userService.findUserByAccountId(accountNumber);
        if (rbsCustomer == null) {
            responseEntity = new ResponseEntity<>("User Not found", HttpStatus.NOT_FOUND);
        } else {
            responseEntity = new ResponseEntity<>(rbsCustomer, HttpStatus.OK);
        }
        return responseEntity;
    }
}
