package com.leecoder.stockchart.app.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.leecoder.stockchart.app.viewmodel.MainSideEffect
import com.leecoder.stockchart.app.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.postToken()
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffect.TokenErrorPopup -> {
                Log.e("heesang", "MainScreen: 에러 팝업 출력")
            }
        }
    }
}