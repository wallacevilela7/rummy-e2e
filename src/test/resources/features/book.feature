Feature: Book a room
  As a customer i want to book a room so that i can make a meeting

  Scenario: Book an available room
    Given the room "Sala América do Sul" exists
    And the room has no bookings for today
    When i book the room for one hour from now
    Then the room should be successfully booked


  Scenario: Conflict when a room is already booked
    Given the room "Sala Europa" exists
    And the room has no bookings for today
    And one user book the room for one hour from now
    When i book the room for one hour from now
    Then the booking should conflict