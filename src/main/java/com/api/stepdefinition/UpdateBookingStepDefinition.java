package com.api.stepdefinition;

import com.api.model.BookingDetailsDTO;
import com.api.utils.ExcelUtils;
import com.api.utils.JsonReader;
import com.api.utils.ResponseHandler;
import com.api.utils.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UpdateBookingStepDefinition {

    private TestContext testContext;
    private static final Logger LOGGER = LogManager.getLogger(UpdateBookingStepDefinition.class);

    public UpdateBookingStepDefinition (TestContext testContext) {
        this.testContext = testContext;
    }
    @When("User create an auth token with credential {string} & {string}")
    public void userCreateAnAuthTokenWithCredential(String username, String password) {
        JSONObject credentials = new JSONObject();
        credentials.put("username", username);
        credentials.put("password", password);
        testContext.response = testContext.requestSetup().body(credentials.toString())
                .when().post(testContext.session.get("endpoint").toString());
        String token = testContext.response.path("token");
        LOGGER.info("Auth Token: " + token);
        testContext.session.put("token", "token=" + token);
    }

    @And("User updates the details of a booking")
    public void userUpdatesTheDetailsOfABooking(DataTable dataTable) {
        Map<String, String> bookingData = dataTable.asMaps().get(0);
        JSONObject bookingBody = new JSONObject();
        bookingBody.put("firstname", bookingData.get("firstname"));
        bookingBody.put("lastname", bookingData.get("lastname"));
        bookingBody.put("totalprice", Integer.valueOf(bookingData.get("totalprice")));
        bookingBody.put("depositpaid", Boolean.valueOf(bookingData.get("lastname")));
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", bookingData.get("checkin"));
        bookingDates.put("checkout", bookingData.get("checkout"));
        bookingBody.put("bookingdates", bookingDates);
        bookingBody.put("additionalneeds", bookingData.get("additionalneeds"));

        testContext.response = testContext.requestSetup()
                .header("Cookie", testContext.session.get("token").toString())
                .pathParam("bookingID", testContext.session.get("bookingID"))
                .body(bookingBody.toString())
                .when().put(testContext.session.get("endpoint") + "/{bookingID}");

        BookingDetailsDTO bookingDetailsDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDetailsDTO.class);
        assertNotNull("Booking not created", bookingDetailsDTO);
    }

    @When("User updates the booking details using {string} from excel")
    public void userUpdatesTheBookingDetailsUsingFromExcel(String dataKey) throws Exception {
        Map<String, String> excelDataMap = ExcelUtils.getData(dataKey);
        testContext.response = testContext.requestSetup()
                .header("Cookie", testContext.session.get("token").toString())
                .pathParam("bookingID", testContext.session.get("bookingID"))
                .body(excelDataMap.get("requestBody"))
                .when().put(testContext.session.get("endpoint") + "/{bookingID}");

        BookingDetailsDTO bookingDetailsDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDetailsDTO.class);
        assertNotNull("Booking not created", bookingDetailsDTO);
        testContext.session.put("excelDataMap", excelDataMap);
    }

    @And("User updates the booking details using {string} from {string}")
    public void userUpdatesTheBookingDetailsUsingFrom(String dataKey, String JSONFile) {
        testContext.response = testContext.requestSetup()
                .header("Cookie", testContext.session.get("token").toString())
                .pathParam("bookingID", testContext.session.get("bookingID"))
                .body(JsonReader.getRequestBody(JSONFile, dataKey))
                .when().put(testContext.session.get("endpoint") + "/{bookingID}");

        BookingDetailsDTO bookingDetailsDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDetailsDTO.class);
        assertNotNull("Booking not created", bookingDetailsDTO);
    }

    @And("User makes a request to update first name to {string} and last name to {string}")
    public void userMakesARequestToUpdateFirstNameToAndLastNameTo(String firstName , String lastName) {
        JSONObject body = new JSONObject();
        body.put("firstname", firstName);
        body.put("lastname", lastName);

        testContext.response = testContext.requestSetup()
                .header("Cookie", testContext.session.get("token").toString())
                .pathParam("bookingID", testContext.session.get("bookingID"))
                .body(body.toString())
                .when().patch(testContext.session.get("endpoint") + "/{bookingID}");

        BookingDetailsDTO bookingDetailsDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDetailsDTO.class);
        assertNotNull("Booking not created", bookingDetailsDTO);
        assertEquals("First name did not match", firstName, bookingDetailsDTO.getFirstname());
        assertEquals("Last name did not match", lastName, bookingDetailsDTO.getLastname());
    }
}















