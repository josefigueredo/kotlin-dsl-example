package com.josefigueredo.dslexample

import com.josefigueredo.dslexample.config.beans
import com.josefigueredo.dslexample.config.dotenv
import java.time.Duration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.DisposableServer
import reactor.netty.http.server.HttpServer

class Application(port: Int = dotenv["PORT"]?.toInt() ?: 8080) {

    private val httpHandler: HttpHandler
    private val server: HttpServer
    private var disposableServer: DisposableServer? = null

    init {
        val context = GenericApplicationContext().apply {
            beans().initialize(this)
            refresh()
        }
        httpHandler = WebHttpHandlerBuilder.applicationContext(context).build()
        val adapter = ReactorHttpHandlerAdapter(httpHandler)
        server = HttpServer.create().host("localhost").handle(adapter).port(port) // port)
    }

    fun start() {
        disposableServer = server.bindNow(Duration.ofSeconds(5))
    }

    fun startAndAwait() {
        server.bindUntilJavaShutdown(Duration.ofSeconds(5)) {
            disposableServer = it
        }
    }

    fun stop() {
        disposableServer?.disposeNow()
    }
}

fun main(args: Array<String>) {
    Application().startAndAwait()
}