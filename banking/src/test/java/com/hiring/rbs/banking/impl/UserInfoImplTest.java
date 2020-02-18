package com.hiring.rbs.banking.impl;

import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.dataStore.CoreDetails;
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
public class UserInfoImplTest {
    private UserInfoImpl userInfoImpl;
    private CoreDetails coreDetails;

    @Before
    public void setUp() throws Exception {
        userInfoImpl = new UserInfoImpl();
        coreDetails = mock(CoreDetails.class);
        userInfoImpl.setCoreDetails(coreDetails);
    }

    @Test
    public void testAddUser() {
        Mockito.doNothing().when(coreDetails).addUser(any(RbsCustomer.class));
        userInfoImpl.addUser(new RbsCustomer());
    }

    @Test
    public void testRemoveUser() {
        Mockito.doNothing().when(coreDetails).removeUser(any(RbsCustomer.class));
        userInfoImpl.removeUser(new RbsCustomer());
    }

    @Test
    public void testUpdateUser() {
        when(coreDetails.updateUser(any(RbsCustomer.class))).thenReturn(true);
        assertTrue(userInfoImpl.updateUser(new RbsCustomer()));
    }

    @Test
    public void testFindUserByAccount() {
        when(coreDetails.findUserByAccountId(anyString())).thenReturn(mock(RbsCustomer.class));
        assertNotNull(userInfoImpl.findUserByAccountId("123"));
    }
}
