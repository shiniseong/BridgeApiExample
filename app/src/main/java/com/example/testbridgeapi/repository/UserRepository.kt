package com.example.testbridgeapi.repository

import com.example.testbridgeapi.core.domain.User
import com.example.testbridgeapi.core.enums.UserType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class UserRepository {
    private val users = ConcurrentHashMap<Long, User>()
    private val idGenerator = AtomicLong(0)

    init {
        // 더미 유저 5명 추가
        val dummyUsers = listOf(
            User(name = "John Doe", age = 30, type = UserType.ADMIN),
            User(name = "Jane Smith", age = 28, type = UserType.SELLER),
            User(name = "Mike Johnson", age = 35, type = UserType.BUYER),
            User(name = "Emily Brown", age = 26, type = UserType.SELLER),
            User(name = "David Wilson", age = 40, type = UserType.BUYER)
        )

        dummyUsers.forEach { save(it) }
    }

    fun save(user: User): User {
        val id = if (user.id == 0L) idGenerator.incrementAndGet() else user.id
        val savedUser = user.copy(id = id)
        users[id] = savedUser
        return savedUser
    }

    fun findById(id: Long): User? = users[id]

    fun findAll(): List<User> = users.values.toList()

    fun delete(id: Long) {
        users.remove(id)
    }

    fun update(user: User): User? {
        return user.id.let { id ->
            users.computeIfPresent(id) { _, _ -> user }
        }
    }
}