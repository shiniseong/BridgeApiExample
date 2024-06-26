package com.example.testbridgeapi.core.domain

import com.example.testbridgeapi.core.enums.UserType

data class User(
    val id: Long = 0,
    val name: String,
    val age: Int,
    val type: UserType,
)
