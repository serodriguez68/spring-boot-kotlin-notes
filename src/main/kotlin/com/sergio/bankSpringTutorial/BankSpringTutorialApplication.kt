package com.sergio.bankSpringTutorial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// Magic that indicates to string that this empty class will be the main application class
@SpringBootApplication
class BankSpringTutorialApplication

// The main entrypoint of the application
fun main(args: Array<String>) {
	runApplication<BankSpringTutorialApplication>(*args)
}
