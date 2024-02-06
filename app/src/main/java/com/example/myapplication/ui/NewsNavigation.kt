package com.example.myapplication.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myapplication.ui.NewsNavigationDestinations.HOME_ROUTE
import com.example.myapplication.ui.NewsNavigationDestinations.INTERESTS_ROUTE

object NewsNavigationDestinations {
    val HOME_ROUTE = "home"
    val INTERESTS_ROUTE = "interests"
}



class NewsNavigationItem(
    navController: NavController
) {

    val navigateToHome: () -> Unit = {
        navController.navigate(HOME_ROUTE) {

            // Pop up to a given destination before navigating.
            // This pops all non-matching destinations from the back stack
            // until this destination is found.
            popUpTo(navController.graph.findStartDestination().id) {
               saveState = true
            }

            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true

            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    val navigateToInterests: () -> Unit = {
        navController.navigate(INTERESTS_ROUTE) {
            // Pop up to a given destination before navigating.
            // This pops all non-matching destinations from the back stack
            // until this destination is found.
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true

            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

}