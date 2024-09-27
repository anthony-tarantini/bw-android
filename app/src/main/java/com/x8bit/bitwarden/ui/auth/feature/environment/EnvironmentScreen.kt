package com.x8bit.bitwarden.ui.auth.feature.environment

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.x8bit.bitwarden.R
import com.x8bit.bitwarden.ui.platform.base.util.EventsEffect
import com.x8bit.bitwarden.ui.platform.base.util.asText
import com.x8bit.bitwarden.ui.platform.components.appbar.BitwardenTopAppBar
import com.x8bit.bitwarden.ui.platform.components.button.BitwardenTextButton
import com.x8bit.bitwarden.ui.platform.components.dialog.BasicDialogState
import com.x8bit.bitwarden.ui.platform.components.dialog.BitwardenBasicDialog
import com.x8bit.bitwarden.ui.platform.components.field.BitwardenTextField
import com.x8bit.bitwarden.ui.platform.components.header.BitwardenListHeaderText
import com.x8bit.bitwarden.ui.platform.components.scaffold.BitwardenScaffold
import com.x8bit.bitwarden.ui.platform.components.util.rememberVectorPainter

/**
 * Displays the about self-hosted/custom environment screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvironmentScreen(
    onNavigateBack: () -> Unit,
    viewModel: EnvironmentViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is EnvironmentEvent.NavigateBack -> onNavigateBack.invoke()
            is EnvironmentEvent.ShowToast -> {
                Toast.makeText(context, event.message(context.resources), Toast.LENGTH_SHORT).show()
            }
        }
    }

    BitwardenBasicDialog(
        visibilityState = if (state.shouldShowErrorDialog) {
            BasicDialogState.Shown(
                title = R.string.an_error_has_occurred.asText(),
                message = R.string.environment_page_urls_error.asText(),
            )
        } else {
            BasicDialogState.Hidden
        },
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(EnvironmentAction.ErrorDialogDismiss) }
        },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    BitwardenScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BitwardenTopAppBar(
                title = stringResource(id = R.string.settings),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = R.drawable.ic_close),
                navigationIconContentDescription = stringResource(id = R.string.close),
                onNavigationIconClick = remember(viewModel) {
                    { viewModel.trySendAction(EnvironmentAction.CloseClick) }
                },
                actions = {
                    BitwardenTextButton(
                        label = stringResource(id = R.string.save),
                        onClick = remember(viewModel) {
                            { viewModel.trySendAction(EnvironmentAction.SaveClick) }
                        },
                        modifier = Modifier.testTag("SaveButton"),
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            BitwardenListHeaderText(
                label = stringResource(id = R.string.self_hosted_environment),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BitwardenTextField(
                label = stringResource(id = R.string.server_url),
                value = state.serverUrl,
                placeholder = "ex. https://bitwarden.company.com",
                hint = stringResource(id = R.string.self_hosted_environment_footer),
                onValueChange = remember(viewModel) {
                    { viewModel.trySendAction(EnvironmentAction.ServerUrlChange(it)) }
                },
                keyboardType = KeyboardType.Uri,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ServerUrlEntry")
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            BitwardenListHeaderText(
                label = stringResource(id = R.string.custom_environment),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BitwardenTextField(
                label = stringResource(id = R.string.web_vault_url),
                value = state.webVaultServerUrl,
                onValueChange = remember(viewModel) {
                    { viewModel.trySendAction(EnvironmentAction.WebVaultServerUrlChange(it)) }
                },
                keyboardType = KeyboardType.Uri,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("WebVaultUrlEntry")
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BitwardenTextField(
                label = stringResource(id = R.string.api_url),
                value = state.apiServerUrl,
                onValueChange = remember(viewModel) {
                    { viewModel.trySendAction(EnvironmentAction.ApiServerUrlChange(it)) }
                },
                keyboardType = KeyboardType.Uri,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ApiUrlEntry")
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BitwardenTextField(
                label = stringResource(id = R.string.identity_url),
                value = state.identityServerUrl,
                onValueChange = remember(viewModel) {
                    { viewModel.trySendAction(EnvironmentAction.IdentityServerUrlChange(it)) }
                },
                keyboardType = KeyboardType.Uri,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("IdentityUrlEntry")
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BitwardenTextField(
                label = stringResource(id = R.string.icons_url),
                value = state.iconsServerUrl,
                onValueChange = remember(viewModel) {
                    { viewModel.trySendAction(EnvironmentAction.IconsServerUrlChange(it)) }
                },
                hint = stringResource(id = R.string.custom_environment_footer),
                keyboardType = KeyboardType.Uri,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("IconsUrlEntry")
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BitwardenTextField(
                label = stringResource(id = R.string.cloudflare_access_client_id),
                value = state.cloudflareClientId,
                onValueChange = remember(viewModel) {
                    { viewModel.trySendAction(EnvironmentAction.CloudflareClientIdChange(it)) }
                },
                hint = stringResource(id = R.string.cloudflare_hint),
                keyboardType = KeyboardType.Text,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("CloudflareClientIdEntry")
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BitwardenTextField(
                label = stringResource(id = R.string.cloudflare_access_client_secret),
                value = state.cloudflareClientSecret,
                onValueChange = remember(viewModel) {
                    { viewModel.trySendAction(EnvironmentAction.CloudflareClientSecretChange(it)) }
                },
                hint = stringResource(id = R.string.cloudflare_hint),
                keyboardType = KeyboardType.Text,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("CloudflareClientSecretEntry")
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
