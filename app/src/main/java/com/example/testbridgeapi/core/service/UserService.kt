package com.example.testbridgeapi.core.service

import com.example.testbridgeapi.core.domain.User
import com.example.testbridgeapi.core.exceptions.TestGeneralThrowable
import com.example.testbridgeapi.core.exceptions.TestServiceException
import com.example.testbridgeapi.repository.UserRepository

// UserService.kt


class UserService(private val userRepository: UserRepository) {
    fun getUserById(id: Long): User {
        return userRepository.findById(id) ?: throw NoSuchElementException("User not found with id: $id")
    }

    fun createUser(user: User): User {
        return userRepository.save(user)
    }

    fun updateUserType(id: Long, user: User): User {
        val existingUser = getUserById(id)
        val updatedUser = existingUser.copy(type = user.type)
        return userRepository.update(updatedUser) ?: throw IllegalStateException("Failed to update user")
    }

    fun deleteUser(id: Long) {
        userRepository.delete(id)
    }

    fun getAllUsers(order: String): List<User> {
        val users = userRepository.findAll()
        return if (order == "ASC") users.sortedBy { it.id } else users.sortedByDescending { it.id }
    }

    fun testExceptionService() {
        throw TestServiceException("test service exception")
    }

    fun testExceptionGeneral() {
        throw TestGeneralThrowable("test general exception")
    }
}