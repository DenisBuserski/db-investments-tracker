package com.investments.tracker.IT_tests;

import com.investments.tracker.controller.DepositController;
import com.investments.tracker.service.DepositService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

//@SpringBootTest(webEnvironment = DEFINED_PORT)
@SpringBootTest
@ActiveProfiles("test-integration")
public class DepositControllerIntegrationTest {

    @Autowired
    private DepositController depositController;

    @Autowired
    private DepositService depositService;

    @Test
    public void smokeTestDepositControllerIsLoaded() {
        assertThat(depositController).isNotNull();
    }

    @Test
    public void smokeTestDepositServiceIsLoaded() {
        assertThat(depositService).isNotNull();
    }
}
