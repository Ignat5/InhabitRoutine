package com.ignatlegostaev.inhabitroutine.core.presentation.ui.common

import android.view.ViewTreeObserver
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

private const val DEFAULT_DEBOUNCE_MILLIS = 200L

@OptIn(FlowPreview::class)
@Composable
fun BaseFocusedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    valueValidator: ((String) -> Boolean)? = null,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val focusRequester = remember { FocusRequester() }
    val viewTreeObserver = LocalView.current.viewTreeObserver
    BaseOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .focusRequester(focusRequester),
        valueValidator = valueValidator,
        label = label,
        placeholder = placeholder,
        supportingText = supportingText,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
    val callback: ViewTreeObserver.OnWindowFocusChangeListener = remember {
        ViewTreeObserver.OnWindowFocusChangeListener { isFocused ->
            if (isFocused)  {
                focusRequester.requestFocus()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewTreeObserver.addOnWindowFocusChangeListener(callback)
    }
    DisposableEffect(Unit) {
        onDispose {
            viewTreeObserver.removeOnWindowFocusChangeListener(callback)
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun BaseOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    valueValidator: ((String) -> Boolean)? = null,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var currentValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }
    OutlinedTextField(
        value = currentValue,
        onValueChange = { textFieldValue ->
            run {
                valueValidator?.invoke(textFieldValue.text)?.let { isValid ->
                    if (!isValid) return@run
                }
                currentValue = textFieldValue
            }
        },
        label = label,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        supportingText = supportingText,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        modifier = modifier
    )

    LaunchedEffect(Unit) {
        snapshotFlow { currentValue }
            .distinctUntilChanged()
            .debounce(DEFAULT_DEBOUNCE_MILLIS)
            .collectLatest { value ->
                onValueChange(value.text)
            }
    }
}