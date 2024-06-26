package com.example.testbridgeapi.controller.dto.req

import com.example.testbridgeapi.core.domain.User
import com.example.testbridgeapi.core.enums.UserType


data class UserReqDto(
    val name: String,
    val age: Int,
    val type: Int,
) {
    fun toDomain(id: Long = 0): User = User(
        id = id,
        name = name,
        age = age,
        type = UserType.from(type),
    )
}
