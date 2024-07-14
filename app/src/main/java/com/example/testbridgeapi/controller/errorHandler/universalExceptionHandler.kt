package com.example.testbridgeapi.controller.errorHandler

import com.example.testbridgeapi.controller.vo.ErrorData
import dto.res.ApiCommonResponse
import io.github.shiniseong.bridgeApi.type.ErrorHandler

val universalExceptionHandler = ErrorHandler { throwable ->
    ApiCommonResponse(
        status = -99,
        message = throwable.message ?: "Unknown error",
        data = ErrorData(throwable),
    )
}