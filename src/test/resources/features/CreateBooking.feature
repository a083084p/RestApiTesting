@bookingApi
Feature: To create a new booking

  Scenario Outline: To create new booking using data table
    Given User has access to endpoint "/booking"
    When User creates a booking
      | firstname      | lastname     | totalprice      | depositpaid      | checkin     | checkout      | additionalneeds      |
      | <firstname> | <lastname> | <totalprice> | <depositpaid> | <checkin> | <checkout> | <additionalneeds> |
    Then User should get the response code 200
    And User validates the response with JSON schema "createBookingSchema.json"

    Examples:
      | firstname | lastname | totalprice | depositpaid | checkin       | checkout      | additionalneeds |
      | John        | Doe        |       1200  | true           | 2021-05-05 | 2021-05-15 | Breakfast          |
      | Jane        | Doe        |       2400  | false          | 2021-06-01 | 2021-07-10 | Dinner              |



    Scenario Outline: To create new booking using excel data
      Given User has access to endpoint "/booking"
      When User creates a booking using "<dataKey>" from excel
      Then User should get the response code 200
      And User validates the response with JSON schema from excel

      Examples:
      | dataKey            |
      | createBooking1 |
      | createBooking2 |



    Scenario Outline: To create new booking using JSON data
      Given User has access to endpoint "/booking"
      When User creates a booking using data "<dataKey>" from JSON file "<JsonFile>"
      Then User should get the response code 200
      And User validates the response with JSON schema "createBookingSchema.json"

      Examples:
      | dataKey            | JsonFile                |
      | createBooking1 |  bookingBody.json |
      | createBooking2 |  bookingBody.json |