Feature: Payment

  Scenario: Create payment link for valid order
    Given a valid order with total 1000
    When I create a payment link
    Then the payment link should be returned successfully

  Scenario: Create payment link for invalid order
    Given an invalid order with total 0
    When I create a payment link
    Then it should throw an IllegalArgumentException