Feature: List Rooms

  As a customer i want to know all the available room so that i can book then

  Scenario Outline: List Rooms
    When i list all the rooms
    Then i detect that the room "<name>" exists

    Examples:
    |name |
    |Sala América do Sul |
    |Sala América do Norte |
    |Sala Europa |
    |Sala África |