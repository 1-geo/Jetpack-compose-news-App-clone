package com.example.myapplication.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHomeCallback: () -> Unit,
    navigateToInterestsCallback: () -> Unit,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet {

        // logo
        NewsLogo()

        // home
        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.home_title)) },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            selected = currentRoute == NewsNavigationDestinations.HOME_ROUTE,
            onClick = { navigateToHomeCallback(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        // interests
        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.interests_title)) },
            icon = { Icon(Icons.Filled.ListAlt, contentDescription = null) },
            selected = currentRoute == NewsNavigationDestinations.INTERESTS_ROUTE,
            onClick = { navigateToInterestsCallback(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}


@Composable
fun NewsLogo() {
    Row(
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_jetnews_logo), contentDescription = "News Logo")
        Icon(painter = painterResource(id = R.drawable.ic_jetnews_wordmark), contentDescription = "News Logo")
    }
}