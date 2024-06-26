package com.example.testbridgeapi.controller.serializer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


val objectMapper: ObjectMapper = jacksonObjectMapper()
    .findAndRegisterModules()
    .registerModule(JacksonCustomSerializeModule())
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

inline fun <reified T> String.deserializeFromJson(): T = objectMapper.readValue(this, object : TypeReference<T>() {})

fun Any.serializeToJson(): String = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
