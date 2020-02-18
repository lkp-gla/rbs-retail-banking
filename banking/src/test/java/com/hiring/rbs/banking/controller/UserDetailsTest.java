package com.hiring.rbs.banking.controller;

import com.hiring.rbs.banking.data.RbsCustomer;
import com.hiring.rbs.banking.exception.ActiveAccountException;
import com.hiring.rbs.banking.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserDetailsTest {
    private UserDetails userDetails;
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userDetails = new UserDetails();
        userService = mock(UserService.class);
        userDetails.setUserService(userService);
    }

    @Test
    public void testAddUser() {
        Mockito.doNothing().when(userService).addUser(any(RbsCustomer.class));
        userDetails.addNewUser(new RbsCustomer());
    }

    @Test
    public void testUpdateUser() {
        when(userService.updateUser(any(RbsCustomer.class))).thenReturn(true);
        assertEquals("true", userDetails.updateUser(new RbsCustomer()));
    }

    @Test
    public void testDeleteUserFailed() {
        Mockito.doThrow(new ActiveAccountException("Live account cannot delete")).when(userService).removeUser(any(RbsCustomer.class));
        final ResponseEntity<String> responseEntity = userDetails.deleteUser(new RbsCustomer());
        assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
        assertEquals("Live account cannot delete", responseEntity.getBody());
    }

    @Test
    public void testDeleteUserSuccess() {
        Mockito.doNothing().when(userService).removeUser(any(RbsCustomer.class));
        final RbsCustomer rbsCustomer = mock(RbsCustomer.class);
        when(rbsCustomer.getAccountNumber()).thenReturn("123");
        final ResponseEntity<String> responseEntity = userDetails.deleteUser(rbsCustomer);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("123 has been deleted", responseEntity.getBody());
    }

    @Test
    public void testFindCustomerByUnknownAccountNo() {
        when(userService.findUserByAccountId(anyString())).thenReturn(null);
        final ResponseEntity<Object> responseEntity = userDetails.findUserByAccountNumber("123");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User Not found", responseEntity.getBody());
    }

    @Test
    public void testFindCustomerByAccount() {
        when(userService.findUserByAccountId(anyString())).thenReturn(new RbsCustomer());
        final ResponseEntity<Object> responseEntity = userDetails.findUserByAccountNumber("123");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
