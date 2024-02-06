package com.example.myapplication.ui.interests

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.R
import com.example.myapplication.data.interests.InterestSection
import com.example.myapplication.data.interests.TopicSelection


@Composable
fun rememberTabContent(
    viewModel: InterestsViewModel
): List<TabSection> {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val topicSection = TabSection(Sections.Topics) {
        val selectedTopics by viewModel.selectedTopics.collectAsStateWithLifecycle()

        TabWithSections(
            sections = uiState.topics,
            selectedSections = selectedTopics,
            onTopicSelectCallback = { viewModel.toggleTopic(it) }
        )
    }

    val peopleSection = TabSection(Sections.People) {
        val peopleSection by viewModel.selectedPeople.collectAsStateWithLifecycle()

        TabWithTopics(
            sections = uiState.people,
            selectedSections = peopleSection,
            onTopicSelectCallback = { viewModel.togglePerson(it) }
        )
    }

    val publicationSection = TabSection(Sections.Publications) {
        val publicationSection by viewModel.selectedPublications.collectAsStateWithLifecycle()

        TabWithTopics(
            sections = uiState.publications,
            selectedSections = publicationSection,
            onTopicSelectCallback = { viewModel.togglePublication(it) }
        )
    }

    return listOf(topicSection, peopleSection, publicationSection)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestsScreen(
    updateSectionCallback: (Sections) -> Unit,
    currentSections: Sections,
    tabSection: List<TabSection>,
    openDrawerCallback: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { openDrawerCallback() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_jetnews_logo),
                            contentDescription = stringResource(id = R.string.cd_open_navigation_drawer)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.interests_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = {
                        Toast
                            .makeText(context, "Search is not yet Implemented.", Toast.LENGTH_LONG)
                            .show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        InterestsScreenContent(
            updateSectionCallback = updateSectionCallback,
            currentSections = currentSections,
            tabSection = tabSection,
            screenModifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun InterestsScreenContent(
    updateSectionCallback: (Sections) -> Unit,
    currentSections: Sections,
    tabSection: List<TabSection>,
    screenModifier: Modifier
) {
    Column(
        modifier = screenModifier
    ) {
        InterestsTabRow(
            currentTab = currentSections,
            tabList = tabSection,
            updateSectionCallback = updateSectionCallback
        )
        Divider()
        Box(modifier = Modifier.weight(1f)) {
            tabSection.firstOrNull{ it.sections == currentSections}?.content?.let {
                it()
            }
        }
    }
}

@Composable
fun TabWithSections(
    sections: List<InterestSection>,
    selectedSections: Set<TopicSelection>,
    onTopicSelectCallback: (TopicSelection) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())

    ) {
        sections.forEach { section ->
            Text(
                text = section.title,
                modifier = Modifier
                    .padding(16.dp)
                    .semantics { heading() },
                style = MaterialTheme.typography.titleMedium
            )
            section.interests.forEach { interest ->
                TabItem(
                    itemTitle = interest,
                    selected = selectedSections.contains(TopicSelection(section.title, interest)),
                    toggleCallback = { onTopicSelectCallback(TopicSelection(section.title, interest)) }
                )
            }
        }
    }
}

@Composable
fun TabWithTopics(
    sections: List<String>,
    selectedSections: Set<String>,
    onTopicSelectCallback: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        sections.forEach { interest ->
            TabItem(
                itemTitle = interest,
                selected = selectedSections.contains(interest),
                toggleCallback = { onTopicSelectCallback(interest) }
            )
        }
    }
}

@Composable
fun TabItem(
    itemTitle: String,
    selected: Boolean,
    toggleCallback: () -> Unit
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.toggleable(
                value = selected,
                onValueChange = { toggleCallback() }
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image = painterResource(R.drawable.placeholder_1_1)
            Image(
                painter = image,
                contentDescription = null, // decorative
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(
                text = itemTitle,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f), // Break line if the title is too long
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.width(16.dp))
            SelectTopicButton(selected = selected)
        }
        Divider(
            modifier = Modifier.padding(start = 72.dp, top = 8.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }
}