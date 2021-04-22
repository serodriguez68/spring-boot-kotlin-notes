package com.sergio.bankSpringTutorial.controller

import com.sergio.bankSpringTutorial.model.Bank
import com.sergio.bankSpringTutorial.service.BankService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

/* Tells spring to include this as bean in the application context */
@RestController
@RequestMapping("/api/banks") // This rest controller is responsible for endpoints that begin with api/banks
class BankController(private val bankService: BankService) {

    // Exception handler for a specified exception that needs
    // to be handled at the controller level.
    // Note that this is not great software design as this is
    // an expected error that bubbles up many levels.
    // A result monad would probably be a better idea.
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> {
        // If we need to craft custom responses, we use ResponseEntity
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> {
        // If we need to craft custom responses, we use ResponseEntity
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    /* Serialization into JSON
    * By default, Spring automatically uses an ObjectMapper to
    * serialize the return value of our controller methods.
    * The default serializer is Jackson, but that can be changed
    * in the configuration
    * */

    @GetMapping
    fun getBanks(): Collection<Bank> {
        return bankService.getBanks()
    }

    // Getting parameters from the URL
    @GetMapping("/{accountNumber}")
    fun getBank(@PathVariable accountNumber: String) = bankService.getBank(accountNumber)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Tells spring which status to return if everything goes ok
    /* @RequestBody tells spring to look in the request body and
    * deserialize a Bank out of it*/
    fun addBank(@RequestBody bank: Bank): Bank = bankService.addBank(bank)

    @PatchMapping
    fun updateBank(@RequestBody bank: Bank): Bank = bankService.updateBank(bank)

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Returning NO_CONTENT is the best practice for after deletion
    // Returning Unit from a controller causes an empty (header only) HTTP response by Spring.
    fun deleteBank(@PathVariable accountNumber: String): Unit = bankService.deleteBank(accountNumber)
}