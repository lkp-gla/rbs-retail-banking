package com.hiring.rbs.banking.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RbsCustomerTest {
    private RbsCustomer rbsCustomer;

    @Before
    public void setUp() throws Exception {
        rbsCustomer = new RbsCustomer();
        rbsCustomer.setAccountNumber("123");
        rbsCustomer.setFirstName("first");
        rbsCustomer.setLastName("last");
    }

    @Test
    public void testGetMethod() {
        assertEquals("first last", rbsCustomer.getFullName());
        assertEquals("first", rbsCustomer.getFirstName());
        assertEquals("last", rbsCustomer.getLastName());
        assertEquals("123", rbsCustomer.getAccountNumber());
        assertNotNull(rbsCustomer.toString());
    }

    @Test
    public void testComparators() {
        final RbsCustomer customer1 = createCustomer("123", "first", "last");
        final RbsCustomer customer2 = createCustomer("123", "first", "last");
        assertTrue(customer1.equals(customer2));
    }

    private RbsCustomer createCustomer(final String acctNum, final String firstNm, final String lastNm) {
        final RbsCustomer rbsCustomer = new RbsCustomer();
        rbsCustomer.setAccountNumber(acctNum);
        rbsCustomer.setFirstName(firstNm);
        rbsCustomer.setLastName(lastNm);
        return rbsCustomer;
    }
}
