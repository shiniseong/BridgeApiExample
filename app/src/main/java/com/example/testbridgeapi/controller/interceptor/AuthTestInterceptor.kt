package com.example.testbridgeapi.controller.interceptor

import org.imtsoft.bridgeApi.type.BridgeResponse
import org.imtsoft.bridgeApi.type.RequestContext
import org.imtsoft.bridgeApi.type.service.ServiceDecorator


class AuthTestInterceptor : ServiceDecorator() {
    override suspend fun serve(ctx: RequestContext): BridgeResponse {
        if (ctx.headers["Authorization"] != "Example Bearer token")
            println("AuthTestInterceptor: Unauthorized")

        return unwrap().serve(ctx)
    }
}