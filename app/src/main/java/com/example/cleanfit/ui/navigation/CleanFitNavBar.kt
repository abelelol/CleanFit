package com.example.cleanfit.ui.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey

@Composable
fun CleanFitNavBar(
    modifier: Modifier = Modifier,
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit
){
    BottomAppBar(
        modifier = modifier,
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { (key, value) ->
            NavigationBarItem(
                selected = key == selectedKey,
                onClick = { onSelectKey(key as NavKey) },
                icon = {
                    Icon(
                        imageVector = value.icon,
                        contentDescription = value.title
                    )
                },
                label = {
                    Text(text = value.title)
                }
            )

        }
    }
}