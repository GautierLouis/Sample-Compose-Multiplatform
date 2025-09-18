package com.louisgautier.sample.server.domain

import com.louisgautier.sample.server.database.entity.StoredUser

internal class UserRepository() {

    //Make a real database
    private val users = mutableListOf(
        StoredUser(1, "test", "test"),
        StoredUser(2, "louis", "abc123")
    )

    fun getUserById(id: Int): StoredUser? {
        return users.firstOrNull { it.id == id }
    }

    fun updateUser(user: StoredUser) {
        users.remove(users.first { it.username == user.username })
        users.add(user)
    }

    fun deleteUser(username: String) {
        users.remove(users.first { it.username == username })
    }
}