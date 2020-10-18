package com.josefigueredo.dslexample.config

import com.josefigueredo.dslexample.btc.BtcHandler
import com.josefigueredo.dslexample.common.internalServerError
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.router
import java.net.URI

class Routes(private val btcHandler: BtcHandler) {

    fun router() = router {
        accept(TEXT_HTML).nest {
            GET("/") { permanentRedirect(URI("index.html")).build() }
        }
        "/api".nest {
            accept(APPLICATION_JSON).nest {
                POST("/btcByDate", btcHandler::findPrice)
                POST("/fetchBtc", btcHandler::savePrice)
            }
        }
        resources("/**", ClassPathResource("static/"))
    }
        .filter { request, next ->
            try {
                next.handle(request)
            } catch (ex: Exception) {
                internalServerError(ex)
            }
        }
}