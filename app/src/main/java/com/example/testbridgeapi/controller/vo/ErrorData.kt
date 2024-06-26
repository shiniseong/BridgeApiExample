package com.example.testbridgeapi.controller.vo

import java.time.LocalDateTime
import java.util.*

data class ErrorData(
    val uniqueCode: String = "UniqueCode",
    val message: String = "Unknown error",
    val errorType: String = "ErrorType",
    val occurredAt: LocalDateTime = LocalDateTime.now(),
) {
    constructor(cause: Throwable) : this(
        uniqueCode = UUID.randomUUID().toString(),
        message = cause.message ?: "Unknown error",
        errorType = cause::class.simpleName ?: "ErrorType",
        occurredAt = LocalDateTime.now(),
    )
}
