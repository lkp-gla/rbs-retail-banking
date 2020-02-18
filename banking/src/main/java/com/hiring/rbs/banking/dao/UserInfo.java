package com.hiring.rbs.banking.dao;

import com.hiring.rbs.banking.data.RbsCustomer;

public interface UserInfo {
    void addUser(final RbsCustomer customer);

    void removeUser(final RbsCustomer customer);

    boolean updateUser(final RbsCustomer customer);

    RbsCustomer findUserByAccountId(final String accountNumber);
}
