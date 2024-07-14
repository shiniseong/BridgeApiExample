package com.example.testbridgeapi.controller.interceptor

import io.github.shiniseong.bridgeApi.type.BridgeResponse
import io.github.shiniseong.bridgeApi.type.RequestContext
import io.github.shiniseong.bridgeApi.type.service.ServiceDecorator

class LoggingTestInterceptor : ServiceDecorator() {
    override suspend fun serve(ctx: RequestContext): BridgeResponse {
        println("LoggingTestInterceptor: Before")
        val response = unwrap().serve(ctx)
        println("LoggingTestInterceptor: After")
        return response
    }
}