package com.josefigueredo.dslexample.config

import com.mongodb.ConnectionString
import com.josefigueredo.dslexample.btc.BtcHandler
import com.josefigueredo.dslexample.btc.BtcMongoRepository
import com.josefigueredo.dslexample.btc.BtcService
import org.springframework.context.support.beans
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.adapter.WebHttpHandlerBuilder

fun beans() = beans {
    bean {
        BtcMongoRepository(ref())
    }
    bean {
        ReactiveMongoTemplate(
            SimpleReactiveMongoDatabaseFactory(
                //ConnectionString(env["mongo.uri"])
                ConnectionString("mongodb://localhost:27017/btc")
            )
        )
    }
    bean<BtcService>()
    bean<BtcHandler>()
    bean<Routes>()
    bean(WebHttpHandlerBuilder.WEB_HANDLER_BEAN_NAME) {
        RouterFunctions.toWebHandler(ref<Routes>().router())
    }
}

class Foo