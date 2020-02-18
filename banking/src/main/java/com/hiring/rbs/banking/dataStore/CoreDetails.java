package com.hiring.rbs.banking.dataStore;

import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Scope("singleton")
@Component
public class CoreDetails {
    private Logger LOG = LoggerFactory.getLogger(CoreDetails.class);
    private Map<String, RbsCustomer> rbsCustomerMap = new HashMap();

    public void addUser(final RbsCustomer customer) {
        LOG.info("Adding user to core {}", customer);
        boolean addedUser = false;
        if (rbsCustomerMap.containsKey(customer.getAccountNumber())) {
            rbsCustomerMap.replace(customer.getAccountNumber(), customer);
        } else {
            rbsCustomerMap.put(customer.getAccountNumber(), customer);
        }
    }

    public void removeUser(final RbsCustomer customer) {
        LOG.info("Removing user with accountNumber {}", customer.getAccountNumber());
        rbsCustomerMap.remove(customer.getAccountNumber());
    }

    public boolean updateUser(final RbsCustomer customer) {
        boolean userUpdated = false;
        if (rbsCustomerMap.containsKey(customer.getAccountNumber())) {
            rbsCustomerMap.replace(customer.getAccountNumber(), customer);
            userUpdated = true;
        }
        return userUpdated;
    }

    public RbsCustomer findUserByAccountId(final String accountNumber) {
        return rbsCustomerMap.get(accountNumber);
    }
}
