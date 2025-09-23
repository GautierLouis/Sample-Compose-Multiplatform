package com.louisgautier.network

import com.louisgautier.apicontracts.dto.UserRefreshTokenJson
import com.louisgautier.apicontracts.dto.UserTokenJson
import com.louisgautier.network.interfaces.TokenAccessor
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertTrue

class ApiClientTest {

    private val mockTokenAccessor = mock<TokenAccessor>(MockMode.autofill)

    @Test
    fun `assert that call function is catching malformed response properly`() {

        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.OK,
            )
        }

        val client = DefaultService(mockEngine, mockTokenAccessor)
        val service = DefaultAuthService(client.unauthedClient)

        runBlocking {
            val response = service.forceRefresh(UserRefreshTokenJson(""))
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

        val client = DefaultService(mockEngine, mockTokenAccessor)
        val service = DefaultAuthService(client.unauthedClient)

        runBlocking {
            val response = service.registerAnon()
            assertTrue(response.isFailure)
        }
    }

    @Test
    fun `assert that call function is catching response == 2xx properly`() {


        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(Json.encodeToString(UserTokenJson("", "", 0L))),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = DefaultService(mockEngine, mockTokenAccessor)
        val service = DefaultAuthService(client.unauthedClient)

        runBlocking {
            val response = service.registerAnon()
            assertTrue(response.isSuccess)
        }
    }

    @Test
    fun `assert that call function is catching timeout properly`() {

        val mockEngine = MockEngine { request ->
            throw HttpRequestTimeoutException(request.url.toString(), 1000)
        }

        val client = DefaultService(mockEngine, mockTokenAccessor)
        val service = DefaultAuthService(client.unauthedClient)

        runBlocking {
            val response = service.registerAnon()
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

        everySuspend { mockTokenAccessor.getUserToken() } returns "token"
        everySuspend { mockTokenAccessor.getUserRefreshToken() } returns "refresh_token"

        val client = DefaultService(mockEngine, mockTokenAccessor)
        val authService = DefaultAuthService(client.unauthedClient)
        val unauthService = DefaultUserService(client.authedClient)

        runBlocking {
            authService.registerAnon()
            unauthService.me()

            assertTrue(mockEngine.requestHistory[0].headers["Authorization"] == null)
            assertTrue(mockEngine.requestHistory[1].headers["Authorization"] != null)
        }
    }
}