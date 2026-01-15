package com.leecoder.stockchart.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leecoder.stockchart.app.viewmodel.LoginSideEffect
import com.leecoder.stockchart.app.viewmodel.LoginViewModel
import com.leecoder.stockchart.design_system.component.BaseButton
import com.leecoder.stockchart.design_system.component.BaseDialog
import com.leecoder.stockchart.design_system.component.BaseTextField
import com.leecoder.stockchart.ui.extension.hide
import com.leecoder.stockchart.ui.extension.isShown
import com.leecoder.stockchart.ui.extension.rememberErrorDialogState
import com.leecoder.stockchart.ui.extension.show

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onSuccessed: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    var failureDialogState = rememberErrorDialogState<LoginSideEffect.Failure>()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.Success -> onSuccessed()
            is LoginSideEffect.Failure -> {
                failureDialogState.show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "볼린저 하한가 알리미",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "한국투자증권 API로 주식 정보를 확인하세요",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        BaseTextField(
            textFieldValue = state.appKeyField,
            hint = "APP KEY를 입력해주세요",
            maxLength = 200,
            onTextChanged = { viewModel.updateAppKeyField(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        BaseTextField(
            textFieldValue = state.appSecretField,
            hint = "APP SECRET을 입력해주세요",
            maxLength = 200,
            onTextChanged = { viewModel.updateAppSecretField(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        BaseButton(
            text = "로그인",
            enabled = state.appKeyField.isNotEmpty() && state.appSecretField.isNotEmpty(),
            onClick = { viewModel.login() }
        )
    }

    if (failureDialogState.isShown) {
        BaseDialog(
            title = "로그인 실패",
            description = "로그인 정보를 확인해 주세요.",
            code = null,
            onClickConfirm = {
                viewModel.cleanFields()
                failureDialogState.hide()
            }
        )
    }
}