package com.hiring.rbs.banking.data;

import com.google.common.base.Objects;

public class RbsCustomer {
    private String firstName;
    private String accountNumber;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFullName(){
        return new StringBuilder(firstName).append(" ").append(lastName).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RbsCustomer that = (RbsCustomer) o;
        return Objects.equal(firstName, that.firstName) &&
                Objects.equal(accountNumber, that.accountNumber) &&
                Objects.equal(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(firstName, accountNumber, lastName);
    }

    @Override
    public String toString() {
        return "RbsCustomer{" +
                "firstName='" + firstName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
