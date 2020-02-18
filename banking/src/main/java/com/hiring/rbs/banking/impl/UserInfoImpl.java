package com.hiring.rbs.banking.impl;

import com.hiring.rbs.banking.dao.UserInfo;
import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.dataStore.CoreDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class UserInfoImpl implements UserInfo {
    private Logger LOG = LoggerFactory.getLogger(UserInfoImpl.class);

    private CoreDetails coreDetails;

    @Autowired
    public void setCoreDetails(final CoreDetails coreDetails) {
        this.coreDetails = coreDetails;
    }

    @Override
    public void addUser(final RbsCustomer customer) {
        LOG.info("Adding user  {}", customer);
        coreDetails.addUser(customer);
    }

    @Override
    public void removeUser(final RbsCustomer customer) {
        coreDetails.removeUser(customer);
    }

    @Override
    public boolean updateUser(final RbsCustomer customer) {
        return coreDetails.updateUser(customer);
    }

    @Override
    public RbsCustomer findUserByAccountId(final String accountNumber) {
        return coreDetails.findUserByAccountId(accountNumber);
    }
}
