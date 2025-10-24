package com.leecoder.stockchart.app.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.leecoder.stockchart.app.viewmodel.MainSideEffect
import com.leecoder.stockchart.app.viewmodel.MainViewModel
import com.leecoder.stockchart.design_system.component.BaseDialog
import com.leecoder.stockchart.ui.extension.hide
import com.leecoder.stockchart.ui.extension.isShown
import com.leecoder.stockchart.ui.extension.rememberErrorDialogState
import com.leecoder.stockchart.ui.extension.show

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {

    var errorDialogState = rememberErrorDialogState<MainSideEffect.TokenErrorPopup>()

    LaunchedEffect(Unit) {
        viewModel.postToken()
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffect.TokenErrorPopup -> {
                errorDialogState.show(
                    description = sideEffect.description,
                    code = sideEffect.code
                )
            }
        }
    }

    if (errorDialogState.isShown) {
        BaseDialog(
            description = errorDialogState.value.description,
            code = errorDialogState.value.code,
            onClickConfirm = {
                errorDialogState.hide()
                onFinish()
            }
        )
    }
}