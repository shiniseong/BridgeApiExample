package com.example.testbridgeapi.controller.interceptor

import org.imtsoft.bridgeApi.type.BridgeResponse
import org.imtsoft.bridgeApi.type.RequestContext
import org.imtsoft.bridgeApi.type.service.ServiceDecorator

class LoggingTestInterceptor : ServiceDecorator() {
    override suspend fun serve(ctx: RequestContext): BridgeResponse {
        println("LoggingTestInterceptor: Before")
        val response = unwrap().serve(ctx)
        println("LoggingTestInterceptor: After")
        return response
    }
}