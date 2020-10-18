package com.josefigueredo.dslexample.btc


import com.josefigueredo.dslexample.config.validate
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

class BtcHandler(val btcService: BtcService) {

    fun findPrice(req: ServerRequest): Mono<ServerResponse> = validate
        .request(req)
        .withBody(FindPriceRequest::class.java) { findPrice ->
            ServerResponse.ok().body(btcService.findPrice(findPrice))
        }

    fun savePrice(req: ServerRequest): Mono<ServerResponse> = validate
        .request(req) {
            ServerResponse.ok().body(btcService.saveBtc2UsdPrice())
        }
}