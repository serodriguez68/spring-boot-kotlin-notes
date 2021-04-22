package com.sergio.bankSpringTutorial.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sergio.bankSpringTutorial.datasource.fake.FakeBankDataSource
import com.sergio.bankSpringTutorial.model.Bank
import com.sergio.bankSpringTutorial.service.BankService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

/* SWAPPING DEPENDENCIES AT TEST TIME
* There are multiple ways to swap an injected dependency for testing. Here we will explain
* 3. Please note that these 3 are mostly applicable for integration tests. If you are unit testing
* you are probably better of testing with a POJO (no Spring) and manually injecting whatever you need for the
* unit test.
*
* OPTION 1) @TestConfiguration. We annotate classes that bring special configuration to our
* tests with this. Inside the class, we override the bean we want to replace with the fake / dummy implementation.
* - To actually override the @Bean make sure you use the same name of the injected variable and the same type.
* - To apply the Test configuration to a test, use the @Import annotation
* - Augment @SpringBootTest with `properties = ["spring.main.allow-bean-definition-overriding=true"]` to temporarily
*   enable bean overriding.
* - This is the most flexible option, as you can run any code to replace the bean.
* - This option is the one shown in this file.

* OPTION 2) @MockBean (Mockito) or @MockkBean (mockk)
* Use one of these annotations within the test to tell Spring to replace the bean with mock. Then you can use Mockito
* or Mockk to determine how the mock should respond when a method is called on it.
* - @see: https://github.com/Ninja-Squad/springmockk
* - @see:https://www.youtube.com/watch?v=Ekr4jxOIf4c&t=514s
*
* OPTION 3) @Profile and @Active Profile
* Label bean implementations (stuff to inject) with different profiles (e.g label fake implementation with @Profile
* ("test)) and annotate a class with @ActiveProfile("test").
* This will tell spring to give precedence to Beans matching the active profile when the bean resolution is ambiguous.
* - This can be a very blunt instrument since you probably want to limit your profiles to only a few (3 to 5). This
* means that by activating a profile, you will be swapping a significant number of beans.
* */
@TestConfiguration
class SwapBankDataSourceInBankService {
    // TODO: I am not sure why I am being forced to swap the service and swapping the BankDataSource itself is not
    //   working. Maybe one can only swap CONCRETE beans and not interfaces?
    @Bean
    fun bankService(): BankService = BankService(FakeBankDataSource())
}

