package com.ignatlegostaev.inhabitroutine.core.presentation.ui.common

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BaseOutlinedDropdown(
    allOptions: List<T>,
    currentOption: T,
    optionText: (T) -> String,
    onOptionClick: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var isDropdownExpanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = { isDropdownExpanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = optionText(currentOption),
            onValueChange = {},
            modifier = Modifier.menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isDropdownExpanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            readOnly = true,
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            allOptions.forEach { option ->
                val text = remember {
                    optionText(option)
                }
                DropdownMenuItem(
                    text = {
                        Text(text = text)
                    },
                    onClick = {
                        onOptionClick(option)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
    LaunchedEffect(isDropdownExpanded) {
        if (!isDropdownExpanded) {
            focusManager.clearFocus(true)
        }
    }
}