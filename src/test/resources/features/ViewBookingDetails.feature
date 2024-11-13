  @bookingApi
  Feature: To view the booking details

    Scenario: To view all the booking IDs
      Given User has access to endpoint "/booking"
      When User makes a request to view booking IDs
      Then User should get the response code 200
      And User should see all the booking IDs


    Scenario: To view booking details
      Given User has access to endpoint "/booking"
      When User makes a request to view booking IDs
      And    User makes a request to view details of a booking ID
      Then  User should get the response code 200
      And    User validates the response with JSON schema "bookingDetailsSchema.json"


    Scenario Outline: To view all the booking Ids by booking dates
      Given User has access to endpoint "/booking"
      When User makes a request to view booking IDs from "<checkin>" to "<checkout>"
      Then  User should get the response code 200
      And    User should see all the booking IDs

      Examples:
      | checkin       | checkout      |
      | 2018-01-01 | 2021-12-31 |
      | 2010-01-01 | 2020-12-31 |


    Scenario: To view all the booking IDs by booking names
      Given User has access to endpoint "/booking"
      When User makes a request to view booking IDs
      Then  User should see all the booking IDs
      And   User makes a request to view details of a booking ID
      And   User makes a request to view all the booking IDs of that user name
      And   User should get the response code 200
      And   User should see all the booking IDs


    Scenario: To do the health check of the booking api
      Given User has access to endpoint "/ping"
      When User makes a request to check the health of the booking service
      Then User should get the response code 201