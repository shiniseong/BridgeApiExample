package com.example.testbridgeapi.controller.serializer

import com.example.testbridgeapi.core.enums.UserType
import com.fasterxml.jackson.databind.module.SimpleModule

class JacksonCustomSerializeModule : SimpleModule() {
    init {
        addSerializer(UserType::class.java, UserTypeSerializer.Serializer())
        addDeserializer(UserType::class.java, UserTypeSerializer.Deserializer())
    }
}

