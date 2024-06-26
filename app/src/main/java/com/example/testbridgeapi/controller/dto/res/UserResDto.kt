package com.example.testbridgeapi.controller.dto.res

import com.example.testbridgeapi.core.domain.User
import com.example.testbridgeapi.core.enums.UserType

data class UserResDto(
    val id: Long,
    val name: String,
    val age: Int,
    val type: UserType,
)

fun User.toResDto(): UserResDto = UserResDto(
    id = id,
    name = name,
    age = age,
    type = type,
)
