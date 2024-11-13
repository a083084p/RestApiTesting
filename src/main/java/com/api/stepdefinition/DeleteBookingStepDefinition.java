package com.api.stepdefinition;

import com.api.utils.TestContext;
import io.cucumber.java.en.When;

public class DeleteBookingStepDefinition {

    private TestContext testContext;


    public DeleteBookingStepDefinition (TestContext testContext) {
        this.testContext = testContext;
    }
    @When("User makes a request to delete booking with basic auth {string} and {string}")
    public void userMakesARequestToDeleteBookingWithBasicAuthAnd(String username, String password) {
        testContext.response = testContext.requestSetup()
                .auth().preemptive().basic(username, password)
                .pathParam("bookingID", testContext.session.get("bookingID"))
                .when().delete(testContext.session.get("endpoint") + "/{bookingID}");
    }
}
