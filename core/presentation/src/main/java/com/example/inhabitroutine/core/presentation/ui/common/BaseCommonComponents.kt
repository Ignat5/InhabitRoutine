package com.example.inhabitroutine.core.presentation.ui.common

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.inhabitroutine.core.presentation.R

@Composable
fun CreateTaskFAB(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null
        )
    }
}