package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jetnews.model.PostsFeed
import com.example.myapplication.R
import com.example.myapplication.data.Result
import com.example.myapplication.data.posts.PostsRepository
import com.example.myapplication.utils.ErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class HomeViewModel(
    private val postsRepository: PostsRepository,
    preSelectedPostId: String?
) : ViewModel() {

    /**
     * Class representing UI State
     */
    data class HomeViewModelState(
        val postsFeed: PostsFeed? = null,
        val selectedPostId: String? = null, // TODO back selectedPostId in a SavedStateHandle
        val isArticleOpen: Boolean = false,
        val favorites: Set<String> = emptySet(),
        val isLoading: Boolean = false,
        val errorMessages: List<ErrorMessage> = emptyList(),
        val searchInput: String = "",
    ) {
        enum class State {
            NoPosts,
            HasPosts
        }

        fun getState(): State {
            return if (postsFeed != null) State.HasPosts else State.NoPosts
        }
    }

    // internal MutableStateFlow
    private val _uiState = MutableStateFlow(
        HomeViewModelState(
            isLoading = true,
            selectedPostId = preSelectedPostId,
            isArticleOpen = preSelectedPostId != null
        )
    )

    // exposed StateFlow for UI to consume
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            _uiState.value
        )


    init {
        refreshPosts()

        // observe favorites
        viewModelScope.launch {
            postsRepository.observeFavorites().collect { favorites ->
                _uiState.update { it.copy(favorites = favorites) }
            }
        }
    }


    /**
     * refresh Posts
     */
    fun refreshPosts() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = postsRepository.getPostsFeed()
            _uiState.update {
                when (result) {
                    is Result.Success -> it.copy(postsFeed = result.data, isLoading = false)
                    is Result.Error -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }


    /**
     * Selects the given article to view more information about it.
     */
    fun selectArticle(postId: String) {
        // Treat selecting a detail as simply interacting with it
        interactedWithArticleDetails(postId)
    }

    /**
     * Notify that an error was displayed on the screen
     */
    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }


    /**
     * toggle favorites
     */
    fun toggleFavorite(postId: String) {
        viewModelScope.launch {
            postsRepository.toggleFavorites(postId)
        }
    }


    /**
     * Notify that the user interacted with the feed
     */
    fun interactedWithFeed() {
        _uiState.update {
            it.copy(isArticleOpen = false)
        }
    }

    /**
     * Notify that the user interacted with the article details
     */
    fun interactedWithArticleDetails(postId: String) {
        _uiState.update {
            it.copy(
                selectedPostId = postId,
                isArticleOpen = true
            )
        }
    }


    /**
     * Notify that the user updated the search query
     */
    fun onSearchInputChanged(searchInput: String) {
        _uiState.update {
            it.copy(searchInput = searchInput)
        }
    }


    /**
     * Factory for HomeViewModel that takes PostsRepository as a dependency
     */
    companion object {
        fun provideFactory(
            postsRepository: PostsRepository,
            preSelectedPostId: String? = null
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(postsRepository, preSelectedPostId) as T
            }
        }
    }

}