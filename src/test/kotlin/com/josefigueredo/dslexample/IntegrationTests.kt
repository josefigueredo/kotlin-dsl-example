package com.josefigueredo.dslexample

import com.josefigueredo.dslexample.btc.FindPriceRequest
import com.josefigueredo.dslexample.btc.FindPriceResponse
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.kotlin.test.test

class IntegrationTests {

    private val application = Application(8181)
    private val client = WebClient.create("http://localhost:8181")

    @BeforeAll
    fun beforeAll() {
        application.start()
    }

    @Test
    fun `Find all users on via users endpoint`() {
        client.post().uri("/api/btcByDate")
            .accept(APPLICATION_JSON)
            .bodyValue(FindPriceRequest("2020-10-18T14:40:05.956"))
            .retrieve()
            .bodyToFlux<FindPriceResponse>()
            .test()
            .expectNextMatches { it.price.toDouble() > 0 }
            .verifyComplete()
    }

    @AfterAll
    fun afterAll() {
        application.stop()
    }
}