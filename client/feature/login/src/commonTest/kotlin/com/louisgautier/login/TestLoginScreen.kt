package com.louisgautier.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.louisgautier.auth.AuthRepository
import dev.mokkery.MockMode
import dev.mokkery.coroutines.answering.Awaitable.Companion.delayed
import dev.mokkery.coroutines.answering.awaits
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalTestApi::class)
class TestLoginScreen {

    private val mockAuthRepository = mock<AuthRepository>(MockMode.autofill)

    /*
    Component to test
     */
    private fun SemanticsNodeInteractionsProvider.btnLabel() = onNodeWithTag("btnLabel")
    private fun SemanticsNodeInteractionsProvider.btn() = onNodeWithTag("login")

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testLogin() = runComposeUiTest {
        val viewModel = LoginViewModel(mockAuthRepository)

        everySuspend {
            mockAuthRepository.login(any(), any())
        } awaits delayed(by = 2.seconds) { Result.success(Unit) }

        setContent {
            LoginScreen(viewModel) { }
        }

        //Default state
        btnLabel().assertTextEquals("Login")
        btn().assertIsEnabled()

        //Action
        btn().performClick()

        //Loading (temporary state ~2 seconds)
        btnLabel().assertTextEquals("Loading")
        btn().assertIsNotEnabled()

        //Final state
        btn().assertIsEnabled()
        btnLabel().assertTextEquals("Login")

    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testLogin2() = runComposeUiTest {
        val viewModel = LoginViewModel(mockAuthRepository)

        everySuspend {
            mockAuthRepository.login(any(), any())
        } awaits delayed(by = 2.seconds) { Result.success(Unit) }


        setContent {
            LoginScreen(viewModel) {}
        }

        //Default state
        btnLabel().assertTextEquals("Login")
        btn().assertIsEnabled()

        //Action
        btn().performClick()

        //Loading (temporary state ~2 seconds)
        btnLabel().assertTextEquals("Loading")
        btn().assertIsNotEnabled()

        //Final state
        btn().assertIsEnabled()
        btnLabel().assertTextEquals("Login")

    }
}