  @bookingApi
  Feature: To delete a booking

    Background: Create an auth token
      Given User has access to endpoint "/auth"
      When User create an auth token with credential "admin" & "password123"
      Then  User should get the response code 200


    Scenario: To delete a booking
      Given User has access to endpoint "/booking"
      And    User makes a request to view booking IDs
      When  User makes a request to delete booking with basic auth "admin" and "password123"
      Then   User should get the response code 201
