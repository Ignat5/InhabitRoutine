package com.ignatlegostaev.inhabitroutine.core.presentation.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ignatlegostaev.inhabitroutine.core.presentation.R

@Composable
fun <T> BaseFilterChipWithDropdownMenu(
    allItems: List<T>,
    currentItem: T?,
    textByItem: (T) -> String,
    label: @Composable () -> Unit,
    isFilterActive: Boolean,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    showArrowDropdown: Boolean = true,
    showCurrent: Boolean = true
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    BaseChipWithDropdownMenu(
        chip = {
            val trailingIcon: @Composable (() -> Unit)? = if (showArrowDropdown) {
                val icon: @Composable () -> Unit = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                        modifier = Modifier.rotate(
                            if (isExpanded) 180f else 0f
                        ),
                        contentDescription = null
                    )
                }
                icon
            } else null
            FilterChip(
                selected = isFilterActive,
                onClick = { isExpanded = !isExpanded },
                label = label,
                trailingIcon = trailingIcon,
                modifier = modifier
            )
        },
        menu = {
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                allItems.forEach { item ->
                    val itemText = remember {
                        textByItem(item)
                    }
                    val isCurrent = remember { item == currentItem }
                    DropdownMenuItem(
                        text = {
                            Text(text = itemText)
                        },
                        trailingIcon = if (showCurrent && isCurrent) {
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_check),
                                    modifier = Modifier.size(16.dp),
                                    contentDescription = null
                                )
                            }
                        } else null,
                        onClick = {
                            isExpanded = false
                            onItemClick(item)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun BaseChipWithDropdownMenu(
    chip: @Composable () -> Unit,
    menu: @Composable () -> Unit,
) {
    Box {
        chip()
        Box(modifier = Modifier.align(Alignment.BottomStart)) {
            menu()
        }
    }
}