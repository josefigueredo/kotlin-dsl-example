package com.josefigueredo.dslexample.btc

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime


// Req
data class FindPriceRequest(
    var time: String
)

// Resp
data class FindPriceResponse(
    var price: String
)

data class FetchBtcResponse(
    var price: String,
    var time: String
)

// DB
@Document
data class BitcoinPriceDocument(
    var price: Double,
    var createdDate: LocalDateTime = LocalDateTime.now()
)
