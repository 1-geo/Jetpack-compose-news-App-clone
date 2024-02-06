package com.example.myapplication.ui.interests

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun InterestsRoute(
    interestsViewModel: InterestsViewModel,
    isLargeExpandedScreen: Boolean,
    openDrawer: () -> Unit
) {
    val tabContent = rememberTabContent(viewModel = interestsViewModel)
    val (currentSection, updateSection) = rememberSaveable {
        mutableStateOf(tabContent.first().sections)
    }

    InterestsScreen(
        updateSectionCallback = updateSection,
        currentSections = currentSection,
        tabSection = tabContent,
        openDrawerCallback = openDrawer
    )
}