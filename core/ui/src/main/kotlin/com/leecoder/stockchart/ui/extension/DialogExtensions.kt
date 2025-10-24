package com.leecoder.stockchart.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

typealias ErrorDialogState = MutableState<ErrorDialogData>

data class ErrorDialogData(
    val isShown: Boolean = false,
    val description: String? = null,
    val code: String? = null,
)

@Composable
fun <SideEffect> rememberErrorDialogState(
    initial: ErrorDialogData = ErrorDialogData(),
): ErrorDialogState = rememberSaveable(
    saver = Saver<MutableState<ErrorDialogData>, List<Any?>>(
        save = { listOf(it.value.isShown, it.value.description, it.value.code) },
        restore = { list ->
            mutableStateOf(
                ErrorDialogData(
                    isShown = list[0] as Boolean,
                    description = list[1] as String?,
                    code = list[2] as String?,
                )
            )
        }
    ),
) {
    mutableStateOf(initial)
}

val ErrorDialogState.isShown get() = this.value.isShown

fun ErrorDialogState.show(
    description: String? = null,
    code: String? = null
) {
    this.value = this.value.copy(
        isShown = true,
        description = description,
        code = code,
    )
}

fun ErrorDialogState.hide() {
    this.value = this.value.copy(
        isShown = false,
        description = null,
        code = null,
    )
}