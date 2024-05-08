package com.ignatlegostaev.inhabitroutine.core.presentation.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BaseSnackBar(
    snackBarData: SnackbarData,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Snackbar(
        snackbarData = snackBarData,
        containerColor = containerColor,
        contentColor = contentColor
    )
}