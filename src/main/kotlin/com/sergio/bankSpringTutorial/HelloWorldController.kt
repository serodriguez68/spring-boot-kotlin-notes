package com.sergio.bankSpringTutorial

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Tells spring that this class is a controller.
// Rest controllers (as opposed to just @Controller) return data and not HTML.
@RestController
@RequestMapping("/api/hello") // This rest controller is responsible for endpoints that begin with api/hello
class HelloWorldController {

    @GetMapping// Assigns this function to the given path
    // @GetMapping("/some-subroute")
    fun helloWorld(): String = "Hello , this is a REST endpoint"
}