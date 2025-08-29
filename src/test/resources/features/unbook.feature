Feature: Unbook a room
  As as customer i want to unbook a room for some reason

  Scenario: Delete an booking
  Given the room "Sala América do Norte" exists
  And the room has at least one booking for today
  When i try to delete the booking
  Then the booking should be successfully deleted

  Scenario: Cannot delete an past booking
    Given the room "Sala África" exists
    And the room has at least one booking for today that is already finished
    When i try to delete the booking
    Then the booking deletion should not be processed

  Scenario: Cannot delete an booking in progress
    Given the room "Sala África" exists
    And the room has at least one booking for today that is already in progress
    When i try to delete the booking
    Then the booking deletion should not be processed