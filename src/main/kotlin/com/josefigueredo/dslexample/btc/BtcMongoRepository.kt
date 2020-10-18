package com.josefigueredo.dslexample.btc

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query.query
import reactor.core.publisher.Mono
import java.time.LocalDateTime

class BtcMongoRepository(private val reactiveMongoTemplate: ReactiveMongoTemplate) {

    fun findByCreatedDate(time: LocalDateTime): Mono<BitcoinPriceDocument> =
        reactiveMongoTemplate.findOne(
            query(
                Criteria("createdDate").`is`(time)
            )
        )

    fun save(bitcoinPriceDocument: BitcoinPriceDocument) =
        reactiveMongoTemplate.save(bitcoinPriceDocument)
}