/* This test is not an isolated test and is also not a POJO.
* This test will use Spring's test capabilities.
* @SpringBootTest will boot the app so that it can be tested.
* - This makes sense for integration tests
* - This makes setting up the test somewhat expensive.
* - There are some ways to boot only parts of the application using Test Slices and other mechanisms.
* - The property is needed to allow overriding of dependencies using @TestConfiguration
* */
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@AutoConfigureMockMvc // Part of the setup to use MockMvc
@Import(SwapBankDataSourceInBankService::class)
internal class BankControllerTest(
    /* MockMvc allows you to make requests to your REST api without actually making HTTP requests.
    - Hooks in one layer below.
    - By avoiding HTTP requests, we make the tests faster
    */
    /* @Autowired is Spring's dependency injection. Here we are labelling
    * mockMvc as a variable that needs to be dependency injected */
    @Autowired val mockMvc: MockMvc,

    // Let Spring Autoinject an ObjectMapper serializer.
    // The default available one is Jackson
    @Autowired val objectMapper: ObjectMapper
) {


    val baseURL = "/api/banks"

    // This is how to do nested contexts in tests
    @Nested
    @DisplayName("GET /api/banks")
    // do not create a new instance of this inner class for every test case ran
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {
        // With nested tests, you can do the common test setup here
        // that applies only for this context.

        @Test
        fun `returns all banks`() {
            /* mockMvc also provides:
            mockMvc.post
            mockMvc.put
            mockMvc.delete
            */
            mockMvc.get(baseURL)
                // andDo gives us the mock handler to helps us to inspect what is going on in the test
                .andDo {
                    println("\nPrinting debugging info...")
                    print()
                }.andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    // jsonPath allows us to traverse a json and assert it
                    // - $ means root object
                    // - [0] means first element of the root object
                    // - get the account_number field
                    jsonPath("$[0].account_number") { value("1234") } //Taken from mock data source
                }

        }
    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `returns the bank with the given account number`() {
            val accountNumber = 1234
            mockMvc.get("$baseURL/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    // From root JSON, get the trust property
                    jsonPath("$.trust") { value("2.0") } //Taken from mock data source
                }

        }

        @Test
        fun `returns NOT_FOUND if the account number does not exist`() {
            val accountNumber = "does_not_exist"
            mockMvc.get("$baseURL/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        /* @DirtiesContext is a Spring test annotation that tells Spring that this particular test
        * mutates the state (context) of the application and that therefore the application context needs to be
        * reloaded after this test is ran to ensure that the tests are independent from each other.
        * Reloading context reduces the performance of your tests. However, Spring makes some caching to make it fast
        * to reload context that it has loaded before.
        * @DirtiesContext is a bit of a hack. It would probably be better to have an app-wide teardown strategy
        * that ensures test independence.
        * */
        @DirtiesContext
        fun `adds a new bank`() {
            val newBank = Bank("acc123", 31.415, 2)
            mockMvc.post(baseURL){
                contentType = MediaType.APPLICATION_JSON
                // Serialize to a JSON string
                content = objectMapper.writeValueAsString(newBank)
            }
                .andDo{ print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.account_number") { "acc123" }
                }
            mockMvc.get("$baseURL/${newBank.accountNumber}")
                .andExpect {
                    content { json(objectMapper.writeValueAsString(newBank)) }
                }
        }

        @Test
        fun `returns BAD REQUEST if given account number already exists`() {
            // This is relying on the fact that the MockBankDataSource already has a bank with
            // that account number.
            val invalidBank = Bank(accountNumber = "1234", trust = 1.0, transactionFee = 1)

            mockMvc.post(baseURL){
                contentType = MediaType.APPLICATION_JSON
                // Serialize to a JSON string
                content = objectMapper.writeValueAsString(invalidBank)
            }
                .andDo{ print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
        @Test
        @DirtiesContext
        fun `updates an existing bank`() {
            // This is relying on the fact that the MockBankDataSource already has a bank with
            // that account number.
            val updatedBank = Bank("1234", 10.0, 10)
            mockMvc.patch(baseURL){
                contentType = MediaType.APPLICATION_JSON
                // Serialize to a JSON string
                content = objectMapper.writeValueAsString(updatedBank)
            }
                .andDo{ print() }
                .andExpect {
                    status { isOk() }
                    content{
                        contentType(MediaType.APPLICATION_JSON)
                        /* An alternative way to assert JSON matching.
                        * Anything within `content` is called on the
                        * HTTP result. */
                        /*
                        This matcher asserts that the result has json
                        data and that it matches the given JSON.
                        */
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }
            mockMvc.get("$baseURL/${updatedBank.accountNumber}")
                .andExpect {
                    content { json(objectMapper.writeValueAsString(updatedBank)) }
                }
        }

        @Test
        fun `returns NOT FOUND if given account number does not exists`() {
            val invalidBank = Bank(accountNumber = "non-existent", trust = 1.0, transactionFee = 1)

            mockMvc.patch(baseURL){
                contentType = MediaType.APPLICATION_JSON
                // Serialize to a JSON string
                content = objectMapper.writeValueAsString(invalidBank)
            }
                .andDo{ print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteExistingBanks {
        @Test
        @DirtiesContext
        fun `deletes the bank with the given account number`() {
            val accountNumber = "1234"
            mockMvc.delete("$baseURL/$accountNumber")
                .andDo { print() }
                .andExpect {
                    // This is the best practice HTTP return status for after deletion
                    status { isNoContent() }
                }

            mockMvc.get("$baseURL/$accountNumber")
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `returns NOT FOUND if given account number does not exists`() {
            val accountNumber = "non-existent"

            mockMvc.delete("$baseURL/$accountNumber")
                .andDo{ print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
}