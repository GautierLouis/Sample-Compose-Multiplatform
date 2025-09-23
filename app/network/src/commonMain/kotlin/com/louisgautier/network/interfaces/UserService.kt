package com.louisgautier.network.interfaces

import com.louisgautier.apicontracts.pojo.UserJson

interface UserService {
    suspend fun me(): Result<UserJson>
}