package com.example.inhabitroutine.navigation.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.inhabitroutine.core.presentation.R

private const val DRAWER_END_PADDING_DP = 64

@Composable
fun RootModalNavigationDrawer(
    currentBackStackEntry: NavBackStackEntry?,
    drawerState: DrawerState,
    onRootDestinationClick: (RootDestination) -> Unit,
    content: @Composable () -> Unit
) {
    val allMainDestinations = remember { RootDestination.allMainDestinations }
    val allMainDestinationRoutes = remember { allMainDestinations.map { it.destination.route } }
    val gesturesEnabled = remember(currentBackStackEntry) {
        currentBackStackEntry?.destination?.route in allMainDestinationRoutes
    }
//    val allOtherDestinations = remember { RootDestination.allOtherDestinations }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        content = content,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.padding(end = DRAWER_END_PADDING_DP.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    AppTitle()
                    ItemDivider()
                    ItemsRootDestination(
                        items = allMainDestinations,
                        currentBackStackEntry = currentBackStackEntry,
                        onClick = onRootDestinationClick
                    )
//                    ItemDivider()
//                    ItemsRootDestination(
//                        items = allOtherDestinations,
//                        currentBackStackEntry = currentBackStackEntry,
//                        onClick = onRootDestinationClick
//                    )
                }
            }
        },
    )
}

@Composable
private fun ItemsRootDestination(
    items: List<RootDestination>,
    currentBackStackEntry: NavBackStackEntry?,
    onClick: (RootDestination) -> Unit
) {
    items.forEach { item ->
        val isSelected = remember(currentBackStackEntry) {
            currentBackStackEntry?.destination?.hierarchy?.any { it.route == item.destination.route } == true
        }
        ItemRootDestination(
            rootDestination = item,
            isSelected = isSelected,
            onClick = {
                onClick(item)
            }
        )
    }
}

@Composable
private fun ItemRootDestination(
    rootDestination: RootDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(text = stringResource(id = rootDestination.titleResId))
        },
        icon = {
            Icon(
                painter = painterResource(id = rootDestination.iconResId),
                contentDescription = null
            )
        },
        selected = isSelected,
        onClick = onClick
    )
}

@Composable
private fun AppTitle() {
    Text(
        text = stringResource(id = com.example.inhabitroutine.R.string.app_name),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ItemDivider(
    spacerHeight: Dp = 16.dp
) {
    Spacer(modifier = Modifier.height(spacerHeight))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(spacerHeight))
}