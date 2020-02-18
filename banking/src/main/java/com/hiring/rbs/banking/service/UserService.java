package com.hiring.rbs.banking.service;

import com.hiring.rbs.banking.dao.UserInfo;
import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.exception.ActiveAccountException;
import com.hiring.rbs.banking.validator.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private Logger LOG = LoggerFactory.getLogger(UserService.class);
    private UserInfo userInfoImpl;
    private TransactionValidator transactionValidator;

    @Autowired
    public void setUserInfoImpl(final UserInfo userInfoImpl) {
        this.userInfoImpl = userInfoImpl;
    }

    @Autowired
    public void setTransactionValidator(final TransactionValidator transactionValidator) {
        this.transactionValidator = transactionValidator;
    }

    public void addUser(final RbsCustomer customer) {
        LOG.info("Adding user  {}", customer);
        userInfoImpl.addUser(customer);
    }

    public void removeUser(final RbsCustomer customer) throws ActiveAccountException {
        transactionValidator.preCheckAccountDelete(customer);
        userInfoImpl.removeUser(customer);
    }

    public boolean updateUser(final RbsCustomer customer) {
        return userInfoImpl.updateUser(customer);
    }

    public RbsCustomer findUserByAccountId(String accountNumber) {
        return userInfoImpl.findUserByAccountId(accountNumber);
    }
}
