package com.louisgautier.network

import com.louisgautier.apicontracts.pojo.UserTokenJson
import com.louisgautier.core.AppPreferences
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class ApiClientTest {

    private val mockPreferences = mock<AppPreferences>(MockMode.autofill)

    @Test
    fun `assert that call function is catching malformed response properly`() {

        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.OK,
            )
        }

        val client = ApiClient(mockEngine, mockPreferences)

        runBlocking {
            val response: Result<UserTokenJson> = client.refresh(UserTokenJson("", ""))
            assertTrue(response.isFailure)
        }
    }

    @Test
    fun `assert that call function is catching response != 2xx properly`() {

        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.Unauthorized,
            )
        }

        val client = ApiClient(mockEngine, mockPreferences)

        runBlocking {
            val response: Result<String> = client.unprotected()
            assertTrue(response.isFailure)
        }
    }

    @Test
    fun `assert that call function is catching timeout properly`() {

        val mockEngine = MockEngine { request ->
            throw HttpRequestTimeoutException(request.url.toString(), 1000)
        }

        val client = ApiClient(mockEngine, mockPreferences)

        runBlocking {
            val response: Result<String> = client.unprotected()
            assertTrue(response.isFailure)
        }
    }

    @Test
    fun `assert that token is send properly`() {

        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.OK,
            )
        }

        everySuspend { mockPreferences.getUserToken() } returns "token"
        everySuspend { mockPreferences.getUserRefreshToken() } returns "refresh_token"

        val client = ApiClient(mockEngine, mockPreferences)


        runBlocking {
            client.unprotected()
            client.protected()

            assertTrue(mockEngine.requestHistory[0].headers["Authorization"] == null)
            assertTrue(mockEngine.requestHistory[1].headers["Authorization"] != null)
        }
    }

}