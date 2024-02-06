package com.example.myapplication.ui.interests

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.NewsAppTheme


enum class Sections(@StringRes val titleResId: Int) {
    Topics(R.string.interests_section_topics),
    People(R.string.interests_section_people),
    Publications(R.string.interests_section_publications)
}
data class TabSection(val sections: Sections, val content: @Composable () -> Unit)


@Composable
fun InterestsTabRow(
    currentTab: Sections,
    tabList: List<TabSection>,
    updateSectionCallback: (Sections) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = tabList.indexOfFirst { it.sections == currentTab }
    TabRow(
        selectedTabIndex = selectedIndex,
        contentColor = MaterialTheme.colorScheme.primary
        ) {
        tabList.forEachIndexed { index, section ->
            val colorText = if (selectedIndex == index)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)

            Tab(
                selected = index == selectedIndex,
                onClick = { updateSectionCallback(section.sections) },
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Text(
                    text = stringResource(id = section.sections.titleResId),
                    color = colorText,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.paddingFromBaseline(top = 20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewInterestsTab() {
    val tabs = listOf(
        TabSection(sections = Sections.Topics, {}),
        TabSection(sections = Sections.People, {}),
        TabSection(sections = Sections.Publications, {})
    )
    NewsAppTheme {
        InterestsTabRow(currentTab = tabs.first().sections, tabList = tabs, updateSectionCallback = {})
    }
}