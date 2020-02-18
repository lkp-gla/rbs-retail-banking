Feature: Basic Cucumber scenarios for Retail banking

  Scenario: Add first Transaction
    Given user account
      | AccountNumber | FirstName | LastName |
      | 1             | firstnm   | lastNm   |
    When a deposit is made
      | AccountNumber | Amount | Type   |
      | 1             | 20.0   | Credit |
    Then transaction is logged and balance updated
      | HttpStatus | AccountNumber | Balance |
      | 200        | 1             | 20.0    |

  Scenario: Add Multiple Transaction
    Given user account
      | AccountNumber | FirstName | LastName |
      | 2             | firstnm   | lastNm   |
    When a deposit is made
      | AccountNumber | Amount | Type   |
      | 2             | 20.0   | Credit |
      | 2             | 20.0   | Credit |
    Then transaction is logged and balance updated
      | HttpStatus | AccountNumber | Balance |
      | 200        | 2             | 40.0    |

  Scenario: Accrued Interest Calculated after minimum transaction
    Given user account
      | AccountNumber | FirstName | LastName |
      | 3             | firstnm   | lastNm   |
    When a deposit is made
      | AccountNumber | Amount | Type   |
      | 3             | 20.0   | Credit |
      | 3             | 20.0   | Credit |
      | 3             | 20.0   | Credit |
      | 3             | 20.0   | Credit |
      | 3             | 20.0   | Credit |
      | 3             | 20.0   | Credit |
    Then interest is calculated and logged
      | AccountNumber |
      | 3             |

  Scenario: User transaction history
    Given user account
      | AccountNumber | FirstName | LastName |
      | 4             | firstnm   | lastNm   |
    And a deposit is made
      | AccountNumber | Amount | Type   |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
      | 4             | 20.0   | Credit |
    And withdrawal is made
      | AccountNumber | Amount | Type  |
      | 4             | 20.0   | Debit |
      | 4             | 20.0   | Debit |
      | 4             | 20.0   | Debit |
      | 4             | 20.0   | Debit |
      | 4             | 20.0   | Debit |
      | 4             | 20.0   | Debit |
      | 4             | 20.0   | Debit |
      | 4             | 20.0   | Debit |
      | 4             | 20.0   | Debit |
    When transaction details is requested gives only last twenty
      | AccountNumber |
      | 4             |