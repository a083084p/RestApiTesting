package com.api.stepdefinition;

import com.api.model.BookingDTO;
import com.api.utils.ExcelUtils;
import com.api.utils.JsonReader;
import com.api.utils.ResponseHandler;
import com.api.utils.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateBookingStepDefinition {

    private TestContext testContext;
    private static final Logger LOGGER = LogManager.getLogger(CreateBookingStepDefinition.class);

    public CreateBookingStepDefinition(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("User creates a booking")
    public void userCreatesABooking(DataTable dataTable) {
        Map<String, String> bookingData = dataTable.asMaps().get(0);

        JSONObject bookingBody = new JSONObject();
        bookingBody.put("firstname", bookingData.get("firstname"));
        bookingBody.put("lastname", bookingData.get("lastname"));
        bookingBody.put("totalprice", Integer.valueOf(bookingData.get("totalprice")));
        bookingBody.put("depositpaid", Boolean.valueOf(bookingData.get("depositpaid")));

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", bookingData.get("checkin"));
        bookingDates.put("checkout", bookingData.get("checkout"));
        bookingBody.put("bookingdates", bookingDates);
        bookingBody.put("additionalneeds", bookingData.get("additionalneeds"));

        testContext.response = testContext.requestSetup().body(bookingBody.toString())
                .when().post(testContext.session.get("endpoint").toString());

        BookingDTO bookingDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDTO.class);
        assertNotNull("Booking not created", bookingDTO);
        LOGGER.info("New booking ID: " + bookingDTO.getBookingid());
        testContext.session.put("bookingID", bookingDTO.getBookingid());
        LOGGER.info(bookingData);
        validateBookingData(new JSONObject(bookingData), bookingDTO);
    }

    private void validateBookingData (JSONObject bookingData, BookingDTO bookingDTO) {
        assertNotNull("Booking id missing ", bookingDTO.getBookingid());
        assertEquals("First name did not match ", bookingData.get("firstname"), bookingDTO.getBooking().getFirstname());
        assertEquals("Last name did not match ", bookingData.get("lastname"), bookingDTO.getBooking().getLastname());
        assertEquals("Total pricedid not match ", bookingData.get("totalprice"), bookingDTO.getBooking().getTotalprice());
        assertEquals("Deposit paid did not match ", bookingData.get("depositpaid"), bookingDTO.getBooking().getDepositpaid());
        assertEquals("Additional needs did not match ", bookingData.get("additionalneeds"), bookingDTO.getBooking().getAdditionalneeds());
        assertEquals("Checkin did not match ", bookingData.get("checkin"), bookingDTO.getBooking().getBookingdates().getCheckin());
        assertEquals("Checkout did not match ", bookingData.get("checkout"), bookingDTO.getBooking().getBookingdates().getCheckout());
    }

    @When("User creates a booking using {string} from excel")
    public void userCreatesABookingUsingFromExcel(String datakey) throws Exception {
        Map<String, String> excelDataMap = ExcelUtils.getData(datakey);
        testContext.response = testContext.requestSetup().body(excelDataMap.get("requestBody"))
                .when().post(testContext.session.get("endpoint").toString());

        BookingDTO bookingDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDTO.class);
        assertNotNull("Booking not created: ", bookingDTO);
        LOGGER.info("Newly created booking id: " + bookingDTO.getBookingid());
        testContext.session.put("bookingID", bookingDTO.getBookingid());
        validateBookingData(new JSONObject(excelDataMap.get("responseBody")), bookingDTO);
        testContext.session.put("excelDataMap", excelDataMap);
    }


    @And("User validates the response with JSON schema from excel")
    public void userValidatesTheResponseWithJSONSchemaFromExcel() {
        String responseSchema = ((Map<String, String>) testContext.session.get("excelDataMap")).get("responseSchema");
        testContext.response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(responseSchema));
        LOGGER.info("Successfully Validated schema from excel");
    }


    @When("User creates a booking using data {string} from JSON file {string}")
    public void userCreatesABookingUsingDataFromJSONFile(String dataKey, String JsonFile) {
        testContext.response = testContext.requestSetup().body(JsonReader.getRequestBody(JsonFile, dataKey))
                .when().post(testContext.session.get("endpoint").toString());

        BookingDTO bookingDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDTO.class);
        assertNotNull("Booking not created", bookingDTO);
        LOGGER.info("Newly created booking ID: " + bookingDTO.getBookingid());
        testContext.session.put("bookingID", bookingDTO.getBookingid());
    }
}
















