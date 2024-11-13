package com.api.stepdefinition;

import com.api.model.BookingDetailsDTO;
import com.api.model.BookingID;
import com.api.utils.ResponseHandler;
import com.api.utils.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ViewBookingStepDefinition {
    
    private TestContext testContext;
    private static final Logger LOGGER = LogManager.getLogger(ViewBookingStepDefinition.class);
    public ViewBookingStepDefinition(TestContext testContext) {
        this.testContext = testContext;
    }
    
    @Given("User has access to endpoint {string}")
    public void userHasAccessToEndpoint(String endpoint) {
        testContext.session.put("endpoint", endpoint);
    }



    @Then("User should get the response code {int}")
    public void userShouldGetTheResponseCode(int statusCode) {
        assertEquals(statusCode, testContext.response.getStatusCode());
    }

    @And("User validates the response with JSON schema {string}")
    public void userValidatesTheResponseWithJSONSchema(String schemaFileName) {
        testContext.response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/"+schemaFileName));
    }

    @When("User makes a request to view booking IDs")
    public void userMakesARequestToViewBookingIDs() {
        testContext.response = testContext.requestSetup().when().get(testContext.session.get("endpoint").toString());
        int bookingID = testContext.response.getBody().jsonPath().getInt("[0].bookingid");
        LOGGER.info("Booking ID: " + bookingID);
        assertNotNull("Booking ID not found ", bookingID);
        testContext.session.put("bookingID", bookingID);
    }

    @And("User should see all the booking IDs")
    public void userShouldSeeAllTheBookingIDs() {
        BookingID[] bookingIDs = ResponseHandler.deserializedResponse(testContext.response, BookingID[].class);
        assertNotNull("Booking ID is not Null",  bookingIDs);
    }

    @And("User makes a request to view details of a booking ID")
    public void userMakesARequestToViewDetailsOfABookingID() {
        LOGGER.info("Session BookingID: " + testContext.session.get("bookingID"));
        testContext.response = testContext.requestSetup().pathParam("bookingID", testContext.session.get("bookingID"))
                .when().get(testContext.session.get("endpoint") + "/{bookingID}");
        BookingDetailsDTO bookingDetailsDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDetailsDTO.class);
        assertNotNull("Booking details not found", bookingDetailsDTO);
        testContext.session.put("firstname", bookingDetailsDTO.getFirstname());
        testContext.session.put("lastname", bookingDetailsDTO.getLastname());
    }

    @When("User makes a request to view booking IDs from {string} to {string}")
    public void userMakesARequestToViewBookingIDsFromTo(String checkin, String checkout) {
        testContext.response = testContext.requestSetup().queryParams("checkin", checkin, "checkout", checkout)
                .when().get(testContext.session.get("endpoint").toString());
    }

    @And("User makes a request to view all the booking IDs of that user name")
    public void userMakesARequestToViewAllTheBookingIDsOfThatUserName() {
        LOGGER.info("Session firstname: " + testContext.session.get("firstname"));
        LOGGER.info("Session lastname: " + testContext.session.get("lastname"));
        testContext.response = testContext.requestSetup()
                .queryParams("firstname", testContext.session.get("firstname"), "lastname", testContext.session.get("lastname"))
                .when().get(testContext.session.get("endpoint").toString());
        BookingID[] bookingIDS = ResponseHandler.deserializedResponse(testContext.response, BookingID[].class);
        assertNotNull("Booking id not found", bookingIDS);
    }

    @When("User makes a request to check the health of the booking service")
    public void userMakesARequestToCheckTheHealthOfTheBookingService() {
        testContext.response = testContext.requestSetup().get(testContext.session.get("endpoint").toString());
    }
}
