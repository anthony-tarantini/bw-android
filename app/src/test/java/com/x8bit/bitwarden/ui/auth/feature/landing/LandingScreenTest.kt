package com.x8bit.bitwarden.ui.auth.feature.landing

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.x8bit.bitwarden.ui.platform.base.BaseComposeTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class LandingScreenTest : BaseComposeTest() {
    @Test
    fun `continue button should be enabled or disabled according to the state`() {
        val mutableStateFlow = MutableStateFlow(
            LandingState(
                emailInput = "",
                isContinueButtonEnabled = true,
                isRememberMeEnabled = false,
            ),
        )
        val viewModel = mockk<LandingViewModel>(relaxed = true) {
            every { eventFlow } returns emptyFlow()
            every { stateFlow } returns mutableStateFlow
        }
        composeTestRule.setContent {
            LandingScreen(
                onNavigateToCreateAccount = {},
                onNavigateToLogin = {},
                viewModel = viewModel,
            )
        }
        composeTestRule.onNodeWithText("Continue").assertIsEnabled()

        mutableStateFlow.update { it.copy(isContinueButtonEnabled = false) }

        composeTestRule.onNodeWithText("Continue").assertIsNotEnabled()
    }

    @Test
    fun `continue button click should send ContinueButtonClick action`() {
        val viewModel = mockk<LandingViewModel>(relaxed = true) {
            every { eventFlow } returns emptyFlow()
            every { stateFlow } returns MutableStateFlow(
                LandingState(
                    emailInput = "",
                    isContinueButtonEnabled = true,
                    isRememberMeEnabled = false,
                ),
            )
        }
        composeTestRule.setContent {
            LandingScreen(
                onNavigateToCreateAccount = {},
                onNavigateToLogin = {},
                viewModel = viewModel,
            )
        }
        composeTestRule.onNodeWithText("Continue").performClick()
        verify {
            viewModel.trySendAction(LandingAction.ContinueButtonClick)
        }
    }

    @Test
    fun `remember me click should send RememberMeToggle action`() {
        val viewModel = mockk<LandingViewModel>(relaxed = true) {
            every { eventFlow } returns emptyFlow()
            every { stateFlow } returns MutableStateFlow(
                LandingState(
                    emailInput = "",
                    isContinueButtonEnabled = true,
                    isRememberMeEnabled = false,
                ),
            )
        }
        composeTestRule.setContent {
            LandingScreen(
                onNavigateToCreateAccount = {},
                onNavigateToLogin = {},
                viewModel = viewModel,
            )
        }
        composeTestRule
            .onNodeWithText("Remember me")
            .onChildren()
            .filterToOne(hasClickAction())
            .performClick()
        verify {
            viewModel.trySendAction(LandingAction.RememberMeToggle(true))
        }
    }

    @Test
    fun `create account click should send CreateAccountClick action`() {
        val viewModel = mockk<LandingViewModel>(relaxed = true) {
            every { eventFlow } returns emptyFlow()
            every { stateFlow } returns MutableStateFlow(
                LandingState(
                    emailInput = "",
                    isContinueButtonEnabled = true,
                    isRememberMeEnabled = false,
                ),
            )
        }
        composeTestRule.setContent {
            LandingScreen(
                onNavigateToCreateAccount = {},
                onNavigateToLogin = {},
                viewModel = viewModel,
            )
        }
        composeTestRule.onNodeWithText("Create account").performScrollTo().performClick()
        verify {
            viewModel.trySendAction(LandingAction.CreateAccountClick)
        }
    }

    @Test
    fun `email address change should send EmailInputChanged action`() {
        val input = "email"
        val viewModel = mockk<LandingViewModel>(relaxed = true) {
            every { eventFlow } returns emptyFlow()
            every { stateFlow } returns MutableStateFlow(
                LandingState(
                    emailInput = "",
                    isContinueButtonEnabled = true,
                    isRememberMeEnabled = false,
                ),
            )
        }
        composeTestRule.setContent {
            LandingScreen(
                onNavigateToCreateAccount = {},
                onNavigateToLogin = {},
                viewModel = viewModel,
            )
        }
        composeTestRule.onNodeWithText("Email address").performTextInput(input)
        verify {
            viewModel.trySendAction(LandingAction.EmailInputChanged(input))
        }
    }

    @Test
    fun `NavigateToCreateAccount event should call onNavigateToCreateAccount`() {
        var onNavigateToCreateAccountCalled = false
        val viewModel = mockk<LandingViewModel>(relaxed = true) {
            every { eventFlow } returns flowOf(LandingEvent.NavigateToCreateAccount)
            every { stateFlow } returns MutableStateFlow(
                LandingState(
                    emailInput = "",
                    isContinueButtonEnabled = true,
                    isRememberMeEnabled = false,
                ),
            )
        }
        composeTestRule.setContent {
            LandingScreen(
                onNavigateToCreateAccount = { onNavigateToCreateAccountCalled = true },
                onNavigateToLogin = {},
                viewModel = viewModel,
            )
        }
        assert(onNavigateToCreateAccountCalled)
    }

    @Test
    fun `NavigateToLogin event should call onNavigateToLogin`() {
        val testEmail = "test@test.com"
        var onNavigateToLoginEmail = ""
        val viewModel = mockk<LandingViewModel>(relaxed = true) {
            every { eventFlow } returns flowOf(LandingEvent.NavigateToLogin(testEmail))
            every { stateFlow } returns MutableStateFlow(
                LandingState(
                    emailInput = "",
                    isContinueButtonEnabled = true,
                    isRememberMeEnabled = false,
                ),
            )
        }
        composeTestRule.setContent {
            LandingScreen(
                onNavigateToCreateAccount = { },
                onNavigateToLogin = { onNavigateToLoginEmail = it },
                viewModel = viewModel,
            )
        }
        assertEquals(testEmail, onNavigateToLoginEmail)
    }
}
