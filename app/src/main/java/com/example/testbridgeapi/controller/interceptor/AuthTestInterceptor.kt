package com.example.testbridgeapi.controller.interceptor

import io.github.shiniseong.bridgeApi.type.BridgeResponse
import io.github.shiniseong.bridgeApi.type.RequestContext
import io.github.shiniseong.bridgeApi.type.service.ServiceDecorator


class AuthTestInterceptor : ServiceDecorator() {
    override suspend fun serve(ctx: RequestContext): BridgeResponse {
        if (ctx.headers["Authorization"] != "Example Bearer token")
            println("AuthTestInterceptor: Unauthorized")

        return unwrap().serve(ctx)
    }
}