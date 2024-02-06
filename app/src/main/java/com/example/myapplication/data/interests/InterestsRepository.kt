package com.example.myapplication.data.interests

import kotlinx.coroutines.flow.Flow

// topic
data class InterestSection(val title: String, val interests: List<String>)

interface InterestsRepository {

    suspend fun getTopics(): com.example.myapplication.data.Result<List<InterestSection>>
    suspend fun getPeople(): com.example.myapplication.data.Result<List<String>>
    suspend fun getPublications(): com.example.myapplication.data.Result<List<String>>

    suspend fun toggleTopics(topic: TopicSelection)
    suspend fun togglePeople(person: String)
    suspend fun togglePublication(publication: String)

    fun observeTopicsSelected(): Flow<Set<TopicSelection>>
    fun observePeopleSelected(): Flow<Set<String>>
    fun observePublicationSelected(): Flow<Set<String>>

}

// selected topic
data class TopicSelection(val section: String, val topic: String)