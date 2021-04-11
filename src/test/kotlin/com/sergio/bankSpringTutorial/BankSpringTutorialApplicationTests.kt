package com.sergio.bankSpringTutorial

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BankSpringTutorialApplicationTests {

	@Test
	fun contextLoads() {
		// Despite looking like this test is not doing anything, it is actually testing that the application context
		// loads correctly. To run a test, we need to load the app so this dummy test forces the application context
		// to load.
	}

}
