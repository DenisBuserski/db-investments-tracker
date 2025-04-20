package com.investments.tracker;

import com.investments.tracker.controller.DepositController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DbInvestmentsTrackerApplicationTests {
	@Autowired
	private DepositController depositController;

	@Test
	void contextLoads() {
		assertNotNull(depositController);
	}

}
