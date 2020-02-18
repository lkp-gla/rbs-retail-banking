package com.hiring.rbs.banking.service;

import com.hiring.rbs.banking.dao.UserInfo;
import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.exception.ActiveAccountException;
import com.hiring.rbs.banking.validator.TransactionValidator;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserServiceTest {
    private UserService userService;
    private UserInfo userInfoImpl;
    private TransactionValidator transactionValidator;

    @Before
    public void setUp() throws Exception {
        userService = new UserService();
        userInfoImpl = mock(UserInfo.class);
        transactionValidator = mock(TransactionValidator.class);
        userService.setTransactionValidator(transactionValidator);
        userService.setUserInfoImpl(userInfoImpl);
    }

    @Test
    public void testAddUser() {
        Mockito.doNothing().when(userInfoImpl).addUser(any(RbsCustomer.class));
        userService.addUser(new RbsCustomer());
    }

    @Test
    public void testRemoveUser() {
        Mockito.doNothing().when(transactionValidator).preCheckAccountDelete(any(RbsCustomer.class));
        Mockito.doNothing().when(userInfoImpl).removeUser(any(RbsCustomer.class));
        Assertions.assertThatCode(() -> userService.removeUser(new RbsCustomer())).doesNotThrowAnyException();
    }

    @Test(expected = ActiveAccountException.class)
    public void testRemoveUserException() {
        Mockito.doThrow(new ActiveAccountException("active Account")).when(transactionValidator).preCheckAccountDelete(any(RbsCustomer.class));
        userService.removeUser(new RbsCustomer());
    }

    @Test
    public void testUpdateUser() {
        when(userInfoImpl.updateUser(any(RbsCustomer.class))).thenReturn(true);
        assertTrue(userService.updateUser(new RbsCustomer()));
    }

    @Test
    public void testFindUserByAccout() {
        when(userInfoImpl.findUserByAccountId(anyString())).thenReturn(mock(RbsCustomer.class));
        assertNotNull(userService.findUserByAccountId("1234"));
    }
}
