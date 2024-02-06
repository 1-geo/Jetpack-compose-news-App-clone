package com.example.myapplication.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.interests.InterestsRepository
import com.example.myapplication.data.posts.PostsRepository
import com.example.myapplication.ui.theme.NewsAppTheme
import kotlinx.coroutines.launch


/**
 * - Summary of Concepts Learned -
 *
 *
 * ** UI **
 * remember() - if cached value exists return it else rerun block()
 * remember(key) - if cached value exists and key not changed return it else return rerun block()
 *
 * rememberSaveable() - it works just like remember but cache stays across device rotation like config changes.
 * Ex: ArticleScreen, dialog
 * more in Home is also showing Dialog with remember()
 *
 *
 *
 *
 * Image(painterResourse = r.drawable)
 * Icon(vector = Icons.filled.)
 * IconButton, IconToggleButton
 * Modifier.clickable { }
 *
 * Spacer
 * Spacer(modifier = Modifier.height(6.dp)) OR
 * Spacer(modifier = Modifier.width(6.dp))
 *
 * ModalNavigationDrawer
 * ModalDrawerSheet
 *
 * stringResource
 *
 * LazyColumn with LazyListState
 * Row horizontalState, height(IntrinsicSize.Max)
 *
 * CenterAlignedTopAppBar using Scaffold(topBar, bottomBar)
 *
 * scoped Composable function in ArticleDetails for LazyColumn
 *
 * AnnotatedString using Text
 *
 * Modifier.clip can use CircleShape,RoundedCornershape or MaterialTheme.shapes.medium (can override in Shapes in theme)
 *
 * BackHandler to handle hardware back action
 *
 * BottomAppBar with content see ArticleDetails
 *
 * Side affect for scroll state in HomeRoute
 *
 *
 * weighted
  1 + 2 + 1 = 4
 * 1/4 2/4 1/4
 * Modifier.weight(1)
 *  Modifier.weight can only used Inside a Row or Column. Refers to width or height.
 *  Set one child that could have long text to 1f. It allows to measure all other siblings and whatever space remains give me all of that.
 *  Ref: https://medium.com/@theAndroidDeveloper/jetpack-compose-trick-the-hidden-secret-of-the-weight-modifier-640daf63b151
 *  Sometimes compiler issue if not in right scope OR if the Row or Column is glance not the compose.ui package!!
 *
 *
 * Recomposition:
 * If a composable A, return a value and its State changes. then it will recompose
 * at the call site. Ex: rememberTabContent() in Interests
 *
 * Interests Screen
 * 3 Composables.
 *  Click on tab needs to recompose whole screen
 *  Click on item needs to recompose only that screen
 *  Ui state change need to recompose whole screen
 *
 * State change can be passed as a lambda. Check out updateSection in InterestsRoute
 *
 * Column can be scrollable by adding
 * modifier = Modifier
 *             .verticalScroll(rememberScrollState())
 *
 *
 * Composable Tree:
 * A composable function is not like regular function.
 * It takes params and emits a Node.
 * The runtime can call it any N or in any order.
 *
 * Row
 *     Image=image
 *     Column
 *         Text=fname
 *         Text=lname
 *
 * Turns into Node Tree like below:
 *                 Row
 *          Image      Column
 *                   Text    Text
 *
 * Updates are done on exactly where the Node not whole tree.
 *
 *
 * ** Flow, Data layer **
 * stateFlow vs stateIn
 *
 * TODO
 * pass data behind screen in nav graph
 * complicated navigation's like login, home etc
 * small, medium tests
 */
@Composable
fun NewsApp(
    repository: PostsRepository,
    interestsRepository: InterestsRepository,
    widthSizeClass: WindowWidthSizeClass) {

    NewsAppTheme {
        val navController = rememberNavController()
        val navigateActions = remember(key1 = navController) {
            NewsNavigationItem(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        // when navController changes the back stack due to a NavController.navigate or NavController.popBackStack
        // this will trigger a recompose and return the top entry on the back stack.
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: NewsNavigationDestinations.HOME_ROUTE


        // check if large screen. disables drawer and shows AppRail
        val isExpandedLargeScreen = widthSizeClass == WindowWidthSizeClass.Expanded

        // remember state so that if opened and rotated
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        // drawer
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    navigateToHomeCallback = navigateActions.navigateToHome,
                    navigateToInterestsCallback = navigateActions.navigateToInterests,
                    closeDrawer = { coroutineScope.launch { drawerState.close() } })
            }) {

            // home
            NewsAppGraph(
                repository = repository,
                interestsRepository = interestsRepository,
                navController = navController,
                isExpandedLargeScreen = isExpandedLargeScreen,
                openDrawerCallback = { coroutineScope.launch { drawerState.open() } })
        }
    }

}