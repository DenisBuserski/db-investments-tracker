package com.investments.tracker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class DepositControllerTestIT {

    @Test
    @DisplayName("Test should return Status 200 when hit [/deposit/get/all]")
    public void testShouldReturnStatus200WhenHitDepositGetAll() {
        given().get("/deposit/get/all").then().statusCode(200);
    }
}
