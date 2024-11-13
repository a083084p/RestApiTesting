  @bookingApi
  Feature: Verify update booking scenarios

    Background: Create an auth token
      Given User has access to endpoint "/auth"
      When User create an auth token with credential "admin" & "password123"
      Then  User should get the response code 200


    Scenario Outline: To update a booking using cucumber Data Table
      Given User has access to endpoint "/booking"
      When User makes a request to view booking IDs
      And    User updates the details of a booking
      | firstname     | lastname     | totalprice     | depositpaid     | checkin     | checkout      | additionalneeds     |
      | <firstname>| <lastname> | <totalprice>| <depositpaid> | <checkin>| <checkout> | <additionalneeds> |
      Then User should get the response code 200
      And   User validates the response with JSON schema "bookingDetailsSchema.json"

      Examples:
        | firstname     | lastname     | totalprice | depositpaid | checkin       | checkout      | additionalneeds |
        | John            | Rambo        | 10000     |  true           | 2021-05-15 | 2021-06-11 | Breakfast          |
        | Rocky          | Balboa        | 2006       |  false          | 2021-06-01 | 2021-07-10 | Dinner              |


    Scenario Outline: To create and update a new booking using data from the excel
      Given User has access to endpoint "/booking"
      And   User creates a booking using "<createKey>" from excel
      When User updates the booking details using "<updateKey>" from excel
      Then  User should get the response code 200
      And   User validates the response with JSON schema from excel

      Examples:
      | createKey         | updateKey         |
      | createBooking1 | updateBooking1 |
      | createBooking2 | updateBooking2 |


    Scenario Outline: To update a booking using JSON data
      Given User has access to endpoint "/booking"
      When User makes a request to view booking IDs
      And    User updates the booking details using "<dataKey>" from "<JSONFile>"
      Then  User should get the response code 200
      And    User validates the response with JSON schema "bookingDetailsSchema.json"

      Examples:
      | dataKey            | JSONFile              |
      | updateBooking1 | bookingBody.json |
      | updateBooking2 | bookingBody.json |


    Scenario: To partially update a booking
      Given User has access to endpoint "/booking"
      When User makes a request to view booking IDs
      And    User makes a request to update first name to "Jonny" and last name to "Adams"
      Then  User should get the response code 200
      And   User validates the response with JSON schema "bookingDetailsSchema.json"