package com.api.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;



public class RestAssuredRequestFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(RestAssuredRequestFilter.class);

    @Override
    public Response filter (FilterableRequestSpecification filterableRequestSpecification,
                            FilterableResponseSpecification filterableResponseSpecification,
                            FilterContext filterContext) {
        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);

        String logs = " Request Method:  " + filterableRequestSpecification.getMethod() +
                " Request URI:  " + filterableRequestSpecification.getURI() +
                " Request Header:  " + filterableRequestSpecification.getHeaders() +
                " Request Body:  " + filterableRequestSpecification.getBody() +
                " Response Status:  " + response.getStatusLine() +
                " Response Header:  " + response.getHeaders() +
                " Response Body:  " + response.getBody().prettyPrint();

        LOGGER.info("---------------------------------------------------------------------");

        LOGGER.info("logs:==> " + logs);

        LOGGER.info("---------------------------------------------------------------------");

        return response;
    }

}
