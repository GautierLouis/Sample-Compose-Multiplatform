package com.louisgautier.login

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.louisgautier.auth.AuthRepository
import dev.mokkery.MockMode
import dev.mokkery.annotations.DelicateMokkeryApi
import dev.mokkery.coroutines.answering.Awaitable.Companion.delayed
import dev.mokkery.coroutines.answering.awaits
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

// Simple LifecycleOwner for testing
class TestLifecycleOwner : LifecycleOwner {
    private val registry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle
        get() = registry

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        registry.handleLifecycleEvent(event)
    }
}

@OptIn(ExperimentalTestApi::class, ExperimentalCoroutinesApi::class)
class TestLoginScreen {

    private val mockAuthRepository = mock<AuthRepository>(MockMode.autofill)
    private val testLifecycleOwner = TestLifecycleOwner()

    @BeforeTest
    fun before() {
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    @AfterTest
    fun after() {
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    @OptIn(DelicateMokkeryApi::class, ExperimentalTime::class)
    @Test
    fun `check that the state is correctly update with a success`() = runComposeUiTest {

        val viewModel = LoginViewModel(mockAuthRepository)

        everySuspend {
            mockAuthRepository.login(any(), any())
        } awaits delayed { Result.success(Unit) }

        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides testLifecycleOwner) {
                LoginScreen(viewModel) {
                    assertTrue(true)
                }
            }
        }

        //Default state
        onNodeWithTag("loginBtn")
            .assertIsEnabled()
            .assertTextEquals("Login")

        // perform action
        onNodeWithTag("loginBtn").performClick()

        onNodeWithTag("loginBtn")
            .assertTextEquals("Loading")
            .assertIsNotEnabled()

        waitUntil("End of Loading state") {
            runCatching {
                onNodeWithTag("loginBtn")
                    .assertIsEnabled()
                    .assertTextEquals("Login")
                true
            }.getOrDefault(false)
        }
    }

    @OptIn(DelicateMokkeryApi::class, ExperimentalTime::class)
    @Test
    fun `check that the state is correctly update with a failure`() = runComposeUiTest {

        val viewModel = LoginViewModel(mockAuthRepository)

        everySuspend {
            mockAuthRepository.login(any(), any())
        } awaits delayed { Result.failure(Exception()) }

        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides testLifecycleOwner) {
                LoginScreen(viewModel) {
                    assertTrue(false)
                }
            }
        }

        //Default state
        onNodeWithTag("loginBtn")
            .assertIsEnabled()
            .assertTextEquals("Login")

        // perform action
        onNodeWithTag("loginBtn").performClick()

        onNodeWithTag("loginBtn")
            .assertTextEquals("Loading")
            .assertIsNotEnabled()

        waitUntil("End of Loading state") {
            runCatching {
                onNodeWithTag("loginBtn")
                    .assertIsEnabled()
                    .assertTextEquals("Login")
                onNodeWithTag("snackbar")
                    .isDisplayed()
                true
            }.getOrDefault(false)
        }
    }
}