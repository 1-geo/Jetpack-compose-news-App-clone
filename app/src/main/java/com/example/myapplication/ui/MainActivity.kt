package com.example.myapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.myapplication.NewsApplication
import com.example.myapplication.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as NewsApplication).getRepository()
        val interestsRepository = (application as NewsApplication).getInterestsRepository()

        setContent {
            val widthSizeClass = calculateWindowSizeClass(activity = this).widthSizeClass
            NewsApp(repository = repository, interestsRepository = interestsRepository, widthSizeClass = widthSizeClass)
        }
    }


}
