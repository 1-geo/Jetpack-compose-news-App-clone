package com.example.myapplication.ui.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.interests.InterestSection
import com.example.myapplication.data.interests.InterestsRepository
import com.example.myapplication.data.interests.TopicSelection
import com.example.myapplication.data.successOr
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InterestsViewModel(
    val interestsRepository: InterestsRepository
):ViewModel() {

    data class InterestsUiState(
        val topics: List<InterestSection> = emptyList(),
        val people: List<String> = emptyList(),
        val publications: List<String> = emptyList(),
        val loading: Boolean = false
    )

    val _uiState = MutableStateFlow(InterestsUiState(loading = true))
    val uiState = _uiState.asStateFlow()


    init {
        refreshAll()
    }

    fun refreshAll() {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            // trigger in parallel
            val deferredTopics = async { interestsRepository.getTopics() }
            val deferredPeoples = async { interestsRepository.getPeople() }
            val deferredPublications = async { interestsRepository.getPublications() }

            val topics = deferredTopics.await().successOr(emptyList())
            val peoples = deferredPeoples.await().successOr(emptyList())
            val publications = deferredPublications.await().successOr(emptyList())

            _uiState.update {
                it.copy(
                    loading = false,
                    topics = topics,
                    people = peoples,
                    publications = publications
                )
            }
        }
    }


    // selections are not inside uiState unlike HomeViewModel.
    // uses StateIn with whileSubscribed
    val selectedTopics = interestsRepository.observeTopicsSelected()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )
    val selectedPeople = interestsRepository.observePeopleSelected()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )
    val selectedPublications = interestsRepository.observePublicationSelected()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )

    fun toggleTopic(topic: TopicSelection) {
        viewModelScope.launch {
            interestsRepository.toggleTopics(topic)
        }
    }

    fun togglePerson(person: String) {
        viewModelScope.launch {
            interestsRepository.togglePeople(person)
        }
    }

    fun togglePublication(publication: String) {
        viewModelScope.launch {
            interestsRepository.togglePublication(publication)
        }
    }



    /**
     * Factory for InterestsViewModel that takes PostsRepository as a dependency
     */
    companion object {
        fun provideFactory(
            interestsRepository: InterestsRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InterestsViewModel(interestsRepository) as T
            }
        }
    }
}