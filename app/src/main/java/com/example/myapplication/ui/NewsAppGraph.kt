package com.example.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.myapplication.NewsApplication.Companion.JETNEWS_APP_URI
import com.example.myapplication.data.interests.InterestsRepository
import com.example.myapplication.data.posts.FakePostsRepository
import com.example.myapplication.data.posts.PostsRepository
import com.example.myapplication.ui.home.HomeRoute
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.interests.InterestsRoute
import com.example.myapplication.ui.interests.InterestsViewModel

const val POST_ID = "postId"

@Composable
fun NewsAppGraph(
    repository: PostsRepository,
    interestsRepository: InterestsRepository,
    isExpandedLargeScreen: Boolean,
    openDrawerCallback: () -> Unit,
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = NewsNavigationDestinations.HOME_ROUTE) {


        composable(
            route = NewsNavigationDestinations.HOME_ROUTE,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$JETNEWS_APP_URI/${NewsNavigationDestinations.HOME_ROUTE}?$POST_ID={$POST_ID}"
                }
            )
        ) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(
                    postsRepository = repository,
                    preSelectedPostId = it.arguments?.getString(POST_ID)
                )
            )

            HomeRoute(
                homeViewModel = homeViewModel,
                isLargeExpandedScreen = isExpandedLargeScreen,
                openDrawer = { openDrawerCallback() }
            )
        }


        composable(route = NewsNavigationDestinations.INTERESTS_ROUTE) {
            val interestsViewModel: InterestsViewModel = viewModel(
                factory = InterestsViewModel.provideFactory(interestsRepository)
            )

            InterestsRoute(
                interestsViewModel = interestsViewModel,
                isLargeExpandedScreen = isExpandedLargeScreen,
                openDrawer = openDrawerCallback
            )
        }
    }
}