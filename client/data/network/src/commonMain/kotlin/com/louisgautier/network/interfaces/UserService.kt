package com.louisgautier.network.interfaces

import com.louisgautier.apicontracts.dto.UserJson

interface UserService {
    suspend fun me(): Result<UserJson>
}
