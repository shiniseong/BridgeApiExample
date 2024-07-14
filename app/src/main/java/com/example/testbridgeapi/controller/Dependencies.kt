package com.example.testbridgeapi.controller

import UserController
import com.example.testbridgeapi.controller.errorHandler.serviceExceptionHandler
import com.example.testbridgeapi.controller.errorHandler.universalExceptionHandler
import com.example.testbridgeapi.controller.interceptor.AuthTestInterceptor
import com.example.testbridgeapi.controller.interceptor.LoggingTestInterceptor
import com.example.testbridgeapi.controller.serializer.objectMapper
import com.example.testbridgeapi.core.service.UserService
import com.example.testbridgeapi.repository.UserRepository
import io.github.shiniseong.bridgeApi.BridgeRouter


val userRepository = UserRepository()
val userService = UserService(userRepository)
val userController = UserController(userService)

val router = BridgeRouter.builder().apply {
    setSerializer(objectMapper)
    registerAllErrorHandlers(listOf(serviceExceptionHandler, universalExceptionHandler))
    registerDecorator(AuthTestInterceptor())
    registerDecorator(LoggingTestInterceptor())
    registerController("api/v1/users", userController)
}.build()

