package com.example.myapplication.data.interests

import com.example.myapplication.utils.addOrRemove
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeInterestsRepository: InterestsRepository {

    private val topics by lazy {
        listOf(
            InterestSection("Android", listOf("Jetpack Compose", "Kotlin", "Jetpack")),
            InterestSection(
                "Programming",
                listOf("Kotlin", "Declarative UIs", "Java", "Unidirectional Data Flow", "C++")
            ),
            InterestSection("Technology", listOf("Pixel", "Google"))
        )
    }

    private val people by lazy {
        listOf(
            "Kobalt Toral",
            "K'Kola Uvarek",
            "Kris Vriloc",
            "Grala Valdyr",
            "Kruel Valaxar",
            "L'Elij Venonn",
            "Kraag Solazarn",
            "Tava Targesh",
            "Kemarrin Muuda"
        )
    }

    private val publications by lazy {
        listOf(
            "Kotlin Vibe",
            "Compose Mix",
            "Compose Breakdown",
            "Android Pursue",
            "Kotlin Watchman",
            "Jetpack Ark",
            "Composeshack",
            "Jetpack Point",
            "Compose Tribune"
        )
    }

    override suspend fun getTopics(): com.example.myapplication.data.Result<List<InterestSection>> {
        return com.example.myapplication.data.Result.Success(topics)
    }

    override suspend fun getPeople(): com.example.myapplication.data.Result<List<String>> {
        return com.example.myapplication.data.Result.Success(people)
    }

    override suspend fun getPublications(): com.example.myapplication.data.Result<List<String>> {
        return com.example.myapplication.data.Result.Success(publications)
    }


    /**
     * Observing/Toggle topics,persons,publications.
     */
    val selectedTopics = MutableStateFlow(setOf<TopicSelection>())
    val selectedPeople = MutableStateFlow(setOf<String>())
    val selectedPublication = MutableStateFlow(setOf<String>())

    override suspend fun toggleTopics(topic: TopicSelection) {
        selectedTopics.update {
            it.addOrRemove(topic)
        }
    }

    override suspend fun togglePeople(person: String) {
        selectedPeople.update {
            it.addOrRemove(person)
        }
    }

    override suspend fun togglePublication(publication: String) {
        selectedPublication.update {
            it.addOrRemove(publication)
        }
    }

    override fun observeTopicsSelected(): Flow<Set<TopicSelection>> = selectedTopics

    override fun observePeopleSelected(): Flow<Set<String>> = selectedPeople

    override fun observePublicationSelected(): Flow<Set<String>> = selectedPublication

}