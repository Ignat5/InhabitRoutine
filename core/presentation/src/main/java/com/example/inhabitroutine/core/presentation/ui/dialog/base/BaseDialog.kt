package com.example.inhabitroutine.core.presentation.ui.dialog.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

private val DialogPadding = 24.dp
private const val DEFAULT_STATIC_DIALOG_SCREEN_FRACTION = 0.5f

sealed interface BaseDialogActionType {
    data object NoAction : BaseDialogActionType
    data class ConfirmDismiss(
        val confirmButton: @Composable () -> Unit,
        val dismissButton: (@Composable () -> Unit)? = null,
    ) : BaseDialogActionType
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable ColumnScope.() -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = properties
    ) {
        Surface(
            modifier = modifier,
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.padding(DialogPadding)) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    title: (@Composable () -> Unit)? = null,
    actionType: BaseDialogActionType = BaseDialogActionType.NoAction,
    content: @Composable ColumnScope.() -> Unit
) {
    BaseDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = properties
    ) {
        title?.let {
            title()
            AfterTitleSpacer()
        }
        content()
        when (actionType) {
            is BaseDialogActionType.NoAction -> Unit
            is BaseDialogActionType.ConfirmDismiss -> {
                BeforeButtonsSpacer()
                RowActionButtons(
                    confirmButton = actionType.confirmButton,
                    dismissButton = actionType.dismissButton
                )
            }
        }
    }
}

@Composable
fun BaseStaticDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    screenFraction: Float = DEFAULT_STATIC_DIALOG_SCREEN_FRACTION,
    properties: DialogProperties = DialogProperties(),
    title: (@Composable () -> Unit)? = null,
    actionType: BaseDialogActionType = BaseDialogActionType.NoAction,
    content: @Composable ColumnScope.() -> Unit
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val dialogHeight = remember(screenHeightDp) {
        (screenHeightDp * screenFraction).dp
    }
    BaseDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.then(
            Modifier.height(dialogHeight)
        ),
        properties = properties,
        title = title,
        actionType = actionType
    ) {
//        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            content()
        }
//        HorizontalDivider()
    }
}

@Composable
fun BaseMessageDialog(
    onDismissRequest: () -> Unit,
    titleText: String,
    messageText: String,
    actionType: BaseDialogActionType,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
) {
    BaseDialog(
        onDismissRequest = onDismissRequest,
        title = {
            BaseDialogDefaults.BaseDialogTitle(titleText = titleText)
        },
        actionType = actionType,
        properties = properties,
        modifier = modifier
    ) {
        Text(
            text = messageText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AfterTitleSpacer() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun BeforeButtonsSpacer() {
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun RowActionButtons(
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
    ) {
        dismissButton?.invoke()
        confirmButton.invoke()
    }
}

object BaseDialogDefaults {

    @Composable
    fun BaseDialogTitle(
        titleText: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            text = titleText,
            color = AlertDialogDefaults.titleContentColor,
            style = MaterialTheme.typography.headlineSmall,
            modifier = modifier,
            textAlign = TextAlign.Start
        )
    }

    @Composable
    fun BaseActionButton(
        text: String,
        enabled: Boolean = true,
        onClick: () -> Unit
    ) {
        TextButton(
            onClick = onClick,
            enabled = enabled
        ) {
            Text(text = text)
        }
    }

}