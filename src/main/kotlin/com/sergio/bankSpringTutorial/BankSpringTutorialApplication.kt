package com.sergio.bankSpringTutorial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

// Magic that indicates to string that this empty class will be the main application class
@SpringBootApplication
class BankSpringTutorialApplication {
	/* @Bean is the most generic way to add something to your Spring application beans.
	* In this case, Spring is going to use this method to provide Beans to any components that request
	* an @Autowired RestTemplate.
	* @see NetworkDataSource.kt */
	@Bean
	fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build()
}

// The main entrypoint of the application
fun main(args: Array<String>) {
	runApplication<BankSpringTutorialApplication>(*args)
}
