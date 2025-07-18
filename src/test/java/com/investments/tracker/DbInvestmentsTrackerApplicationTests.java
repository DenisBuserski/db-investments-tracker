package com.investments.tracker;

import com.investments.tracker.controller.DepositController;
import com.investments.tracker.controller.WithdrawalController;
import com.investments.tracker.service.deposit.DepositService;
import com.investments.tracker.service.withdrawal.WithdrawalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class DbInvestmentsTrackerApplicationTests {
	@Autowired
	private DepositController depositController;

	@Autowired
	private DepositService depositService;

	@Autowired
	private WithdrawalController withdrawalController;

	@Autowired
	private WithdrawalService withdrawalService;

	@Test
	void contextLoads() {
		assertThat(depositController).isNotNull();
		assertThat(depositService).isNotNull();
		assertThat(withdrawalController).isNotNull();
		assertThat(withdrawalService).isNotNull();
	}

}
