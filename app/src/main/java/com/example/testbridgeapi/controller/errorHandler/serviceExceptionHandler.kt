package com.example.testbridgeapi.controller.errorHandler

import com.example.testbridgeapi.controller.vo.ErrorData
import com.example.testbridgeapi.core.exceptions.TestServiceException
import dto.res.ApiCommonResponse
import org.imtsoft.bridgeApi.type.ErrorHandler

val serviceExceptionHandler = ErrorHandler { throwable ->
    when (throwable) {
        is TestServiceException -> {
            ApiCommonResponse(
                status = 1,
                message = throwable.message ?: "Unknown error",
                data = ErrorData(throwable),
            )
        }

        else -> null
    }
}