package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.interests.FakeInterestsRepository
import com.example.myapplication.data.interests.InterestsRepository
import com.example.myapplication.data.posts.FakePostsRepository
import com.example.myapplication.data.posts.PostsRepository


class NewsApplication: Application() {

    companion object {
        const val JETNEWS_APP_URI = "https://developer.android.com/jetnews"
    }

    private val repository = FakePostsRepository()
    private val interestsRepository = FakeInterestsRepository()

    override fun onCreate() {
        super.onCreate()
    }

    fun getRepository(): PostsRepository {
        return repository
    }

    fun getInterestsRepository(): InterestsRepository {
        return interestsRepository
    }
}