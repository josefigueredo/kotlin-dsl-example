package com.josefigueredo.dslexample.btc

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BtcService(
    val btcRepository: BtcMongoRepository
) {

    val webClient = WebClient
        .builder()
        .baseUrl("https://blockchain.info")
        .exchangeStrategies(
            ExchangeStrategies
                .builder()
                .codecs { clientDefaultCodecsConfigurer: ClientCodecConfigurer ->
                    clientDefaultCodecsConfigurer.defaultCodecs()
                        .jackson2JsonEncoder(Jackson2JsonEncoder(ObjectMapper(), MediaType.APPLICATION_JSON))
                    clientDefaultCodecsConfigurer.defaultCodecs()
                        .jackson2JsonDecoder(Jackson2JsonDecoder(ObjectMapper(), MediaType.APPLICATION_JSON))
                }.build()
        )
        .build()

    val formatDouble = { d: Double -> String.format("%.12f", d) }

    fun findPrice(findPrice: FindPriceRequest) =
        btcRepository
            .findByCreatedDate(LocalDateTime.parse(findPrice.time, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .flatMap { btc ->
                Mono.just(
                    FindPriceResponse(
                        formatDouble(btc.price)
                    )
                )
            }

    fun saveBtc2UsdPrice() =
        webClient
            .method(HttpMethod.GET)
            .uri("/tobtc?currency=USD&value=1")
            .accept(MediaType.TEXT_PLAIN)
            .retrieve()
            .bodyToMono<String>()
            .flatMap { price ->
                Mono.just(
                    BitcoinPriceDocument(price.toDouble())
                )
            }
            .flatMap { btcRepository.save(it) }
            .flatMap {
                Mono.just(
                    FetchBtcResponse(
                        formatDouble(it.price),
                        it.createdDate.toString()
                    )
                )
            }

}